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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.FlushMode;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.geoloc.entity.AlternateName;
import com.gisgraphy.domain.geoloc.entity.AlternateOsmName;
import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.geoloc.entity.ZipCode;
import com.gisgraphy.domain.repository.ICityDao;
import com.gisgraphy.domain.repository.IIdGenerator;
import com.gisgraphy.domain.repository.IOpenStreetMapDao;
import com.gisgraphy.domain.repository.ISolRSynchroniser;
import com.gisgraphy.domain.valueobject.AlternateNameSource;
import com.gisgraphy.domain.valueobject.GisFeatureDistance;
import com.gisgraphy.domain.valueobject.NameValueDTO;
import com.gisgraphy.fulltext.FullTextSearchEngine;
import com.gisgraphy.geocoloc.IGeolocSearchEngine;
import com.gisgraphy.geoloc.GeolocQuery;
import com.gisgraphy.geoloc.GeolocResultsDto;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.helper.StringHelper;
import com.gisgraphy.street.StreetType;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * Import the street from an (pre-processed) openStreet map data file .
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class OpenStreetMapSimpleImporter extends AbstractSimpleImporterProcessor {
	
    @Autowired
    protected IIdGenerator idGenerator;
    
    @Autowired
    protected IOpenStreetMapDao openStreetMapDao;
    
    @Autowired
    protected ISolRSynchroniser solRSynchroniser;
    
    @Autowired
    protected IGeolocSearchEngine geolocSearchEngine;
    
    @Autowired
    protected IMunicipalityDetector municipalityDetector;
    
    @Autowired
    protected ICityDao cityDao;
    
    private static final Pattern pattern = Pattern.compile("(\\w+)\\s\\d+.*",Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    
 public static final String ALTERNATENAMES_EXTRACTION_REGEXP = "((?:(?!___).)+)(?:(?:___)|(?:$))";
    
    public static final Pattern ALTERNATENAMES_EXTRACTION_PATTERN = Pattern.compile(ALTERNATENAMES_EXTRACTION_REGEXP);
    

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#flushAndClear()
     */
    @Override
    protected void flushAndClear() {
	openStreetMapDao.flushAndClear();

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
	return ImporterHelper.listCountryFilesToImport(importerConfig.getOpenStreetMapDir());
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#getNumberOfColumns()
     */
    @Override
    protected int getNumberOfColumns() {
	return 10;
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#processData(java.lang.String)
     */
    @Override
    protected void processData(String line) throws ImporterException {
	String[] fields = line.split("\t");

	//
	// Line table has the following fields :
	// --------------------------------------------------- 
	//0: id; 1 name; 2 location; 3 length ;4 countrycode; 5 : gid ;
	//6 type; 7 oneway; 8 : shape;
	//
	checkNumberOfColumn(fields);
	OpenStreetMap street = new OpenStreetMap();
	
	// set id
	if (!isEmptyField(fields, 0, false)) {
	    Long openstreetmapId= null;
	    try {
		openstreetmapId = new Long(fields[0].trim());
	    } catch (NumberFormatException e) {
		logger.warn("can not get openstreetmap id for "+fields[0]);
	    }
	    street.setOpenstreetmapId(openstreetmapId);
	}
	
	// set name
	if (!isEmptyField(fields, 1, false)) {
	    street.setName(fields[1].trim());
	    StringHelper.updateOpenStreetMapEntityForIndexation(street);
	}
	if (!isEmptyField(fields, 2, false)) {
	    try {
		Point location = (Point) GeolocHelper.convertFromHEXEWKBToGeometry(fields[2]);
		street.setLocation(location);
	    } catch (RuntimeException e) {
	    	logger.warn("can not parse location for "+fields[1]+" : "+e);
	    	return;
	    }
	}
	
	if (!isEmptyField(fields, 3, false)) {
	    street.setLength(new Double(fields[3].trim()));
	}
	
	if (!isEmptyField(fields, 4, false)) {
	    street.setCountryCode(fields[4].trim());
	}
		if (!isEmptyField(fields, 5, false)) {
			street.setIsIn(fields[5].trim());
		} else if (shouldFillIsInField()) {
			setIsInFields(street);
		}
	
		long generatedId= idGenerator.getNextGId();
		street.setGid(new Long(generatedId));
	
	if (!isEmptyField(fields, 6, false)) {
	    StreetType type;
	    try {
		type = StreetType.valueOf(fields[6].toUpperCase());
		street.setStreetType(type);
	    } catch (Exception e) {
		logger.warn("can not determine streetType for "+fields[1]+" : "+e);
		street.setStreetType(StreetType.UNCLASSIFIED);
	    }
	    
	}
	
	if (!isEmptyField(fields, 7, false)) {
	    boolean oneWay = false;
	    try {
		oneWay  = Boolean.valueOf(fields[7]);
		street.setOneWay(oneWay);
	    } catch (Exception e) {
		logger.warn("can not determine oneway for "+fields[1]+" : "+e);
	    }
	    
	}
	
	if (!isEmptyField(fields, 8, true)) {
	    try {
		street.setShape((LineString)GeolocHelper.convertFromHEXEWKBToGeometry(fields[8]));
	    } catch (RuntimeException e) {
		logger.warn("can not parse shape for "+fields[8] +" : "+e);
		return;
	    }
	    
	}
	
	if (fields.length == 10 && !isEmptyField(fields, 8, false)){
		populateAlternateNames(street,fields[9]);
	}
		
	try {
		openStreetMapDao.save(street);
	} catch (ConstraintViolationException e) {
		logger.error("Can not save "+dumpFields(fields)+"(ConstraintViolationException) we continue anyway but you should consider this",e);
	}catch (Exception e) {
		logger.error("Can not save "+dumpFields(fields)+" we continue anyway but you should consider this",e);
	}

    }
    
    OpenStreetMap populateAlternateNames(OpenStreetMap street,
			String alternateNamesAsString) {
		if (street ==null || alternateNamesAsString ==null){
			return street;
		}
		Matcher matcher = ALTERNATENAMES_EXTRACTION_PATTERN.matcher(alternateNamesAsString);
		int i = 0;
		while (matcher.find()){
			if (matcher.groupCount() != 1) {
				logger.warn("wrong number of fields for street alternatename no " + i + "for line " + alternateNamesAsString);
				continue;
			}
			String alternateName = matcher.group(1);
			if (alternateName!= null && !"".equals(alternateName.trim())){
				if (street.getName()==null){
					street.setName(alternateName);
				} else {
				 street.addAlternateName((new AlternateOsmName(alternateName.trim(),AlternateNameSource.OPENSTREETMAP)));
				}
			}
		}
		return street;
		
	}

    protected void setIsInFields(OpenStreetMap street) {
    	if (street != null && street.getLocation() != null) {
    		//first searchByShape because it is the more reliable :
    		City cityByShape = cityDao.getByShape(street.getLocation(),true);
    		if (cityByShape != null){
    			street.setIsIn(cityByShape.getName());
    			street.setCityConfident(true);
    			street.setPopulation(cityByShape.getPopulation());
    			if (cityByShape.getZipCodes() != null) {
    				for (ZipCode zip:cityByShape.getZipCodes()){
    					street.addZip(zip.getCode());
    				}
    			}
    			if (cityByShape.getAlternateNames()!=null){
    				for (AlternateName name : cityByShape.getAlternateNames() ){
    					if (name!=null && name.getName()!=null){
    						street.addIsInCitiesAlternateName(name.getName());
    					}
    				}
    			}
    			if (cityByShape.getAdm()!=null){
    				street.setIsInAdm(cityByShape.getAdm().getName());
    			}
    			return;
    		}
    		GisFeatureDistance city = getNearestCity(street.getLocation(), true);
    		if (city != null) {
    			street.setPopulation(city.getPopulation());
    			street.setIsInAdm(getDeeperAdmName(city));
    			if (city.getZipCodes() != null) {
    				for (String zip:city.getZipCodes()){
    					street.addZip(zip);
    				}
    			}
    			if (city.getName() != null) {
    				street.setIsIn(pplxToPPL(city.getName()));
    			}
    			City cityFromDb = cityDao.getByFeatureId(city.getFeatureId());
    			if (cityFromDb!= null && cityFromDb.getAlternateNames()!=null){
    				for (AlternateName name : cityFromDb.getAlternateNames() ){
    					if (name!=null && name.getName()!=null){
    						street.addIsInCitiesAlternateName(name.getName());
    					}
    				}
    			}
    		}
    		GisFeatureDistance city2 = getNearestCity(street.getLocation(), false);
    		if (city2 != null) {
    			if (city != null){
    					if (city.getFeatureId() == city2.getFeatureId()) {
    						return;
    					}
    					if (city2.getDistance()>city.getDistance()){
    						return;
    					}
    			}
    				//we got a non municipality that is nearest, we set isinPlace tag and update is_in if needed
    				if (city2.getPopulation() != null && city2.getPopulation() != 0 && (street.getPopulation() == null || street.getPopulation() == 0)) {
    					street.setPopulation(city2.getPopulation());
    				}

    				if (street.getIsIn() == null) {
    					street.setIsIn(pplxToPPL(city2.getName()));
    				} else {
    					street.setIsInPlace(pplxToPPL(city2.getName()));
    				}
    				if (street.getIsInAdm() == null) {
    					street.setIsInAdm(getDeeperAdmName(city2));
    				}
    				if (street.getIsInZip() == null && city2.getZipCodes() != null ) {
    					for (String zip:city2.getZipCodes()){
        					street.addZip(zip);
        				}
    				}
    				if (city==null && city2!=null){//add AN only if there are not added yet
	    				City city2FromDb = cityDao.getByFeatureId(city2.getFeatureId());
	        			if (city2FromDb!= null && city2FromDb.getAlternateNames()!=null){
	        				for (AlternateName name : city2FromDb.getAlternateNames() ){
	        					if (name!=null && name.getName()!=null){
	        						street.addIsInCitiesAlternateName(name.getName());
	        					}
	        				}
	        			}
    				}
    		}
    	}
    }

	//todo test
	protected String getDeeperAdmName(GisFeatureDistance city) {
		if (city != null) {
			if (city.getAdm5Name() != null) {
				return city.getAdm5Name();
			} else if (city.getAdm4Name() != null) {
				return city.getAdm4Name();
			} else if (city.getAdm3Name() != null) {
				return city.getAdm3Name();
			} else if (city.getAdm2Name() != null) {
				return city.getAdm2Name();
			} else if (city.getAdm1Name() != null) {
				return city.getAdm1Name();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
    
   
	protected GisFeatureDistance getNearestCity(Point location, boolean filterMunicipality) {
		if (location ==null){
			return null;
		}
		GeolocQuery query = (GeolocQuery) new GeolocQuery(location).withDistanceField(true).withPlaceType(City.class).withMunicipalityFilter(filterMunicipality);
		GeolocResultsDto results = geolocSearchEngine.executeQuery(query);
		if (results != null){
			for (GisFeatureDistance gisFeatureDistance : results.getResult()) {
				return gisFeatureDistance;
			}
		}
		return null;
	}
    
    /**
     *  tests if city is a paris district, if so it is
		probably a pplx that is newly considered as ppl
		http://forum.geonames.org/gforum/posts/list/2063.page
     */
    protected String pplxToPPL(String cityName){
    	if (cityName!=null){
    		Matcher matcher = pattern.matcher(cityName);
    		if (matcher.find()) {
    			return matcher.group(1);
    		} else {
    			return cityName;
    		}
    	} else {
    		return cityName;
    	}
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
    	this.openStreetMapDao.setFlushMode(FlushMode.COMMIT);
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
    	logger.info("deleting openstreetmap entities...");
    	int deleted = openStreetMapDao.deleteAll();
    	if (deleted != 0) {
    	    deletedObjectInfo
    		    .add(new NameValueDTO<Integer>(openStreetMapDao.getPersistenceClass().getSimpleName(), deleted));
    	}
    	logger.info(deleted + " openstreetmap entities have been deleted");
    	resetStatus();
    	return deletedObjectInfo;
    }
    
    
    @Required
    public void setOpenStreetMapDao(IOpenStreetMapDao openStreetMapDao) {
        this.openStreetMapDao = openStreetMapDao;
    }
    
    @Override
    //TODO test
    protected void tearDown() {
    	super.tearDown();
    	FullTextSearchEngine.disableLogging=false;
    	String savedMessage = this.statusMessage;
    	try {
    		this.statusMessage = internationalisationService.getString("import.message.createIndex");
    		openStreetMapDao.createSpatialIndexes();
    	} catch (Exception e) {
    		logger.error("an error occured during spatial index creation, we ignore it but you have to manually run it to have good performances : "+e.getMessage(),e);
    	}
    	this.statusMessage = internationalisationService.getString("import.fulltext.optimize");
    	solRSynchroniser.optimize();
    }
    
    /**
     * overidded because alternatenames can be null so number of fields can differ
     * 
     * @see #getNumberOfColumns()
     * @param fields
     *                The array to check
     */
    @Override
    protected void checkNumberOfColumn(String[] fields) {
	if (fields.length != 9 && fields.length != 10) {

	    throw new WrongNumberOfFieldsException(
		    "The number of fields is not correct. expected : "
			    + getNumberOfColumns() + ", founds :  "
			    + fields.length+ ". details :"+dumpFields(fields));
	}
    }
    
    protected boolean shouldFillIsInField(){
    	return importerConfig.isGeonamesImporterEnabled() && importerConfig.isOpenStreetMapFillIsIn(); 
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
	public void setGeolocSearchEngine(IGeolocSearchEngine geolocSearchEngine) {
		this.geolocSearchEngine = geolocSearchEngine;
	}

    @Required
    public void setMunicipalityDetector(IMunicipalityDetector municipalityDetector) {
		this.municipalityDetector = municipalityDetector;
	}

    @Required
	public void setCityDao(ICityDao cityDao) {
		this.cityDao = cityDao;
	}
    
}
