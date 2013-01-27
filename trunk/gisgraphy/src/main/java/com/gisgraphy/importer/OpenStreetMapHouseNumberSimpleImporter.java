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
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.repository.IIdGenerator;
import com.gisgraphy.domain.repository.IOpenStreetMapDao;
import com.gisgraphy.domain.repository.ISolRSynchroniser;
import com.gisgraphy.domain.repository.IhouseNumberDao;
import com.gisgraphy.domain.valueobject.GisFeatureDistance;
import com.gisgraphy.domain.valueobject.GisgraphyConfig;
import com.gisgraphy.domain.valueobject.NameValueDTO;
import com.gisgraphy.geocoloc.IGeolocSearchEngine;
import com.gisgraphy.geoloc.GeolocQuery;
import com.gisgraphy.geoloc.GeolocResultsDto;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.helper.StringHelper;
import com.gisgraphy.importer.dto.AssociatedStreetHouseNumber;
import com.gisgraphy.importer.dto.AssociatedStreetMember;
import com.gisgraphy.importer.dto.InterpolationHouseNumber;
import com.gisgraphy.importer.dto.NodeHouseNumber;
import com.gisgraphy.street.StreetType;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * Import the street from an (pre-processed) openStreet map data file .
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class OpenStreetMapHouseNumberSimpleImporter extends AbstractSimpleImporterProcessor {
	
    
    protected IIdGenerator idGenerator;
    
    protected IOpenStreetMapDao openStreetMapDao;
    
    protected IhouseNumberDao houseNumberDao;
    
    protected ISolRSynchroniser solRSynchroniser;
    
    protected IGeolocSearchEngine geolocSearchEngine;
    
    protected IcityDetector cityDetector;
    
    private static final String ASSOCIATED_HOUSE_NUMBER_REGEXP = "([0-9]+)___([^_]*)___((?:(?!___).)*)___((?:(?!___).)*)___([NW])___([^_;]*)(?:;?)";
    
    private static final String INTERPOLATION_HOUSE_NUMBER_REGEXP = "([0-9]+)___([0-9]+)___([^_]*)___((?:(?!___).)*)___([^_;]*)(?:;?)";
    
    private static final Pattern pattern = Pattern.compile(ASSOCIATED_HOUSE_NUMBER_REGEXP,Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    
    
    

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#flushAndClear()
     */
    @Override
    protected void flushAndClear() {
	openStreetMapDao.flushAndClear();
	houseNumberDao.flushAndClear();

    }
    
    @Override
    protected void setup() {
        super.setup();
    }
    

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#getFiles()
     */
    @Override
    protected File[] getFiles() {
	return ImporterHelper.listCountryFilesToImport(importerConfig.getOpenStreetMapHouseNumberDir());
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#getNumberOfColumns()
     */
    @Override
    protected int getNumberOfColumns() {
	return 9;
    }

    protected AssociatedStreetHouseNumber parseAssociatedStreetHouseNumber(String line){
    	/*A	1264114	
    	 * "{""47129758___0101000020E61000005CCBD3C231E76240AA6514FE5BF440C0___Bowral Street___Bowral Street___W___street"",
    	 * ""84623507___0101000020E6100000546690CC36E76240A417D5545AF440C0___71___Bowral Street___W___house""}"
    	 * */
    	if (line==null || "".equals(line.trim())){
    		return null;
    	}
    	String[] fields = line.split("\t");
    	if (fields.length != 3){
    		logger.warn("wrong number of fields for line "+line+" expected 3 but was "+fields.length);
    		return null;
    	}
    	if (!"A".equals(fields[0])){
    		logger.warn("wrong house Number Type for line "+line+" expected 'A' but was "+fields[0]);
    		return null;
    	}
    	AssociatedStreetHouseNumber houseNumber = new AssociatedStreetHouseNumber();
    	if (!isEmptyField(fields, 1, false)) {
    		houseNumber.setRelationID(fields[1].trim());
    	}
    	if (!isEmptyField(fields, 2, false)) {
    		Matcher matcher = pattern.matcher(fields[2].trim());
    		int i = 0;
    		while (matcher.find()) {
    			AssociatedStreetMember member = new AssociatedStreetMember();
    			if (matcher.groupCount()!= 6){
    				logger.warn("wrong number of fields for AssociatedStreetMember n"+ i+"for line "+line);
    				continue;
    			}
    			member.setId(matcher.group(1));
    		    Point point = (Point) GeolocHelper.convertFromHEXEWKBToGeometry(matcher.group(2));
    		    if (point==null){
    		    	logger.warn("wrong number of fields for point n"+ i+"for line "+line);
    		    	continue;
    		    }
				member.setLocation(point);
    		    String role = matcher.group(6);
    		    member.setRole(role);
    		    member.setType(matcher.group(5));
    		    if ("street".equalsIgnoreCase(role)){
    		    	if (matcher.group(3)!=null){
    		    		member.setName(matcher.group(3));
    		    	}else {
    		    		member.setName(matcher.group(4));
    		    	}
    		    } else if ("house".equalsIgnoreCase(role)){
	    			member.setHouseNumber(matcher.group(3));
    		    }
    		    
    		    houseNumber.addMember(member);
    		    i++;
    		}
    		
    	} else {
    		return null;
    	}
    	return houseNumber;
    }
    
    protected InterpolationHouseNumber parseInterpolationHouseNumber(String line){
    	/*
    	 * I	168365171	1796478450___0___0101000020E61000009A023EE4525350C0959C137B682F38C0______;
    	 * 1366275082___1___0101000020E610000068661CD94B5350C0B055270C6F2F38C0______;
    	 * 1796453793___2___0101000020E610000038691A144D5350C023ADE75A6A2F38C0___600___;
    	 * 1796453794___3___0101000020E6100000F38F6390605350C028A6666A6D2F38C0___698___		even
    	 */
    	if (line==null || "".equals(line.trim())){
    		return null;
    	}
    	String[] fields = line.split("\t");
    	if (fields.length != 3){
    		logger.warn("wrong number of fields for line "+line+" expected 3 but was "+fields.length);
    		return null;
    	}
    	if (!"A".equals(fields[0])){
    		logger.warn("wrong house Number Type for line "+line+" expected 'A' but was "+fields[0]);
    		return null;
    	}
    	InterpolationHouseNumber houseNumber = new InterpolationHouseNumber();
    	if (!isEmptyField(fields, 1, false)) {
    		houseNumber.setWayID(fields[1].trim());
    	}
    	return null;
    }
    
    protected NodeHouseNumber parseNodeHouseNumber(String line){
    	return null;
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
		
	try {
		openStreetMapDao.save(street);
	} catch (ConstraintViolationException e) {
		logger.error("Can not save "+dumpFields(fields)+"(ConstraintViolationException) we continue anyway but you should consider this",e);
	}catch (Exception e) {
		logger.error("Can not save "+dumpFields(fields)+" we continue anyway but you should consider this",e);
	}

    }

	protected void setIsInFields(OpenStreetMap street) {
		if (street != null && street.getLocation() != null) {
			boolean filterMunicipality = cityDetector.isCountryHasMunicipality(street.getCountryCode());
			GisFeatureDistance city = getNearestCity(street.getLocation(), filterMunicipality);
			if (city != null) {
				street.setPopulation(city.getPopulation());
				street.setIsInAdm(getDeeperAdmName(city));
				if (city.getZipCodes() != null && city.getZipCodes().size() == 1) {
					street.setIsInZip(city.getZipCodes().get(0));
				}
				if (city.getName() != null) {
					street.setIsIn(pplxToPPL(city.getName()));
				}
			}
			if (filterMunicipality) {
				GisFeatureDistance city2 = getNearestCity(street.getLocation(), false);
				if (city2 != null) {
					if (city != null && city.getFeatureId() == city2.getFeatureId()) {
						return;
					}
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
					if (street.getIsInZip() == null && city2.getZipCodes() != null && city2.getZipCodes().size() == 1) {
						street.setIsInZip(city2.getZipCodes().get(0));
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
    	String savedMessage = this.statusMessage;
    	try {
    		this.statusMessage = internationalisationService.getString("import.message.createIndex");
    		openStreetMapDao.createSpatialIndexes();
    	} catch (Exception e) {
    		logger.error("an error occured during spatial index creation, we ignore it but you have to manually run it to have good performances : "+e.getMessage(),e);
    	}
    	this.statusMessage = internationalisationService.getString("import.fulltext.optimize");
    	solRSynchroniser.optimize();
    	try {
    	    if (GisgraphyConfig.PARTIAL_SEARH_EXPERIMENTAL) {
    		openStreetMapDao.clearPartialSearchName();
    	    }
    	} finally {
    	    // we restore message in case of error
    	    this.statusMessage = savedMessage;
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
    public void setCityDetector(IcityDetector cityDetector) {
		this.cityDetector = cityDetector;
	}
    
}
