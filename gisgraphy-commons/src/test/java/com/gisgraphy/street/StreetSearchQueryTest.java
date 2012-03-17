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
/**
 *
 */
package com.gisgraphy.street;

import static com.gisgraphy.domain.valueobject.Pagination.paginate;
import static com.gisgraphy.test.GisgraphyCommonsTestHelper.createPoint;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.TestEntity;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.geoloc.GeolocQuery;
import com.gisgraphy.serializer.common.OutputFormat;
import com.vividsolutions.jts.geom.Point;

public class StreetSearchQueryTest  {

    /**
     * a simple point to avoid creation of new one for each test
     */
    private static Point GENERIC_POINT = createPoint(3.2F, 2.5F);

    @Test
    public void streetSearchQueryPointRadiusPaginationOutputStreetType() {
	Pagination pagination = paginate().from(2).to(7);
	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
		"FR").withStyle(OutputStyle.FULL).withIndentation();
	StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT, 3D, pagination,
		output, StreetType.UNCLASSIFIED,true,"name",StreetSearchMode.CONTAINS);
	assertEquals(pagination, query.getPagination());
	assertEquals(output, query.getOutput());
	assertEquals(null, query.getPlaceType());
	assertEquals(GENERIC_POINT, query.getPoint());
	assertTrue(query.isOutputIndented());
	assertEquals(3D, query.getRadius(),0.01);
	assertEquals(StreetType.UNCLASSIFIED, query.getStreetType());
	assertEquals(Boolean.TRUE, query.getOneWay());
	assertEquals("name", query.getName());
	assertEquals(StreetSearchMode.CONTAINS, query.getStreetSearchMode());
    }
    
    @Test
    public void streetSearchModeShouldBeSetToDefaultIfNameIsNotNullAndStreetSearchModeIsNull(){
    	Pagination pagination = paginate().from(2).to(7);
    	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
    		"FR").withStyle(OutputStyle.FULL).withIndentation();
    	StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT, 3D, pagination,
    		output, StreetType.UNCLASSIFIED,true,"name",null);
    	assertEquals(StreetSearchMode.getDefault(), query.getStreetSearchMode());
    	
    	query = new StreetSearchQuery(GENERIC_POINT, 3D, pagination,
        		output, StreetType.UNCLASSIFIED,true,null,null);
    	assertNull("StreetSearchModeCanBeNull if name is null", query.getStreetSearchMode());
    	
    	
    	
    }

    @Test
    public void streetSearchQueryPointRadius() {
	    StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT, 3D,StreetType.UNCLASSIFIED);
	    assertEquals(Pagination.DEFAULT_PAGINATION, query.getPagination());
	    assertEquals(Output.DEFAULT_OUTPUT, query.getOutput());
	    assertEquals(GENERIC_POINT, query.getPoint());
	    assertEquals(3D, query.getRadius(),0.01);
	    assertEquals(StreetType.UNCLASSIFIED, query.getStreetType());
    }

    @Test
    public void streetSearchQueryPoint() {
	    StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT,StreetType.UNCLASSIFIED);
	    assertEquals(Pagination.DEFAULT_PAGINATION, query.getPagination());
	    assertEquals(Output.DEFAULT_OUTPUT, query.getOutput());
	    assertEquals(GENERIC_POINT, query.getPoint());
	    assertEquals(GeolocQuery.DEFAULT_RADIUS, query.getRadius(),0.01);
	    assertEquals(StreetType.UNCLASSIFIED, query.getStreetType());
    }

   

    @Test
    public void testStreetSearchQueryWithNullPointThrows() {
	Pagination pagination = paginate().from(2).to(7);
	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
		"FR").withStyle(OutputStyle.FULL);
	try {
	    new StreetSearchQuery(null, 3D, pagination, output, TestEntity.class);
	} catch (IllegalArgumentException e) {
	    fail("Query with null point should not throws");

	}

	try {
	    new StreetSearchQuery(null, 5D);
	} catch (RuntimeException e) {
	    fail("Query with null point should not throws");
	}
	
    }

    @Test
    public void testStreetSearchQueryWithWrongRadiusShouldBeSetWithDefault() {
	Pagination pagination = paginate().from(2).to(7);
	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
		"FR").withStyle(OutputStyle.FULL);
	// with negative value
	StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT, -1, pagination,
		output, StreetType.UNCLASSIFIED,true,"name",StreetSearchMode.getDefault());
	assertEquals(GeolocQuery.DEFAULT_RADIUS, query.getRadius(),0.01);

	// with 0
	 query = new StreetSearchQuery(GENERIC_POINT, 0, pagination,
		output, StreetType.UNCLASSIFIED,true,"name",StreetSearchMode.getDefault());
	assertEquals(GeolocQuery.DEFAULT_RADIUS, query.getRadius(),0.01);

    }

    // TODO test withradius

    @Test
    public void testStreetWithPaginationShouldBeSetToDefaultPaginationIfNull() {
	assertEquals(Pagination.DEFAULT_PAGINATION, new StreetSearchQuery(
		GENERIC_POINT,StreetType.UNCLASSIFIED).withPagination(null).getPagination());
    }

    @Test
    public void testWithPaginationShouldSetThePagination() {
	assertEquals(5, new StreetSearchQuery(GENERIC_POINT,StreetType.UNCLASSIFIED).withPagination(
		paginate().from(5).to(23)).getPagination().getFrom());
	assertEquals(23, new StreetSearchQuery(GENERIC_POINT,StreetType.UNCLASSIFIED).withPagination(
		paginate().from(5).to(23)).getPagination().getTo());
    }

    @Test
    public void testStreetWithOutputShouldBeSetToDefaultOutputIfNull() {
	assertEquals(Output.DEFAULT_OUTPUT, new GeolocQuery(GENERIC_POINT)
		.withOutput(null).getOutput());
    }

    @Test
    public void testWithOutputShouldSetTheOutput() {
	StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT,StreetType.UNCLASSIFIED);
	Pagination pagination = paginate().from(2).to(7);
	query.withPagination(pagination);
	assertEquals(pagination, query.getPagination());
    }

    @Test
    public void testWithStreetTypeShouldBeSetToNullIfNull() {
	assertNull(new StreetSearchQuery(GENERIC_POINT,StreetType.UNCLASSIFIED).withStreetType(null)
		.getStreetType());
    }

    @Test
    public void testWithStreetTypeShouldSetTheStreetType() {
	StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT,StreetType.UNCLASSIFIED);
	query.withStreetType(StreetType.CONSTRUCTION);
	assertEquals(StreetType.CONSTRUCTION, query.getStreetType());
    }

    @Test
    public void testWithOneWayShouldSetTheOneWay() {
	StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT,StreetType.UNCLASSIFIED);
	assertNull("Default value of oneway should be null", query.getOneWay());
	query.withOneWay(true);
	assertEquals(Boolean.TRUE, query.getOneWay());
    }
    
    @Test
    public void testWithStreetSearchModeShouldSetTheStreetSearchMode() {
	StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT,StreetType.UNCLASSIFIED);
	assertEquals("Default value of Street searchMode should be the StreetSearchMode.getDefault() one",StreetSearchMode.getDefault(), query.getStreetSearchMode());
	query.withStreetSearchMode(StreetSearchMode.CONTAINS);
	assertEquals(StreetSearchMode.CONTAINS, query.getStreetSearchMode());
    }
    
    @Test
        public void testWithName() {
    	//good Value
    	StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT,StreetType.UNCLASSIFIED);
    	query.withName("nameValue");
    	assertEquals("nameValue", query.getName());
    	
    	//too long String
    	try {
    	    query = new StreetSearchQuery(GENERIC_POINT,StreetType.UNCLASSIFIED);
    	    query.withName(RandomStringUtils
    	    .random(StreetSearchQuery.NAME_MAX_LENGTH) + 1);
    	    fail("Name Prefix must have a maximmum length of "
    		    + StreetSearchQuery.NAME_MAX_LENGTH);
    	} catch (StreetSearchException e) {
    	}
    	
    	//Empty String
    	query = new StreetSearchQuery(GENERIC_POINT,StreetType.UNCLASSIFIED);
    	query.withName(" ");
    	assertNull("Name ShouldNot Be considered for Empty String", query.getName());
    	
    	
        }
    
    
    @Test
    public void testCallbackOk() {
	StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT);
	String callback ="doit";
	query.withCallback(callback);
	assertEquals(callback,query.getCallback() );
    }
    @Test
    public void testCallbackKOBecauseNonAlphanumeric() {
	StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT);
	String callback ="doit(";
	query.withCallback(callback);
	assertNull("callback should be alphanumeric",query.getCallback() );
    }
    
    @Test
    public void testCallbackKOBecauseNnull() {
	StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT);
	query.withCallback(null);
	assertNull("callback should not be null",query.getCallback() );
    }

   @Test
   public void testToString(){
       StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT,StreetType.UNCLASSIFIED);
       assertFalse("ToString should not contains GeolocQuery and should be overide",query.toString().contains("GeolocQuery"));
   }
   
   @Test
   public void testToStringWithNullPoint(){
       StreetSearchQuery query = new StreetSearchQuery("toto");
       query.toString();
   }

}
