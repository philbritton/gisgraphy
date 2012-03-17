/*******************************************************************************
 * Gisgraphy Project 
 *  
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *  
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *    Lesser General Public License for more details.
 *  
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA
 *  
 *   Copyright 2008  Gisgraphy project 
 * 
 *   David Masclet <davidmasclet@gisgraphy.com>
 ******************************************************************************/
package com.gisgraphy.street;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.geoloc.entity.Street;
import com.gisgraphy.helper.GeolocHelper;
import com.vividsolutions.jts.geom.Point;


public class StreetFactoryTest {

    @Test
    public void  createShouldCreate(){
	long gid = 12345L;
	long openstreetmapId = 5678L;
	String name= "california street"; 
	Double length = 5.6D;
	boolean oneWay = true;
	String isIn = "los angeles";
	StreetType streetType = StreetType.SECONDARY_LINK;
	String countryCode = "FR";
	Point location = GeolocHelper.createPoint(10.2F, 9.5F);
	OpenStreetMap openStreetMap = new OpenStreetMap();
	openStreetMap.setGid(gid);
	openStreetMap.setCountryCode(countryCode);
	openStreetMap.setName(name);
	openStreetMap.setLocation(location);
	openStreetMap.setLength(length);
	openStreetMap.setStreetType(streetType);
	openStreetMap.setOneWay(oneWay);
	openStreetMap.setOpenstreetmapId(openstreetmapId);
	openStreetMap.setIsIn(isIn);
	
	StreetFactory factory = new StreetFactory();
	Street street = factory.create(openStreetMap);
	
	Assert.assertEquals(new Long(gid), street.getFeatureId());
	Assert.assertEquals(new Long(openstreetmapId), street.getOpenstreetmapId());
	Assert.assertEquals(name, street.getName());
	Assert.assertEquals(location, street.getLocation());
	Assert.assertEquals(length, street.getLength());
	Assert.assertEquals(streetType, street.getStreetType());
	Assert.assertEquals(oneWay, street.isOneWay());
	Assert.assertEquals(countryCode, street.getCountryCode());
	Assert.assertEquals(isIn, street.getIsIn());
	
	
    }
    
}
