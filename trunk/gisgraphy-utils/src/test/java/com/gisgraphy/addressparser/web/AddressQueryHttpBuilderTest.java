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
package com.gisgraphy.addressparser.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.gisgraphy.addressparser.Address;
import com.gisgraphy.addressparser.AddressQuery;
import com.gisgraphy.addressparser.StructuredAddressQuery;
import com.gisgraphy.addressparser.exception.AddressParserException;
import com.gisgraphy.serializer.common.OutputFormat;
import com.gisgraphy.servlet.AbstractAddressServlet;
import com.gisgraphy.servlet.GisgraphyServlet;


public class AddressQueryHttpBuilderTest {
	
	 @Test
	    public void buildFromRequestWithNullRequest(){
	    AddressQueryHttpBuilder builder = AddressQueryHttpBuilder.getInstance();
	    try {
			builder.buildAddressFromRequest(null);
			Assert.fail("could not build a query with null request");
		} catch (Exception e) {
		}
	 }
	 
	 
	 @Test
	    public void buildQueryFromRequestWithNullRequest(){
	    AddressQueryHttpBuilder builder = AddressQueryHttpBuilder.getInstance();
	    try {
			builder.buildFromRequest(null);
			Assert.fail("could not build a query with null request");
		} catch (Exception e) {
		}
	 }
    
    @Test
    public void buildFromRequest(){
    AddressQueryHttpBuilder builder = AddressQueryHttpBuilder.getInstance();
	MockHttpServletRequest request = new MockHttpServletRequest();
	AddressQuery query;
	//without parameters
	try {
		builder.buildFromRequest(request);
		Assert.fail("without parameters the builder should throws");
	} catch (AddressParserException e) {
		//ignore
	}
	//country
	//without country
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	query = builder.buildFromRequest(request);
    Assert.assertNull("country parameter is not required",query.getCountry());

	//with empty country
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, " ");
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	query = builder.buildFromRequest(request);
	Assert.assertNull("with empty country equals to space, the country should be considered as null",query.getCountry());
	
	//with empty country
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "");
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	builder.buildFromRequest(request);
	Assert.assertNull("with empty country, the country should be considered as null",query.getCountry());
	
	//address
	//without address nor structured
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "US");
	try {
		builder.buildFromRequest(request);
		Assert.fail("without address parameter or structured the builder should throws");
	} catch (AddressParserException e) {
		//ignore
	}
	//with empty address, nor structured
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, " ");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	try {
		builder.buildFromRequest(request);
		Assert.fail("with empty country equals to space, the builder should throws");
	} catch (AddressParserException e) {
		//ignore
	}
	
	//with empty country
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, " ");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	try {
		builder.buildFromRequest(request);
		Assert.fail("with empty string the builder should throws");
	} catch (AddressParserException e) {
		//ignore
	}
	
	//with too long address
	request = new MockHttpServletRequest();
	String tooLongAddress = RandomStringUtils.random(AbstractAddressServlet.QUERY_MAX_LENGTH+1);
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, tooLongAddress);
	try {
		builder.buildFromRequest(request);
		Assert.fail("with empty string the builder should throws");
	} catch (AddressParserException e) {
		//ignore
	}
	
	//all ok
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	query = builder.buildFromRequest(request);
	Assert.assertEquals("address", query.getAddress());
	Assert.assertEquals("us", query.getCountry());
	
	// test outputFormat
	// with no value specified
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	query = builder.buildFromRequest(request);
	assertEquals("When no " + AbstractAddressServlet.OUTPUT_FORMAT_PARAMETER
		+ " is specified, the  parameter should be set to  "
		+ OutputFormat.getDefault(), OutputFormat.getDefault(), query
		.getFormat());
	// with wrong value
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.OUTPUT_FORMAT_PARAMETER, "UNK");
	query =builder.buildFromRequest(request);
	assertEquals("When wrong " + AbstractAddressServlet.OUTPUT_FORMAT_PARAMETER
		+ " is specified, the  parameter should be set to  "
		+ OutputFormat.getDefault(), OutputFormat.getDefault(), query
		.getFormat());
	// test case sensitive
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.OUTPUT_FORMAT_PARAMETER, "json");
	query =builder.buildFromRequest(request);
	assertEquals(AbstractAddressServlet.OUTPUT_FORMAT_PARAMETER
		+ " should be case insensitive  ", OutputFormat.JSON, query
		.getFormat());
	
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.OUTPUT_FORMAT_PARAMETER, "unsupported");
	query =builder.buildFromRequest(request);
    assertEquals(GisgraphyServlet.FORMAT_PARAMETER
	    + " should set default if not supported  ", OutputFormat.getDefault(), query
	    .getFormat());
    

    
    //test indent
    // with no value specified
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	query =builder.buildFromRequest(request);
    assertEquals("When no " + AbstractAddressServlet.INDENT_PARAMETER
	    + " is specified, the  parameter should be set to default indentation",AddressQuery.DEFAULT_INDENTATION
	    ,query.isIndent());
    // with wrong value
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.INDENT_PARAMETER, "unk");
	query =builder.buildFromRequest(request);
    assertEquals("When wrong " + AbstractAddressServlet.INDENT_PARAMETER
	    + " is specified, the  parameter should be set to default value",AddressQuery.DEFAULT_INDENTATION,
	    query.isIndent());
    // test case sensitive
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.INDENT_PARAMETER, "TrUe");
	query =builder.buildFromRequest(request);
    assertTrue(AbstractAddressServlet.INDENT_PARAMETER
	    + " should be case insensitive  ", query.isIndent());
    // test 'on' value
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.INDENT_PARAMETER, "On");
	query =builder.buildFromRequest(request);
    assertTrue(
    		AbstractAddressServlet.INDENT_PARAMETER
		    + " should be true for 'on' value (case insensitive and on value)  ",
	    query.isIndent());
    
    //test postal
    // with no value specified
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	query =builder.buildFromRequest(request);
    assertFalse("When no " + AbstractAddressServlet.POSTAL_PARAMETER
	    + " is specified, the  parameter should be set to false",query.isPostal());
    // with wrong value
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.POSTAL_PARAMETER, "unk");
	query =builder.buildFromRequest(request);
    assertFalse("When wrong " + AbstractAddressServlet.POSTAL_PARAMETER
	    + " is specified, the  parameter should be set to false", query.isPostal());
    // test case sensitive
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.POSTAL_PARAMETER, "TrUe");
	query =builder.buildFromRequest(request);
    assertTrue(AbstractAddressServlet.POSTAL_PARAMETER
	    + " should be case insensitive  ", query.isPostal());
    // test 'on' value
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.POSTAL_PARAMETER, "On");
	query =builder.buildFromRequest(request);
    assertTrue(
    		AbstractAddressServlet.POSTAL_PARAMETER
		    + " should be true for 'on' value (case insensitive and on value)  ",
	    query.isPostal());
    
    
    //test standardize
    // with no value specified
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	query =builder.buildFromRequest(request);
    assertFalse("When no " + AbstractAddressServlet.STANDARDIZE_PARAMETER
	    + " is specified, the  parameter should be set to false",query.isStandardize());
    // with wrong value
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.STANDARDIZE_PARAMETER, "unk");
	query =builder.buildFromRequest(request);
    assertFalse("When wrong " + AbstractAddressServlet.STANDARDIZE_PARAMETER
	    + " is specified, the  parameter should be set to false", query.isStandardize());
    // test case sensitive
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.STANDARDIZE_PARAMETER, "TrUe");
	query =builder.buildFromRequest(request);
    assertTrue(AbstractAddressServlet.STANDARDIZE_PARAMETER
	    + " should be case insensitive  ", query.isStandardize());
    // test 'on' value
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.STANDARDIZE_PARAMETER, "On");
	query =builder.buildFromRequest(request);
    assertTrue(
    		AbstractAddressServlet.STANDARDIZE_PARAMETER
		    + " should be true for 'on' value (case insensitive and on value)  ",
	    query.isStandardize());
    
    //callback not set
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	query =builder.buildFromRequest(request);
    assertNull("callback should be null when not set",
	     query.getCallback());
    
    //callback set with non alpha value
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.CALLBACK_PARAMETER, "doit(");
	query =builder.buildFromRequest(request);
    assertNull("callback should not be set when not alphanumeric",
	     query.getCallback());
    
    //callback set with alpha value
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.CALLBACK_PARAMETER, "doit");
	query =builder.buildFromRequest(request);
    assertEquals("callback should not be set when alphanumeric",
	     "doit",query.getCallback());
    
    //callback set with alpha value
    request = new MockHttpServletRequest();
    request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.APIKEY_PARAMETER, "apiKEY");
	query =builder.buildFromRequest(request);
    assertEquals("api key Should Be set",
	     "apiKEY",query.getApikey());
	
    }

    
    @Test
    public void buildStructuredFromRequest(){
    AddressQueryHttpBuilder builder = AddressQueryHttpBuilder.getInstance();
	MockHttpServletRequest request = new MockHttpServletRequest();
	AddressQuery query;
	//without parameters
	try {
		builder.buildFromRequest(request);
		Assert.fail("without parameters the builder should throws");
	} catch (AddressParserException e) {
		//ignore
	}
	//country
	//without country
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	query = builder.buildFromRequest(request);
	Assert.assertNull("country parameter is not required",query.getCountry());
	Assert.assertEquals("address", query.getAddress());

	//with empty country
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, " ");
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	query = builder.buildFromRequest(request);
	Assert.assertNull("with empty country equals to space, the builder should set countrycode to null",query.getCountry());
	Assert.assertEquals("address", query.getAddress());
	
	//with empty country
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "");
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	query = builder.buildFromRequest(request);
	Assert.assertNull("with empty country , the builder should set countrycode to null",query.getCountry());
	Assert.assertEquals("address", query.getAddress());
	
	//address
	//without address
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "US");
	try {
		builder.buildFromRequest(request);
		Assert.fail("without address parameter or structured address the builder should throws");
	} catch (AddressParserException e) {
		//ignore
	}
	//with empty address
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, " ");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	try {
		builder.buildFromRequest(request);
		Assert.fail("with empty country equals to space, the builder should throws");
	} catch (AddressParserException e) {
		//ignore
	}
	
	//with empty country
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	try {
		builder.buildFromRequest(request);
		Assert.fail("with empty string the builder should throws");
	} catch (AddressParserException e) {
		//ignore
	}
	
	
	//all ok
	request = new MockHttpServletRequest();
	String city = "city";
	String houseNumber = "1";
	String streetName = "california street";
	request.setParameter(city, city);
	request.setParameter("houseNumber", houseNumber);
	request.setParameter("streetName", streetName);
	request.setParameter("NotExistingFieldName", "foo");
	request.setParameter("country", "france");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	StructuredAddressQuery structuredquery = (StructuredAddressQuery) builder.buildFromRequest(request);
	Address address = structuredquery.getStructuredAddress();
	Assert.assertEquals("us", structuredquery.getCountry());
	Assert.assertEquals(city, address.getCity());
	Assert.assertEquals(houseNumber, address.getHouseNumber());
	Assert.assertEquals(streetName, address.getStreetName());
	Assert.assertNull(address.getCountry());
	
	// test outputFormat
	// with no value specified
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	query = builder.buildFromRequest(request);
	assertEquals("When no " + AbstractAddressServlet.OUTPUT_FORMAT_PARAMETER
		+ " is specified, the  parameter should be set to  "
		+ OutputFormat.getDefault(), OutputFormat.getDefault(), query
		.getFormat());
	// with wrong value
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.OUTPUT_FORMAT_PARAMETER, "UNK");
	query =builder.buildFromRequest(request);
	assertEquals("When wrong " + AbstractAddressServlet.OUTPUT_FORMAT_PARAMETER
		+ " is specified, the  parameter should be set to  "
		+ OutputFormat.getDefault(), OutputFormat.getDefault(), query
		.getFormat());
	// test case sensitive
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.OUTPUT_FORMAT_PARAMETER, "json");
	query =builder.buildFromRequest(request);
	assertEquals(AbstractAddressServlet.OUTPUT_FORMAT_PARAMETER
		+ " should be case insensitive  ", OutputFormat.JSON, query
		.getFormat());
	
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.OUTPUT_FORMAT_PARAMETER, "unsupported");
	query =builder.buildFromRequest(request);
    assertEquals(GisgraphyServlet.FORMAT_PARAMETER
	    + " should set default if not supported  ", OutputFormat.getDefault(), query
	    .getFormat());
    

    
    //test indent
    // with no value specified
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	query =builder.buildFromRequest(request);
    assertEquals("When no " + AbstractAddressServlet.INDENT_PARAMETER
	    + " is specified, the  parameter should be set to default indentation",AddressQuery.DEFAULT_INDENTATION
	    ,query.isIndent());
    // with wrong value
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.INDENT_PARAMETER, "unk");
	query =builder.buildFromRequest(request);
    assertEquals("When wrong " + AbstractAddressServlet.INDENT_PARAMETER
	    + " is specified, the  parameter should be set to default value",AddressQuery.DEFAULT_INDENTATION,
	    query.isIndent());
    // test case sensitive
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.INDENT_PARAMETER, "TrUe");
	query =builder.buildFromRequest(request);
    assertTrue(AbstractAddressServlet.INDENT_PARAMETER
	    + " should be case insensitive  ", query.isIndent());
    // test 'on' value
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.INDENT_PARAMETER, "On");
	query =builder.buildFromRequest(request);
    assertTrue(
    		AbstractAddressServlet.INDENT_PARAMETER
		    + " should be true for 'on' value (case insensitive and on value)  ",
	    query.isIndent());
    
    //test postal
    // with no value specified
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	query =builder.buildFromRequest(request);
    assertFalse("When no " + AbstractAddressServlet.POSTAL_PARAMETER
	    + " is specified, the  parameter should be set to false",query.isPostal());
    // with wrong value
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.POSTAL_PARAMETER, "unk");
	query =builder.buildFromRequest(request);
    assertFalse("When wrong " + AbstractAddressServlet.POSTAL_PARAMETER
	    + " is specified, the  parameter should be set to false", query.isPostal());
    // test case sensitive
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.POSTAL_PARAMETER, "TrUe");
	query =builder.buildFromRequest(request);
    assertTrue(AbstractAddressServlet.POSTAL_PARAMETER
	    + " should be case insensitive  ", query.isPostal());
    // test 'on' value
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.POSTAL_PARAMETER, "On");
	query =builder.buildFromRequest(request);
    assertTrue(
    		AbstractAddressServlet.POSTAL_PARAMETER
		    + " should be true for 'on' value (case insensitive and on value)  ",
	    query.isPostal());
    
    
    //test standardize
    // with no value specified
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	query =builder.buildFromRequest(request);
    assertFalse("When no " + AbstractAddressServlet.STANDARDIZE_PARAMETER
	    + " is specified, the  parameter should be set to false",query.isStandardize());
    // with wrong value
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.STANDARDIZE_PARAMETER, "unk");
	query =builder.buildFromRequest(request);
    assertFalse("When wrong " + AbstractAddressServlet.STANDARDIZE_PARAMETER
	    + " is specified, the  parameter should be set to false", query.isStandardize());
    // test case sensitive
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.STANDARDIZE_PARAMETER, "TrUe");
	query =builder.buildFromRequest(request);
    assertTrue(AbstractAddressServlet.STANDARDIZE_PARAMETER
	    + " should be case insensitive  ", query.isStandardize());
    // test 'on' value
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.STANDARDIZE_PARAMETER, "On");
	query =builder.buildFromRequest(request);
    assertTrue(
    		AbstractAddressServlet.STANDARDIZE_PARAMETER
		    + " should be true for 'on' value (case insensitive and on value)  ",
	    query.isStandardize());
    
    //callback not set
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	query =builder.buildFromRequest(request);
    assertNull("callback should be null when not set",
	     query.getCallback());
    
    //callback set with non alpha value
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.CALLBACK_PARAMETER, "doit(");
	query =builder.buildFromRequest(request);
    assertNull("callback should not be set when not alphanumeric",
	     query.getCallback());
    
    //callback set with alpha value
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressServlet.CALLBACK_PARAMETER, "doit");
	query =builder.buildFromRequest(request);
    assertEquals("callback should not be set when alphanumeric",
	     "doit",query.getCallback());
	
    }
    
    
    @Test
    public void buildAddressFromRequest(){
    	AddressQueryHttpBuilder builder = AddressQueryHttpBuilder.getInstance();
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	String city = "city";
    	String houseNumber = "1";
    	String streetName = "california street";
		request.setParameter(city, city);
		request.setParameter("houseNumber", houseNumber);
		request.setParameter("streetName", streetName);
		request.setParameter("NotExistingFieldName", "foo");
		request.setParameter("country", "france");
    	Address address = builder.buildAddressFromRequest(request);
    	Assert.assertEquals(city, address.getCity());
    	Assert.assertEquals(houseNumber, address.getHouseNumber());
    	Assert.assertEquals(streetName, address.getStreetName());
    	Assert.assertNull(address.getCountry());
    }
    
    @Test
    public void buildAddressFromRequest_WithoutParameters(){
    	AddressQueryHttpBuilder builder = AddressQueryHttpBuilder.getInstance();
    	MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("NotExistingFieldName", "foo");
    	Address address = builder.buildAddressFromRequest(request);
    	Assert.assertNull(address);
    }
    
    @Test
    public void buildAddressFromRequest_WithOnlyCountryParameters(){
    	AddressQueryHttpBuilder builder = AddressQueryHttpBuilder.getInstance();
    	MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("country", "foo");
    	Address address = builder.buildAddressFromRequest(request);
    	Assert.assertNull("country parameter is not really an address field, it is often the country parameter to geocode",address);
    }
    
    
    @Test
    public void getFieldNameFromParameter(){
    	AddressQueryHttpBuilder builder = AddressQueryHttpBuilder.getInstance();
    	Assert.assertEquals("streetName",builder.getFieldNameFromParameter("streetName"));
    	Assert.assertEquals("streetName",builder.getFieldNameFromParameter("streetname"));
    	Assert.assertEquals("streetName",builder.getFieldNameFromParameter("STREETNAME"));
    	Assert.assertNull(builder.getFieldNameFromParameter(null));
    }

}
