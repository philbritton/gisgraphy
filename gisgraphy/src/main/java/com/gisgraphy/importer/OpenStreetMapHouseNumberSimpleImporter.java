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
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.geoloc.entity.HouseNumber;
import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.repository.IIdGenerator;
import com.gisgraphy.domain.repository.IOpenStreetMapDao;
import com.gisgraphy.domain.repository.ISolRSynchroniser;
import com.gisgraphy.domain.repository.IhouseNumberDao;
import com.gisgraphy.domain.valueobject.GisgraphyConfig;
import com.gisgraphy.domain.valueobject.HouseNumberType;
import com.gisgraphy.domain.valueobject.NameValueDTO;
import com.gisgraphy.geocoloc.IGeolocSearchEngine;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.importer.dto.AddressInclusion;
import com.gisgraphy.importer.dto.AssociatedStreetHouseNumber;
import com.gisgraphy.importer.dto.AssociatedStreetMember;
import com.gisgraphy.importer.dto.InterpolationHouseNumber;
import com.gisgraphy.importer.dto.InterpolationMember;
import com.gisgraphy.importer.dto.InterpolationType;
import com.gisgraphy.importer.dto.NodeHouseNumber;
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

	private static final String ASSOCIATED_HOUSE_NUMBER_REGEXP = "([0-9]+)___([^_]*)___((?:(?!___).)*)___((?:(?!___).)*)___([NW])___([^_]*)(?:___)?";

	private static final String INTERPOLATION_HOUSE_NUMBER_REGEXP = "([0-9]+)___([0-9])___((?:(?!___).)+)*___((?:(?!___).)+)*___((?:(?!___).)+)*(?:___)?";

	private static final Pattern ASSOCIATED_HOUSE_NUMBER_PATTERN = Pattern.compile(ASSOCIATED_HOUSE_NUMBER_REGEXP, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

	private static final Pattern INTERPOLATION_HOUSE_NUMBER_PATTERN = Pattern.compile(INTERPOLATION_HOUSE_NUMBER_REGEXP, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#flushAndClear
	 * ()
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#getFiles()
	 */
	@Override
	protected File[] getFiles() {
		return ImporterHelper.listCountryFilesToImport(importerConfig.getOpenStreetMapHouseNumberDir());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#
	 * getNumberOfColumns()
	 */
	@Override
	protected int getNumberOfColumns() {
		return 9;
	}

	protected AssociatedStreetHouseNumber parseAssociatedStreetHouseNumber(String line) {
		/*
		 * A 1264114 "{"
		 * "47129758___0101000020E61000005CCBD3C231E76240AA6514FE5BF440C0___Bowral Street___Bowral Street___W___street"
		 * ", ""84623507
		 * ___0101000020E6100000546690CC36E76240A417D5545AF440C0___71___Bowral
		 * Street___W___house""}"
		 */
		if (line == null || "".equals(line.trim())) {
			return null;
		}
		String[] fields = line.split("\t");
		if (fields.length != 3) {
			logger.warn("wrong number of fields for line " + line + " expected 3 but was " + fields.length);
			return null;
		}
		if (!"A".equals(fields[0])) {
			logger.warn("wrong house Number Type for line " + line + " expected 'A' but was " + fields[0]);
			return null;
		}
		AssociatedStreetHouseNumber houseNumber = new AssociatedStreetHouseNumber();
		if (!isEmptyField(fields, 1, false)) {
			houseNumber.setRelationID(fields[1].trim());
		}
		if (!isEmptyField(fields, 2, false)) {
			Matcher matcher = ASSOCIATED_HOUSE_NUMBER_PATTERN.matcher(fields[2].trim());
			int i = 0;
			while (matcher.find()) {
				AssociatedStreetMember member = new AssociatedStreetMember();
				if (matcher.groupCount() != 6) {
					logger.warn("wrong number of fields for AssociatedStreetMember n" + i + "for line " + line);
					continue;
				}
				member.setId(matcher.group(1));
				Point point = (Point) GeolocHelper.convertFromHEXEWKBToGeometry(matcher.group(2));
				if (point == null) {
					logger.warn("wrong location for AssociatedStreetMember for point n" + i + "for line " + line);
					continue;
				}
				member.setLocation(point);
				String role = matcher.group(6);
				member.setRole(role);
				member.setHouseNumber(matcher.group(3));
				member.setStreetName(matcher.group(4));
				member.setType(matcher.group(5));

				houseNumber.addMember(member);
				i++;
			}

		} else {
			return null;
		}
		return houseNumber;
	}

	protected InterpolationHouseNumber parseInterpolationHouseNumber(String line) {
		/*
		 * I	168365171	1796478450___0___0101000020E61000009A023EE4525350C0959C137B682F38C0______;
		 * 1366275082___1___0101000020E610000068661CD94B5350C0B055270C6F2F38C0______;
		 * 1796453793___2___0101000020E610000038691A144D5350C023ADE75A6A2F38C0___600___;
		 * 1796453794___3___0101000020E6100000F38F6390605350C028A6666A6D2F38C0___698___		even
		 */
		if (line == null || "".equals(line.trim())) {
			return null;
		}
		String[] fields = line.split("\t");
		if (fields.length < 5 || fields.length > 6) {
			logger.warn("wrong number of fields for line " + line + " expected 5/6 but was " + fields.length);
			return null;
		}
		if (!"I".equals(fields[0])) {
			logger.warn("wrong house Number Type for line " + line + " expected 'A' but was " + fields[0]);
			return null;
		}
		InterpolationHouseNumber houseNumber = new InterpolationHouseNumber();
		if (!isEmptyField(fields, 1, false)) {
			houseNumber.setWayId(fields[1].trim());
		}
		if (!isEmptyField(fields, 4, false)) {
			
			try {
				houseNumber.setInterpolationType(InterpolationType.valueOf(fields[4].trim().toLowerCase()));
			} catch (Exception e) {
				//ignore
			}
		}
		if (!isEmptyField(fields, 3, false)) {
			houseNumber.setStreetName(fields[3].trim());
		}
		if (!isEmptyField(fields, 5, false)) {
			
			try {
				houseNumber.setAddressInclusion(AddressInclusion.valueOf(fields[5].trim().toLowerCase()));
			} catch (Exception e) {
				//ignore
			}
		}

		if (!isEmptyField(fields, 2, false)) {
			Matcher matcher = INTERPOLATION_HOUSE_NUMBER_PATTERN.matcher(fields[2].trim());
			int i = 0;
			while (matcher.find()) {
				InterpolationMember member = new InterpolationMember();
				if (matcher.groupCount() != 5) {
					logger.warn("wrong number of fields for InterpolationMember n" + i + "for line " + line);
					continue;
				}
				member.setId(matcher.group(1));
				// seqId
				String seqIdAsString = matcher.group(2);
				int seqId = 0;
				try {
					seqId = Integer.parseInt(seqIdAsString);
				} catch (NumberFormatException e) {
					continue;
				}
				member.setSequenceId(seqId);
				// location
				Point point = (Point) GeolocHelper.convertFromHEXEWKBToGeometry(matcher.group(3));
				if (point == null) {
					logger.warn("wrong location for InterpolationMember point n" + i + "for line " + line);
					continue;
				}
				member.setLocation(point);

				member.setHouseNumber(matcher.group(4));
				member.setStreetname(matcher.group(5));
				houseNumber.addMember(member);
				i++;
			}
			Collections.sort(houseNumber.getMembers());
		} else {
			return null;
		}
		
		return houseNumber;
	}

	protected NodeHouseNumber parseNodeHouseNumber(String line) {
		//N	1053493828	0101000020E610000060910486D17250C05D4B6D4ECA753CC0	75	Sandwichs La Estrellita	Estanislao Maldones
		if (line == null || "".equals(line.trim())) {
			return null;
		}
		String[] fields = line.split("\t");
		if (fields.length < 4 ) {
			logger.warn("wrong number of fields for line " + line + " expected 4 but was " + fields.length);
			return null;
		}
		if (!"N".equals(fields[0])) {
			logger.warn("wrong house Number Type for line " + line + " expected 'N' but was " + fields[0]);
			return null;
		}
		NodeHouseNumber node = new NodeHouseNumber();
		if (!isEmptyField(fields, 1, false)) {
			node.setNodeId(fields[1].trim());
		}
		if (!isEmptyField(fields, 2, false)) {
			Point point = (Point) GeolocHelper.convertFromHEXEWKBToGeometry(fields[2].trim());
			if (point == null) {
				logger.warn("wrong location for NodeHouseNumber for point for line " + line);
			} else {
				node.setLocation(point);
			}
		}
		if (!isEmptyField(fields, 3, false)) {
			node.setHouseNumber(fields[3].trim());
		}
		if (!isEmptyField(fields, 4, false)) {
			node.setName(fields[4].trim());
		}
		if (!isEmptyField(fields, 5, false)) {
			node.setStreetName(fields[5].trim());
		}
		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#processData
	 * (java.lang.String)
	 */
	@Override
	protected void processData(String line) throws ImporterException {
		if (line==null || "".equals(line.trim())){
			return;
		}
		
		if (line.startsWith("A")){
			AssociatedStreetHouseNumber house = parseAssociatedStreetHouseNumber(line);
			if (house==null){
				return;
			}
			List<AssociatedStreetMember> streetMembers = house.getStreetMembers();
			List<AssociatedStreetMember> houseMembers = house.getHouseMembers();
			if (houseMembers.size()==0 ){
				//no streets or no house
				return;
			} 
			if (streetMembers.size()==0){
				//TODO treet as node
			}
			if (streetMembers.size()==1){
				AssociatedStreetMember associatedStreetMember = streetMembers.get(0);
				if (associatedStreetMember.getId()==null){
					logger.warn("associated street "+associatedStreetMember+" has no id");
					return;
				}
				Long idAsLong = null;
				try {
					idAsLong = Long.valueOf(associatedStreetMember.getId());
				} catch (NumberFormatException e) {
					logger.warn(idAsLong+" is not a valid id for associated street");
					return;
				}
				OpenStreetMap associatedStreet = openStreetMapDao.getByOpenStreetMapId(idAsLong);
				if (associatedStreet==null){
					logger.warn("no street can be found for associated street "+associatedStreetMember);
					return;
				}
				for (AssociatedStreetMember houseMember : houseMembers){
					HouseNumber houseNumber = buildHouseNumberFromAssociatedHouseNumber(houseMember);
					associatedStreet.addHouseNumber(houseNumber);
				}
				openStreetMapDao.save(associatedStreet);
			}
			if (streetMembers.size()>1){
				//for each house, search the nearest street
				//getStreetIds
				List<Long>  streetIds = new ArrayList<Long>();
				for (AssociatedStreetMember street : streetMembers){
					Long id;
					try {
						id = Long.valueOf(street.getId());
						streetIds.add(id);
					} catch (NumberFormatException e) {
						logger.warn(street+" has no id");
					}
				}
				for (AssociatedStreetMember houseMember : houseMembers){
					if (houseMember!=null && houseMember.getLocation()!=null){
					HouseNumber houseNumber = buildHouseNumberFromAssociatedHouseNumber(houseMember);
					OpenStreetMap associatedStreet = openStreetMapDao.getNearestByosmIds(houseMember.getLocation(), streetIds);
					if (associatedStreet!=null){
						associatedStreet.addHouseNumber(houseNumber);
						openStreetMapDao.save(associatedStreet);
					}
					}
				}
				
			}
		} else if (line.startsWith("N")){
			NodeHouseNumber house = parseNodeHouseNumber(line);
		} else if (line.startsWith("I")){
			InterpolationHouseNumber house = parseInterpolationHouseNumber(line);
		} else {
			logger.warn("unknow node type for line "+line);
		}

	}

	protected HouseNumber buildHouseNumberFromAssociatedHouseNumber(
			AssociatedStreetMember houseMember) {
		HouseNumber houseNumber = new HouseNumber();
		Integer numberAsInteger = null;
		try {
			numberAsInteger = Integer.valueOf(houseMember.getHouseNumber());
			houseNumber.setNumber(numberAsInteger);
			//TODO if not a number maybe a  separeated by -?
		} catch (NumberFormatException e) {
			logger.warn(numberAsInteger+" is not a valid numeric house number");
			if (houseMember.getHouseNumber()!=null && !"".equals(houseMember.getHouseNumber().trim())){
					houseNumber.setName(houseMember.getHouseNumber());//housenumber of the member is probably a housename
			}
		}
		houseNumber.setLocation(houseMember.getLocation());
		Long osmId = null;
		try {
			osmId = Long.valueOf(houseMember.getId());
			houseNumber.setOpenstreetmapId(osmId);
		} catch (NumberFormatException e) {
			logger.warn(osmId+" is not a valid openstreetmapId");
		}
		houseNumber.setType(HouseNumberType.ASSOCIATED);
		return houseNumber;
	}
	
		
	protected List<Point> segmentize(List<Point> points,int nbInnerPoint){
		List<Point> result = new ArrayList<Point>();
		if (points==null || nbInnerPoint==0 || points.size()==0){
			return result;
		}
		if (points.size()>=1){
			result.add(points.get(0));
			
		}
		if (points.size()==1){
			return result;
		}
		List<Double> distances = new ArrayList<Double>();//5/10/15
		double totalDistance = 0;//30
		for (int i=0;i<points.size()-1;i++){
			double distance = GeolocHelper.distance(points.get(i), points.get(i+1));
			distances.add(distance);
			totalDistance+=distance;
		}
		int nbSegment = nbInnerPoint+1;//4+1
		int nbpoints = nbInnerPoint+1;//4+1
		double segmentLength = totalDistance/nbSegment;//30/5=6
		int currentSubSegment =1;
		double currentSubLength=0;
		for (int i=1;i<=nbpoints;i++){
			while(currentSubLength+distances.get(currentSubSegment-1) <i*segmentLength){
				currentSubLength+=distances.get(currentSubSegment-1);
				currentSubSegment++;
				if (currentSubSegment>distances.size()){
					return result;
				}
			}
			double distanceLeft = (i*segmentLength)-currentSubLength;
			double ratio = distanceLeft/(distances.get(currentSubSegment-1));
			if (ratio <= 0){
				//it is the last point
				result.add(points.get(points.size()-1));
			} else {
				//we put it at the correct ratio
				Point p1=points.get(currentSubSegment-1);
				Point p2=points.get(currentSubSegment);
				Double lng = (p2.getX()-p1.getX())*ratio+p1.getX();
				Double lat = (p2.getY()-p1.getY())*ratio+p1.getY();
				Point p = GeolocHelper.createPoint(lng.floatValue(), lat.floatValue());
				result.add(p);
			}
		}
		return result;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#shouldBeSkiped
	 * ()
	 */
	@Override
	public boolean shouldBeSkipped() {
		return !importerConfig.isOpenstreetmapHouseNumberImporterEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#
	 * setCommitFlushMode()
	 */
	@Override
	protected void setCommitFlushMode() {
		this.openStreetMapDao.setFlushMode(FlushMode.COMMIT);
		this.houseNumberDao.setFlushMode(FlushMode.COMMIT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#
	 * shouldIgnoreComments()
	 */
	@Override
	protected boolean shouldIgnoreComments() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#
	 * shouldIgnoreFirstLine()
	 */
	@Override
	protected boolean shouldIgnoreFirstLine() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gisgraphy.domain.geoloc.importer.IGeonamesProcessor#rollback()
	 */
	public List<NameValueDTO<Integer>> rollback() {
		List<NameValueDTO<Integer>> deletedObjectInfo = new ArrayList<NameValueDTO<Integer>>();
		logger.info("reseting  house number importer...");
		int deleted = houseNumberDao.deleteAll();
		if (deleted != 0) {
			deletedObjectInfo.add(new NameValueDTO<Integer>(houseNumberDao.getPersistenceClass().getSimpleName(), deleted));
		}
		logger.info(deleted + " house number entities have been deleted");
		resetStatus();
		return deletedObjectInfo;
	}


	@Override
	// TODO test
	protected void tearDown() {
		super.tearDown();
		String savedMessage = this.statusMessage;
		try {
			this.statusMessage = internationalisationService.getString("import.message.createIndex");
			openStreetMapDao.createSpatialIndexes();
		} catch (Exception e) {
			logger.error("an error occured during spatial index creation, we ignore it but you have to manually run it to have good performances : " + e.getMessage(), e);
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


	@Required
	public void setHouseNumberDao(IhouseNumberDao houseNumberDao) {
		this.houseNumberDao = houseNumberDao;
	}
	
	@Required
	public void setOpenStreetMapDao(IOpenStreetMapDao openStreetMapDao) {
		this.openStreetMapDao = openStreetMapDao;
	}


}
