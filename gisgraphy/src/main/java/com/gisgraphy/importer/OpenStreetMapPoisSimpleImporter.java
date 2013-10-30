/*******************************************************************************
 *   Gisgraphy Project 
 * 
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 * 
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *   Lesser General Public License for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA
 * 
 *  Copyright 2008  Gisgraphy project 
 *  David Masclet <davidmasclet@gisgraphy.com>
 *  
 *  
 *******************************************************************************/
package com.gisgraphy.importer;

import static com.gisgraphy.fulltext.Constants.ONLY_ADM_PLACETYPE;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.hibernate.FlushMode;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.geoloc.entity.Adm;
import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.ZipCode;
import com.gisgraphy.domain.repository.IAdmDao;
import com.gisgraphy.domain.repository.ICityDao;
import com.gisgraphy.domain.repository.IIdGenerator;
import com.gisgraphy.domain.repository.ISolRSynchroniser;
import com.gisgraphy.domain.valueobject.GISSource;
import com.gisgraphy.domain.valueobject.NameValueDTO;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.fulltext.Constants;
import com.gisgraphy.fulltext.FullTextSearchEngine;
import com.gisgraphy.fulltext.FulltextQuery;
import com.gisgraphy.fulltext.FulltextResultsDto;
import com.gisgraphy.fulltext.IFullTextSearchEngine;
import com.gisgraphy.fulltext.SolrResponseDto;
import com.gisgraphy.helper.GeolocHelper;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 * Import the POI from an (pre-processed) openStreet map data file.
 * The goal of this importer is to cross information between geonames and Openstreetmap. 
 * 
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class OpenStreetMapPoisSimpleImporter extends AbstractSimpleImporterProcessor {
	
    
    public static final Output MINIMUM_OUTPUT_STYLE = Output.withDefaultFormat().withStyle(OutputStyle.SHORT);

	protected IIdGenerator idGenerator;
    
    protected ICityDao cityDao;
    
    protected IAdmDao admDao;
    
    protected ISolRSynchroniser solRSynchroniser;
    
    protected IFullTextSearchEngine fullTextSearchEngine;
    
    private static final Pattern pattern = Pattern.compile("(\\w+)\\s\\d+.*",Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#flushAndClear()
     */
    @Override
    protected void flushAndClear() {
    	cityDao.flushAndClear();
    }
    
    @Override
    protected void setup() {
        super.setup();
        //temporary disable logging when importing
        FullTextSearchEngine.disableLogging=true;
        logger.info("reseting Openstreetmap generatedId");
        idGenerator.sync();
    }
    

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#getFiles()
     */
    @Override
    protected File[] getFiles() {
	return ImporterHelper.listCountryFilesToImport(importerConfig.getOpenStreetMapCitiesDir());
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#getNumberOfColumns()
     */
    @Override
    protected int getNumberOfColumns() {
	return 7;
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#processData(java.lang.String)
     */
    @Override
    protected void processData(String line) throws ImporterException {
	String[] fields = line.split("\t");
	String countrycode=null;
	String name=null;
	Point location=null;
	
	//
	// Line table has the following fields :
	// --------------------------------------------------- 
	//0 : Node type;1 : id; 2 : name;3 : countrycode;4 : alternatenames; 
	//5 : location,	6 : amenity;
	//
	//
	checkNumberOfColumn(fields);
	
	// name
	if (!isEmptyField(fields, 3, false)) {
	    name=fields[3].trim();
	    if (name==null){
	    	return;
	    }
	}
	//countrycode
	if (!isEmptyField(fields, 4, true)) {
	    countrycode=fields[4].trim().toUpperCase();
	}
	//location
	if (!isEmptyField(fields, 5, false)) {
	    try {
		location = (Point) GeolocHelper.convertFromHEXEWKBToGeometry(fields[5]);
	    } catch (RuntimeException e) {
	    	logger.warn("can not parse location for "+fields[6]+" : "+e);
	    	return;
	    }
	}
	City city=null;
	SolrResponseDto  nearestCity = getNearestCity(location, name,countrycode);
	if (nearestCity != null ){
		city = cityDao.getByFeatureId(nearestCity.getFeature_id());
			if (city==null){
				city = createNewCity();
			}
	} else {
		city = createNewCity();
	}
	//populate new fields
	//population
	if(city.getPopulation()==null && !isEmptyField(fields, 5, false)){
		try {
			int population = Integer.parseInt(fields[5].trim());
			city.setPopulation(population);
		} catch (NumberFormatException e) {
			logger.error("can not parse population :"+fields[5]);
		}
	}
	//countrycode
	if(!isEmptyField(fields, 4, false) && (city.getZipCodes()==null || !city.getZipCodes().contains(new ZipCode(fields[4])))){
			city.addZipCode(new ZipCode(fields[4]));
	}
	//shape
	if(!isEmptyField(fields, 7, false)){
		try {
			Geometry shape = (Point) GeolocHelper.convertFromHEXEWKBToGeometry(fields[7]);
			city.setShape(shape);
		    } catch (RuntimeException e) {
		    	logger.warn("can not parse shape for "+fields[7]+" : "+e);
		    }
	}
	city.setMunicipality(true);
	//adm
	if(!isEmptyField(fields, 9, false)){
		String admname =fields[9];
		SolrResponseDto solrResponseDto= getAdm(admname,countrycode);
		if (solrResponseDto!=null){
			Adm adm = admDao.getByFeatureId(solrResponseDto.getFeature_id());
			if (adm!=null){
				city.setAdm(adm);
			}
		}
	}
	try {
		cityDao.save(city);
	} catch (ConstraintViolationException e) {
		logger.error("Can not save "+dumpFields(fields)+"(ConstraintViolationException) we continue anyway but you should consider this",e);
	}catch (Exception e) {
		logger.error("Can not save "+dumpFields(fields)+" we continue anyway but you should consider this",e);
	}

    }

	City createNewCity() {
		City city;
		city = new City();
		city.setFeatureId(idGenerator.getNextFeatureId());
		city.setSource(GISSource.OPENSTREETMAP);
		return city;
	}


	protected SolrResponseDto getNearestCity(Point location, String name,String countryCode) {
		if (location ==null || name==null || "".equals(name.trim())){
			return null;
		}
		FulltextQuery query = (FulltextQuery) new FulltextQuery(name).withPlaceTypes(Constants.ONLY_CITY_PLACETYPE).around(location).withoutSpellChecking().withPagination(Pagination.ONE_RESULT).withOutput(MINIMUM_OUTPUT_STYLE);
		if (countryCode != null){
			query.limitToCountryCode(countryCode);
		}
		FulltextResultsDto results = fullTextSearchEngine.executeQuery(query);
		if (results != null){
			for (SolrResponseDto solrResponseDto : results.getResults()) {
				return solrResponseDto;
			}
		}
		return null;
	}
	
	protected SolrResponseDto getAdm(String name, String countryCode) {
		if (name==null){
			return null;
		}
		FulltextQuery query = (FulltextQuery)new FulltextQuery(name).withAllWordsRequired(false).withoutSpellChecking().
				withPlaceTypes(ONLY_ADM_PLACETYPE).withOutput(MINIMUM_OUTPUT_STYLE).withPagination(Pagination.ONE_RESULT);
		if (countryCode != null){
			query.limitToCountryCode(countryCode);
		}
		FulltextResultsDto results = fullTextSearchEngine.executeQuery(query);
		if (results != null){
			for (SolrResponseDto solrResponseDto : results.getResults()) {
				return solrResponseDto;
			}
		}
		return null;
	}
    

	/* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#shouldBeSkiped()
     */
    @Override
    public boolean shouldBeSkipped() {
    	return !importerConfig.isOpenstreetmapImporterEnabled();
    }
    
   


    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#setCommitFlushMode()
     */
    @Override
    protected void setCommitFlushMode() {
    	this.cityDao.setFlushMode(FlushMode.COMMIT);
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#shouldIgnoreComments()
     */
    @Override
    protected boolean shouldIgnoreComments() {
    	return true;
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#shouldIgnoreFirstLine()
     */
    @Override
    protected boolean shouldIgnoreFirstLine() {
    	return false;
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.IGeonamesProcessor#rollback()
     */
    public List<NameValueDTO<Integer>> rollback() {
    	List<NameValueDTO<Integer>> deletedObjectInfo = new ArrayList<NameValueDTO<Integer>>();
    	logger.info("reseting openstreetmap cities...");
    	//TODO only cities that have source openstreetmap
    	    deletedObjectInfo
    		    .add(new NameValueDTO<Integer>(City.class.getSimpleName(), 0));
    	resetStatus();
    	return deletedObjectInfo;
    }
    
    
    @Required
    public void setOpenStreetMapDao(ICityDao cityDao) {
        this.cityDao = cityDao;
    }
    
    @Override
    //TODO test
    protected void tearDown() {
    	super.tearDown();
    	FullTextSearchEngine.disableLogging=false;
    	String savedMessage = this.statusMessage;
    	try {
    		this.statusMessage = internationalisationService.getString("import.fulltext.optimize");
    		solRSynchroniser.optimize();
    	} finally {
    	    // we restore message in case of error
    	    this.statusMessage = savedMessage;
    	}
    }
    
    
   

    @Required
    public void setSolRSynchroniser(ISolRSynchroniser solRSynchroniser) {
        this.solRSynchroniser = solRSynchroniser;
    }

    @Required
    public void setIdGenerator(IIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Required
    public void setCityDao(ICityDao cityDao) {
		this.cityDao = cityDao;
	}

    @Required
	public void setFullTextSearchEngine(IFullTextSearchEngine fullTextSearchEngine) {
		this.fullTextSearchEngine = fullTextSearchEngine;
	}

    @Required
	public void setAdmDao(IAdmDao admDao) {
		this.admDao = admDao;
	}
    
    

    
}
