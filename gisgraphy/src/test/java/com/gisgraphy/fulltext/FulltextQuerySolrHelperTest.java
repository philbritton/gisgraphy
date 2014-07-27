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
package com.gisgraphy.fulltext;

import static com.gisgraphy.domain.valueobject.Pagination.paginate;
import static com.gisgraphy.fulltext.FulltextQuerySolrHelper.NESTED_QUERY_NOT_ALL_WORDS_REQUIRED_TEMPLATE;
import static com.gisgraphy.fulltext.FulltextQuerySolrHelper.NESTED_QUERY_NUMERIC_TEMPLATE;
import static com.gisgraphy.fulltext.FulltextQuerySolrHelper.NESTED_QUERY_TEMPLATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Locale;

import net.sf.jstester.util.Assert;

import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.Country;
import com.gisgraphy.domain.geoloc.entity.Street;
import com.gisgraphy.domain.valueobject.Constants;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.fulltext.spell.SpellCheckerConfig;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.serializer.common.OutputFormat;
import com.gisgraphy.test.GisgraphyTestHelper;


public class FulltextQuerySolrHelperTest {
	 private  OutputStyleHelper outputStyleHelper = new OutputStyleHelper();
	 
	 
	 @Test
	    public void testToQueryStringShouldreturnCorrectParamsForSuggestQuery() {
		Country france = GisgraphyTestHelper.createCountryForFrance();
		Pagination pagination = paginate().from(3).to(10);
		Output output = Output.withFormat(OutputFormat.XML).withLanguageCode(
			"FR").withStyle(OutputStyle.SHORT).withIndentation();
		String searchTerm = "Saint-André";
		FulltextQuery fulltextQuery = new FulltextQuery(searchTerm,
			pagination, output, null, null).withAllWordsRequired(true).withSuggest(true).withSpellChecking();
		// split parameters
		HashMap<String, String> parameters = GisgraphyTestHelper.splitURLParams(
			FulltextQuerySolrHelper.toQueryString(fulltextQuery), "&");
		// check parameters
		assertNull("field list parameter are by default, we use the one in the suggest request handler", parameters
			.get(Constants.FL_PARAMETER));
		assertEquals("wrong indent parameter found", "on", parameters
			.get(Constants.INDENT_PARAMETER));
		assertEquals("wrong echoparams parameter found", "none", parameters
			.get(Constants.ECHOPARAMS_PARAMETER));
		assertEquals("wrong start parameter found", "2", parameters
			.get(Constants.START_PARAMETER));
		assertEquals("wrong rows parameter found", "8", parameters
			.get(Constants.ROWS_PARAMETER));
		assertEquals("wrong output format parameter found", OutputFormat.JSON
			.getParameterValue(), parameters
			.get(Constants.OUTPUT_FORMAT_PARAMETER));
		assertEquals("wrong query type parameter found",
			Constants.SolrQueryType.suggest.toString(), parameters
				.get(Constants.QT_PARAMETER));
		assertTrue("wrong query parameter found",parameters
				.get(Constants.QUERY_PARAMETER).equals(searchTerm));
		assertNull("spellchecker query should not be set",parameters
			.get(Constants.SPELLCHECKER_QUERY_PARAMETER));   
		}
	 
    @Test
    public void testToQueryStringShouldreturnCorrectParamsForBasicQuery() {
	Country france = GisgraphyTestHelper.createCountryForFrance();
	Pagination pagination = paginate().from(3).to(10);
	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
		"FR").withStyle(OutputStyle.SHORT).withIndentation();
	String searchTerm = "Saint-André";
	FulltextQuery fulltextQuery = new FulltextQuery(searchTerm,
		pagination, output, null, null).withAllWordsRequired(true);
	// split parameters
	HashMap<String, String> parameters = GisgraphyTestHelper.splitURLParams(
		FulltextQuerySolrHelper.toQueryString(fulltextQuery), "&");
	// check parameters
	assertEquals(outputStyleHelper.getFulltextFieldList(Output.OutputStyle.SHORT,"FR"), parameters
		.get(Constants.FL_PARAMETER));
	assertEquals("wrong indent parameter found", "on", parameters
		.get(Constants.INDENT_PARAMETER));
	assertEquals("wrong echoparams parameter found", "none", parameters
		.get(Constants.ECHOPARAMS_PARAMETER));
	assertEquals("wrong start parameter found", "2", parameters
		.get(Constants.START_PARAMETER));
	assertEquals("wrong rows parameter found", "8", parameters
		.get(Constants.ROWS_PARAMETER));
	assertEquals("wrong output format parameter found", OutputFormat.JSON
		.getParameterValue(), parameters
		.get(Constants.OUTPUT_FORMAT_PARAMETER));
	assertEquals("wrong query type parameter found",
		Constants.SolrQueryType.advanced.toString(), parameters
			.get(Constants.QT_PARAMETER));
	assertTrue("wrong query parameter found, we want to boost score even if it is a simple query",parameters
			.get(Constants.QUERY_PARAMETER).contains("dismax"));
	assertNull("spellchecker query should not be set when standard query",parameters
		.get(Constants.SPELLCHECKER_QUERY_PARAMETER));   
	}
    
    
    
    
    @Test
    public void testToQueryStringShouldreturnCorrectParamsForBasicNumericQuery() {
	Pagination pagination = paginate().from(3).to(10);
	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
		"FR").withStyle(OutputStyle.SHORT).withIndentation();
	String searchTerm = "1001";
	FulltextQuery fulltextQuery = new FulltextQuery(searchTerm,
		pagination, output, null, null).withAllWordsRequired(true);
	// split parameters
	HashMap<String, String> parameters = GisgraphyTestHelper.splitURLParams(
		FulltextQuerySolrHelper.toQueryString(fulltextQuery), "&");
	// check parameters
	assertEquals(outputStyleHelper.getFulltextFieldList(Output.OutputStyle.SHORT,"FR"), parameters
		.get(Constants.FL_PARAMETER));
	assertEquals("wrong indent parameter found", "on", parameters
		.get(Constants.INDENT_PARAMETER));
	assertEquals("wrong echoparams parameter found", "none", parameters
		.get(Constants.ECHOPARAMS_PARAMETER));
	assertEquals("wrong start parameter found", "2", parameters
		.get(Constants.START_PARAMETER));
	assertEquals("wrong rows parameter found", "8", parameters
		.get(Constants.ROWS_PARAMETER));
	assertEquals("wrong output format parameter found", OutputFormat.JSON
		.getParameterValue(), parameters
		.get(Constants.OUTPUT_FORMAT_PARAMETER));
	assertEquals("wrong query type parameter found",
		Constants.SolrQueryType.numeric.toString(), parameters
			.get(Constants.QT_PARAMETER));
	assertEquals("wrong query parameter found ",searchTerm,
		parameters
			.get(Constants.QUERY_PARAMETER));
	assertEquals("wrong query parameter",searchTerm,
	parameters
		.get(Constants.QUERY_PARAMETER));
	
	assertNull("spellchecker query should not be set when basic numeric query",parameters
		.get(Constants.SPELLCHECKER_QUERY_PARAMETER));
	}
    
    
    
    
    @Test
    public void testToQueryStringShouldreturnCorrectParamsForAdvancedNumericQuery() {
	Pagination pagination = paginate().from(3).to(10);
	    Output output = Output.withFormat(OutputFormat.JSON)
		    .withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		    .withIndentation();
	    FulltextQuery fulltextQuery = new FulltextQuery("1001",
		    pagination, output, com.gisgraphy.fulltext.Constants.ONLY_CITY_PLACETYPE, "FR");
	// split parameters
	HashMap<String, String> parameters = GisgraphyTestHelper.splitURLParams(
		FulltextQuerySolrHelper.toQueryString(fulltextQuery), "&");
	// check parameters
	assertEquals(outputStyleHelper.getFulltextFieldList(Output.OutputStyle.SHORT,"FR"), parameters
		.get(Constants.FL_PARAMETER));
	assertEquals("wrong indent parameter found", "on", parameters
		.get(Constants.INDENT_PARAMETER));
	assertEquals("wrong echoparams parameter found", "none", parameters
		.get(Constants.ECHOPARAMS_PARAMETER));
	assertEquals("wrong start parameter found", "2", parameters
		.get(Constants.START_PARAMETER));
	assertEquals("wrong rows parameter found", "8", parameters
		.get(Constants.ROWS_PARAMETER));
	assertEquals("wrong output format parameter found", OutputFormat.JSON
		.getParameterValue(), parameters
		.get(Constants.OUTPUT_FORMAT_PARAMETER));
	assertEquals("wrong query type parameter found",
		Constants.SolrQueryType.advanced.toString(), parameters
			.get(Constants.QT_PARAMETER));
	assertTrue("wrong query parameter found '"+FullTextFields.PLACETYPE.getValue()+":' is expected in query but was "+parameters
			.get(Constants.QUERY_PARAMETER),
		parameters
			.get(Constants.QUERY_PARAMETER).contains(FullTextFields.PLACETYPE.getValue()+":"));


	assertTrue("wrong nested parameter found, actual : "+parameters
		.get(Constants.QUERY_PARAMETER),
	parameters
		.get(Constants.QUERY_PARAMETER).contains(String.format(NESTED_QUERY_NUMERIC_TEMPLATE, "1001")));
	
	
	assertTrue("wrong query parameter found '"+FullTextFields.COUNTRYCODE.getValue()+":' is expected in query but was "+parameters
			.get(Constants.QUERY_PARAMETER),
			parameters
				.get(Constants.QUERY_PARAMETER).contains(FullTextFields.COUNTRYCODE.getValue()+":"));
	assertNull("spellchecker query should not be set when advanced numeric query",parameters
		.get(Constants.SPELLCHECKER_QUERY_PARAMETER)); 
	}
    
    @Test
    public void testToQueryStringShouldreturnCorrectParamsWhenAllRequiredIsFalse() {
	Pagination pagination = paginate().from(1).to(10);
	    Output output = Output.withFormat(OutputFormat.JSON)
		    .withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		    .withIndentation();
	    FulltextQuery fulltextQuery = new FulltextQuery("foo",
		    pagination, output, com.gisgraphy.fulltext.Constants.ONLY_CITY_PLACETYPE, "FR").withAllWordsRequired(false);
	// split parameters
	HashMap<String, String> parameters = GisgraphyTestHelper.splitURLParams(
		FulltextQuerySolrHelper.toQueryString(fulltextQuery), "&");
	// check parameters
	assertEquals(outputStyleHelper.getFulltextFieldList(Output.OutputStyle.SHORT,"FR"), parameters
		.get(Constants.FL_PARAMETER));
	assertEquals("wrong indent parameter found", "on", parameters
		.get(Constants.INDENT_PARAMETER));
	assertEquals("wrong echoparams parameter found", "none", parameters
		.get(Constants.ECHOPARAMS_PARAMETER));
	assertEquals("wrong start parameter found", "0", parameters
		.get(Constants.START_PARAMETER));
	assertEquals("wrong rows parameter found", "10", parameters
		.get(Constants.ROWS_PARAMETER));
	assertEquals("wrong output format parameter found", OutputFormat.JSON
		.getParameterValue(), parameters
		.get(Constants.OUTPUT_FORMAT_PARAMETER));
	assertEquals("wrong query type parameter found",
		Constants.SolrQueryType.advanced.toString(), parameters
			.get(Constants.QT_PARAMETER));
	assertTrue("wrong query parameter found '"+FullTextFields.PLACETYPE.getValue()+":' is expected in query but was "+parameters
			.get(Constants.QUERY_PARAMETER),
		parameters
			.get(Constants.QUERY_PARAMETER).contains(FullTextFields.PLACETYPE.getValue()+":"));


	assertTrue("wrong nested parameter found, actual : "+parameters
		.get(Constants.QUERY_PARAMETER),
	parameters
		.get(Constants.QUERY_PARAMETER).contains(String.format(NESTED_QUERY_NOT_ALL_WORDS_REQUIRED_TEMPLATE, "","","foo")));
	
	
	assertTrue("wrong query parameter found '"+FullTextFields.COUNTRYCODE.getValue()+":' is expected in query but was "+parameters
			.get(Constants.QUERY_PARAMETER),
			parameters
				.get(Constants.QUERY_PARAMETER).contains(FullTextFields.COUNTRYCODE.getValue()+":"));
	assertNotNull("spellchecker query should be set ",parameters
		.get(Constants.SPELLCHECKER_QUERY_PARAMETER));    }


    
    @Test
    public void testToQueryStringShouldreturnCorrectParamsForAdvancedNonNumeric_allwordNotRequired() {
	Pagination pagination = paginate().from(3).to(10);
	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
		"FR").withStyle(OutputStyle.SHORT).withIndentation();
	String searchTerm = "Saint-André";
	FulltextQuery fulltextQuery = new FulltextQuery(searchTerm,
		pagination, output, com.gisgraphy.fulltext.Constants.ONLY_ADM_PLACETYPE, "fr").withAllWordsRequired(false);
	// split parameters
	HashMap<String, String> parameters = GisgraphyTestHelper.splitURLParams(
		FulltextQuerySolrHelper.toQueryString(fulltextQuery), "&");
	// check parameters
	assertEquals(outputStyleHelper.getFulltextFieldList(Output.OutputStyle.SHORT,"FR"), parameters
		.get(Constants.FL_PARAMETER));
	assertEquals("wrong indent parameter found", "on", parameters
		.get(Constants.INDENT_PARAMETER));
	assertEquals("wrong echoparams parameter found", "none", parameters
		.get(Constants.ECHOPARAMS_PARAMETER));
	assertEquals("wrong start parameter found", "2", parameters
		.get(Constants.START_PARAMETER));
	assertEquals("wrong rows parameter found", "8", parameters
		.get(Constants.ROWS_PARAMETER));
	assertEquals("wrong output format parameter found", OutputFormat.JSON
		.getParameterValue(), parameters
		.get(Constants.OUTPUT_FORMAT_PARAMETER));
	assertEquals("wrong query type parameter found",
		Constants.SolrQueryType.advanced.toString(), parameters
			.get(Constants.QT_PARAMETER));
	assertTrue("wrong nested parameter found actual : "+parameters
		.get(Constants.QUERY_PARAMETER),
		parameters
			.get(Constants.QUERY_PARAMETER).contains(String.format(NESTED_QUERY_NOT_ALL_WORDS_REQUIRED_TEMPLATE, "","",searchTerm)));
	assertTrue("wrong query parameter found '"+FullTextFields.PLACETYPE.getValue()+":' expected in query but was "+parameters
			.get(Constants.QUERY_PARAMETER),
		parameters
			.get(Constants.QUERY_PARAMETER).contains(FullTextFields.PLACETYPE.getValue()+":"));
	assertTrue("wrong query parameter found '"+FullTextFields.COUNTRYCODE.getValue()+":' expected in query but was "+parameters
			.get(Constants.QUERY_PARAMETER),
			parameters
				.get(Constants.QUERY_PARAMETER).contains(FullTextFields.COUNTRYCODE.getValue()+":"));
	assertNotNull("spellchecker query should be set when numeric query",parameters
		.get(Constants.SPELLCHECKER_QUERY_PARAMETER));  
    }
    
    @Test
    public void testToQueryStringShouldreturnCorrectParamsForAdvancedNonNumeric_allwordsRequired() {
	Pagination pagination = paginate().from(3).to(10);
	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
		"FR").withStyle(OutputStyle.SHORT).withIndentation();
	String searchTerm = "Saint-André";
	FulltextQuery fulltextQuery = new FulltextQuery(searchTerm,
		pagination, output, com.gisgraphy.fulltext.Constants.ONLY_ADM_PLACETYPE, "fr").withAllWordsRequired(true);
	// split parameters
	HashMap<String, String> parameters = GisgraphyTestHelper.splitURLParams(
		FulltextQuerySolrHelper.toQueryString(fulltextQuery), "&");
	// check parameters
	assertEquals(outputStyleHelper.getFulltextFieldList(Output.OutputStyle.SHORT,"FR"), parameters
		.get(Constants.FL_PARAMETER));
	assertEquals("wrong indent parameter found", "on", parameters
		.get(Constants.INDENT_PARAMETER));
	assertEquals("wrong echoparams parameter found", "none", parameters
		.get(Constants.ECHOPARAMS_PARAMETER));
	assertEquals("wrong start parameter found", "2", parameters
		.get(Constants.START_PARAMETER));
	assertEquals("wrong rows parameter found", "8", parameters
		.get(Constants.ROWS_PARAMETER));
	assertEquals("wrong output format parameter found", OutputFormat.JSON
		.getParameterValue(), parameters
		.get(Constants.OUTPUT_FORMAT_PARAMETER));
	assertEquals("wrong query type parameter found",
		Constants.SolrQueryType.advanced.toString(), parameters
			.get(Constants.QT_PARAMETER));
	assertTrue("wrong nested parameter found actual : "+parameters
		.get(Constants.QUERY_PARAMETER),
		parameters
			.get(Constants.QUERY_PARAMETER).contains(String.format(NESTED_QUERY_TEMPLATE, "","",searchTerm)));
	assertTrue("wrong query parameter found '"+FullTextFields.PLACETYPE.getValue()+":' expected in query but was "+parameters
			.get(Constants.QUERY_PARAMETER),
		parameters
			.get(Constants.QUERY_PARAMETER).contains(FullTextFields.PLACETYPE.getValue()+":"));
	assertTrue("wrong query parameter found '"+FullTextFields.COUNTRYCODE.getValue()+":' expected in query but was "+parameters
			.get(Constants.QUERY_PARAMETER),
			parameters
				.get(Constants.QUERY_PARAMETER).contains(FullTextFields.COUNTRYCODE.getValue()+":"));
	assertNotNull("spellchecker query should be set when numeric query",parameters
		.get(Constants.SPELLCHECKER_QUERY_PARAMETER));  
    }

    @Test
    public void testToQueryStringShouldreturnCorrectParamsForGeoRSS() {
	Pagination pagination = paginate().from(3).to(10);
	Output output = Output.withFormat(OutputFormat.GEORSS)
		.withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		.withIndentation();
	FulltextQuery fulltextQuery = new FulltextQuery("Saint-André",
		pagination, output, com.gisgraphy.fulltext.Constants.ONLY_ADM_PLACETYPE, "fr");
	// split parameters
	HashMap<String, String> parameters = GisgraphyTestHelper.splitURLParams(
			FulltextQuerySolrHelper.toQueryString(fulltextQuery), "&");
	// check parameters
	assertEquals("wrong field list", outputStyleHelper.getFulltextFieldList(Output.OutputStyle.MEDIUM,"FR"), parameters.get(Constants.FL_PARAMETER));
	assertEquals("wrong indent parameter found", "on", parameters
		.get(Constants.INDENT_PARAMETER));
	assertEquals("wrong echoparams parameter found", "none", parameters
		.get(Constants.ECHOPARAMS_PARAMETER));
	assertEquals("wrong start parameter found", "2", parameters
		.get(Constants.START_PARAMETER));
	assertEquals("wrong rows parameter found", "8", parameters
		.get(Constants.ROWS_PARAMETER));
	assertEquals("wrong output format parameter found", OutputFormat.GEORSS
		.getParameterValue(), parameters
		.get(Constants.OUTPUT_FORMAT_PARAMETER));
	assertEquals("wrong stylesheet", Constants.GEORSS_STYLESHEET,
		parameters.get(Constants.STYLESHEET_PARAMETER));
	assertEquals("wrong query type parameter found",
		Constants.SolrQueryType.advanced.toString(), parameters
			.get(Constants.QT_PARAMETER));
	assertTrue("wrong query parameter found '"+FullTextFields.PLACETYPE.getValue()+":' expected in query but was "+parameters
			.get(Constants.QUERY_PARAMETER),
		parameters
			.get(Constants.QUERY_PARAMETER).contains(FullTextFields.PLACETYPE.getValue()+":"));
	assertTrue("wrong query parameter found '"+FullTextFields.COUNTRYCODE.getValue()+":' expected in query but was "+parameters
			.get(Constants.QUERY_PARAMETER),
			parameters
				.get(Constants.QUERY_PARAMETER).contains(FullTextFields.COUNTRYCODE.getValue()+":"));
    }

    @Test
    public void testToQueryStringShouldreturnCorrectParamsForAtom() {
	Pagination pagination = paginate().from(3).to(10);
	Output output = Output.withFormat(OutputFormat.ATOM).withLanguageCode(
		"FR").withStyle(OutputStyle.SHORT).withIndentation();
	FulltextQuery fulltextQuery = new FulltextQuery("Saint-André",
		pagination, output, com.gisgraphy.fulltext.Constants.ONLY_ADM_PLACETYPE, "fr");
	// split parameters
	HashMap<String, String> parameters = GisgraphyTestHelper.splitURLParams(
			FulltextQuerySolrHelper.toQueryString(fulltextQuery), "&");
	// check parameters
	assertEquals("wrong field list",outputStyleHelper.getFulltextFieldList(Output.OutputStyle.MEDIUM,"FR"), parameters.get(Constants.FL_PARAMETER));
	assertEquals("wrong indent parameter found", "on", parameters
		.get(Constants.INDENT_PARAMETER));
	assertEquals("wrong echoparams parameter found", "none", parameters
		.get(Constants.ECHOPARAMS_PARAMETER));
	assertEquals("wrong start parameter found", "2", parameters
		.get(Constants.START_PARAMETER));
	assertEquals("wrong rows parameter found", "8", parameters
		.get(Constants.ROWS_PARAMETER));
	assertEquals("wrong output format parameter found", OutputFormat.GEORSS
		.getParameterValue(), parameters
		.get(Constants.OUTPUT_FORMAT_PARAMETER));
	assertEquals("wrong stylesheet", Constants.ATOM_STYLESHEET, parameters
		.get(Constants.STYLESHEET_PARAMETER));
	assertEquals("wrong query type parameter found",
		Constants.SolrQueryType.advanced.toString(), parameters
			.get(Constants.QT_PARAMETER));
	assertTrue("wrong query parameter found '"+FullTextFields.PLACETYPE.getValue()+":' expected in query but was "+parameters
			.get(Constants.QUERY_PARAMETER),
		parameters
			.get(Constants.QUERY_PARAMETER).contains(FullTextFields.PLACETYPE.getValue()+":"));
	assertTrue("wrong query parameter found '"+FullTextFields.COUNTRYCODE.getValue()+":' expected in query but was "+parameters
			.get(Constants.QUERY_PARAMETER),
			parameters
				.get(Constants.QUERY_PARAMETER).contains(FullTextFields.COUNTRYCODE.getValue()+":"));
    }
    
    @Test
    public void testToQueryStringShouldreturnCorrectParamsForSpellChecking() {
    	boolean savedSpellCheckingActiveByDefaultValue = SpellCheckerConfig.activeByDefault;
    	boolean savedSpellCheckerConfigEnabled = SpellCheckerConfig.enabled;
    	try {
	SpellCheckerConfig.activeByDefault= true;
	SpellCheckerConfig.enabled = false;
	Pagination pagination = paginate().from(3).to(10);
	Output output = Output.withFormat(OutputFormat.ATOM).withLanguageCode(
		"FR").withStyle(OutputStyle.SHORT).withIndentation();
	FulltextQuery fulltextQuery = new FulltextQuery("Saint-André",
		pagination, output, com.gisgraphy.fulltext.Constants.ONLY_ADM_PLACETYPE, "fr").withSpellChecking();
	// split parameters
	HashMap<String, String> parameters = GisgraphyTestHelper.splitURLParams(
			FulltextQuerySolrHelper.toQueryString(fulltextQuery), "&");
	// check parameters
	assertTrue("the fulltextquery should have spellchecking enabled even if spellchecker is disabled", fulltextQuery.hasSpellChecking());
	assertTrue("spellchecker should not be listed if spellchecker is disabled", !parameters
		.containsKey(Constants.SPELLCHECKER_ENABLED_PARAMETER));
	//active spellchecker and re test
	SpellCheckerConfig.enabled = true;
	fulltextQuery = new FulltextQuery("Saint-André",
			pagination, output, com.gisgraphy.fulltext.Constants.ONLY_ADM_PLACETYPE, "fr").withSpellChecking();
	parameters = GisgraphyTestHelper.splitURLParams(
			FulltextQuerySolrHelper.toQueryString(fulltextQuery), "&");
	assertTrue("the fulltextquery should have spellchecking enabled when spellchecker is enabled", fulltextQuery.hasSpellChecking());
	assertEquals("spellchecker should be enabled", "true", parameters
			.get(Constants.SPELLCHECKER_ENABLED_PARAMETER));
	assertEquals("spellchecker should be enabled", String.valueOf(SpellCheckerConfig.collateResults), parameters
			.get(Constants.SPELLCHECKER_COLLATE_RESULTS_PARAMETER));
	assertEquals("spellchecker should be enabled",  String.valueOf(SpellCheckerConfig.numberOfSuggestion), parameters
			.get(Constants.SPELLCHECKER_NUMBER_OF_SUGGESTION_PARAMETER));
	assertEquals("spellchecker should be enabled", SpellCheckerConfig.spellcheckerDictionaryName.toString(), parameters
			.get(Constants.SPELLCHECKER_DICTIONARY_NAME_PARAMETER));
    	} catch (RuntimeException e) {
		fail(e.getMessage());
	} finally {
	    SpellCheckerConfig.activeByDefault = savedSpellCheckingActiveByDefaultValue;
	    SpellCheckerConfig.enabled=savedSpellCheckerConfigEnabled;
	}
    }

    @Test
    public void testIsStreetQuery(){
    	FulltextQuery query = new FulltextQuery("foo");
    	query.withPlaceTypes(new Class[]{Street.class});
    	Assert.assertTrue(FulltextQuerySolrHelper.isStreetQuery(query));
    	query.withPlaceTypes(null);
    	Assert.assertFalse(FulltextQuerySolrHelper.isStreetQuery(query));
    	query.withPlaceTypes(new Class[]{Street.class,City.class});
    	Assert.assertTrue(FulltextQuerySolrHelper.isStreetQuery(query));
    	query.withPlaceTypes(new Class[]{Street.class,null});
    	Assert.assertTrue(FulltextQuerySolrHelper.isStreetQuery(query));
    }
    
    @Test
    public void testToQueryStringShouldreturnCorrectParamsForAdvancedGeolocQuery_allwordRequired() {
	Pagination pagination = paginate().from(3).to(10);
	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
		"FR").withStyle(OutputStyle.SHORT).withIndentation();
	String searchTerm = "Saint-André";
	FulltextQuery fulltextQuery = new FulltextQuery(searchTerm,
		pagination, output, com.gisgraphy.fulltext.Constants.ONLY_ADM_PLACETYPE, "fr").withAllWordsRequired(true);
	Float longitude = 20F;
	Float latitude = 30F;
	fulltextQuery.around(GeolocHelper.createPoint(longitude, latitude));
	// split parameters
	HashMap<String, String> parameters = GisgraphyTestHelper.splitURLParams(
		FulltextQuerySolrHelper.toQueryString(fulltextQuery), "&");
	// check parameters
	assertEquals(outputStyleHelper.getFulltextFieldList(Output.OutputStyle.SHORT,"FR"), parameters
		.get(Constants.FL_PARAMETER));
	assertEquals("wrong indent parameter found", "on", parameters
		.get(Constants.INDENT_PARAMETER));
	assertEquals("wrong echoparams parameter found", "none", parameters
		.get(Constants.ECHOPARAMS_PARAMETER));
	assertEquals("wrong start parameter found", "2", parameters
		.get(Constants.START_PARAMETER));
	assertEquals("wrong rows parameter found", "8", parameters
		.get(Constants.ROWS_PARAMETER));
	assertEquals("wrong output format parameter found", OutputFormat.JSON
		.getParameterValue(), parameters
		.get(Constants.OUTPUT_FORMAT_PARAMETER));
	assertEquals("wrong query type parameter found",
		Constants.SolrQueryType.advanced.toString(), parameters
			.get(Constants.QT_PARAMETER));
	assertTrue("wrong query parameter found (no search term part) actual : "+parameters
		.get(Constants.QUERY_PARAMETER),
		parameters
			.get(Constants.QUERY_PARAMETER).contains(String.format(NESTED_QUERY_TEMPLATE, "","",searchTerm)));
	
	assertTrue("wrong query parameter found (no geoloc part) actual : "+parameters
		.get(Constants.QUERY_PARAMETER),
		parameters
			.get(Constants.QUERY_PARAMETER).contains(String.format(Locale.US,FulltextQuerySolrHelper.GEOLOC_QUERY_TEMPLATE, fulltextQuery.getPoint().getY(),fulltextQuery.getPoint().getX(),fulltextQuery.getRadius()/1000)));
	assertTrue("wrong query parameter found '"+FullTextFields.PLACETYPE.getValue()+":' expected in query but was "+parameters
			.get(Constants.QUERY_PARAMETER),
		parameters
			.get(Constants.QUERY_PARAMETER).contains(FullTextFields.PLACETYPE.getValue()+":"));
	assertTrue("wrong query parameter found '"+FullTextFields.COUNTRYCODE.getValue()+":' expected in query but was "+parameters
			.get(Constants.QUERY_PARAMETER),
			parameters
				.get(Constants.QUERY_PARAMETER).contains(FullTextFields.COUNTRYCODE.getValue()+":"));
	assertNotNull("spellchecker query should be set when numeric query",parameters
		.get(Constants.SPELLCHECKER_QUERY_PARAMETER));  
    }
    
    @Test
    public void testToQueryStringShouldreturnCorrectParamsForAdvancedGeolocQuery_allwordNotRequired() {
	Pagination pagination = paginate().from(3).to(10);
	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
		"FR").withStyle(OutputStyle.SHORT).withIndentation();
	String searchTerm = "Saint-André";
	FulltextQuery fulltextQuery = new FulltextQuery(searchTerm,
		pagination, output, com.gisgraphy.fulltext.Constants.ONLY_ADM_PLACETYPE, "fr").withAllWordsRequired(false);
	Float longitude = 20F;
	Float latitude = 30F;
	fulltextQuery.around(GeolocHelper.createPoint(longitude, latitude));
	// split parameters
	HashMap<String, String> parameters = GisgraphyTestHelper.splitURLParams(
		FulltextQuerySolrHelper.toQueryString(fulltextQuery), "&");
	// check parameters
	assertEquals(outputStyleHelper.getFulltextFieldList(Output.OutputStyle.SHORT,"FR"), parameters
		.get(Constants.FL_PARAMETER));
	assertEquals("wrong indent parameter found", "on", parameters
		.get(Constants.INDENT_PARAMETER));
	assertEquals("wrong echoparams parameter found", "none", parameters
		.get(Constants.ECHOPARAMS_PARAMETER));
	assertEquals("wrong start parameter found", "2", parameters
		.get(Constants.START_PARAMETER));
	assertEquals("wrong rows parameter found", "8", parameters
		.get(Constants.ROWS_PARAMETER));
	assertEquals("wrong output format parameter found", OutputFormat.JSON
		.getParameterValue(), parameters
		.get(Constants.OUTPUT_FORMAT_PARAMETER));
	assertEquals("wrong query type parameter found",
		Constants.SolrQueryType.advanced.toString(), parameters
			.get(Constants.QT_PARAMETER));
	assertTrue("wrong query parameter found (no search term part) actual : "+parameters
		.get(Constants.QUERY_PARAMETER),
		parameters
			.get(Constants.QUERY_PARAMETER).contains(String.format(NESTED_QUERY_NOT_ALL_WORDS_REQUIRED_TEMPLATE, "","",searchTerm)));
	
	assertTrue("wrong query parameter found (no geoloc part) actual : "+parameters
		.get(Constants.QUERY_PARAMETER),
		parameters
			.get(Constants.QUERY_PARAMETER).contains(String.format(Locale.US,FulltextQuerySolrHelper.GEOLOC_QUERY_TEMPLATE, fulltextQuery.getPoint().getY(),fulltextQuery.getPoint().getX(),fulltextQuery.getRadius()/1000)));
	assertTrue("wrong query parameter found '"+FullTextFields.PLACETYPE.getValue()+":' expected in query but was "+parameters
			.get(Constants.QUERY_PARAMETER),
		parameters
			.get(Constants.QUERY_PARAMETER).contains(FullTextFields.PLACETYPE.getValue()+":"));
	assertTrue("wrong query parameter found '"+FullTextFields.COUNTRYCODE.getValue()+":' expected in query but was "+parameters
			.get(Constants.QUERY_PARAMETER),
			parameters
				.get(Constants.QUERY_PARAMETER).contains(FullTextFields.COUNTRYCODE.getValue()+":"));
	assertNotNull("spellchecker query should be set when numeric query",parameters
		.get(Constants.SPELLCHECKER_QUERY_PARAMETER));  
    }
    
    @Test
    public void testToQueryStringShouldreturnCorrectParamsForStreetQuery() {
	Pagination pagination = paginate().from(3).to(10);
	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
		"FR").withStyle(OutputStyle.SHORT).withIndentation();
	String searchTerm = "Saint-André";
	FulltextQuery fulltextQuery = new FulltextQuery(searchTerm,
		pagination, output, com.gisgraphy.fulltext.Constants.STREET_PLACETYPE, "fr");
	Float longitude = 20F;
	Float latitude = 30F;
	fulltextQuery.around(GeolocHelper.createPoint(longitude, latitude));
	// split parameters
	HashMap<String, String> parameters = GisgraphyTestHelper.splitURLParams(
		FulltextQuerySolrHelper.toQueryString(fulltextQuery), "&");
	// check parameters
	assertEquals(outputStyleHelper.getFulltextFieldList(Output.OutputStyle.SHORT,"FR"), parameters
		.get(Constants.FL_PARAMETER));
	assertEquals("wrong indent parameter found", "on", parameters
		.get(Constants.INDENT_PARAMETER));
	assertEquals("wrong echoparams parameter found", "none", parameters
		.get(Constants.ECHOPARAMS_PARAMETER));
	assertEquals("wrong start parameter found", "2", parameters
		.get(Constants.START_PARAMETER));
	assertEquals("wrong rows parameter found", "8", parameters
		.get(Constants.ROWS_PARAMETER));
	assertEquals("wrong output format parameter found", OutputFormat.JSON
		.getParameterValue(), parameters
		.get(Constants.OUTPUT_FORMAT_PARAMETER));
	assertEquals("wrong query type parameter found",
		Constants.SolrQueryType.advanced.toString(), parameters
			.get(Constants.QT_PARAMETER));
	
	assertTrue(FullTextFields.IS_IN.getValue()+" field should be added : ",
			parameters
				.get(Constants.QUERY_PARAMETER).contains(FullTextFields.IS_IN.getValue()));
	
	assertTrue(FullTextFields.IS_IN_ADM.getValue()+" field should be added : ",
			parameters
				.get(Constants.QUERY_PARAMETER).contains(FullTextFields.IS_IN_ADM.getValue()));
	
	assertTrue(FullTextFields.IS_IN_PLACE.getValue()+" field should be added : ",
			parameters
				.get(Constants.QUERY_PARAMETER).contains(FullTextFields.IS_IN_PLACE.getValue()));
	
	assertTrue(FullTextFields.IS_IN_ZIP.getValue()+" field should be added : ",
			parameters
				.get(Constants.QUERY_PARAMETER).contains(FullTextFields.IS_IN_ZIP.getValue()));
	
	assertTrue("wrong query parameter found (no geoloc part) actual : "+parameters
		.get(Constants.QUERY_PARAMETER),
		parameters
			.get(Constants.QUERY_PARAMETER).contains(String.format(Locale.US,FulltextQuerySolrHelper.GEOLOC_QUERY_TEMPLATE, fulltextQuery.getPoint().getY(),fulltextQuery.getPoint().getX(),fulltextQuery.getRadius()/1000)));
	assertTrue("wrong query parameter found '"+FullTextFields.PLACETYPE.getValue()+":' expected in query but was "+parameters
			.get(Constants.QUERY_PARAMETER),
		parameters
			.get(Constants.QUERY_PARAMETER).contains(FullTextFields.PLACETYPE.getValue()+":"));
	assertTrue("wrong query parameter found '"+FullTextFields.COUNTRYCODE.getValue()+":' expected in query but was "+parameters
			.get(Constants.QUERY_PARAMETER),
			parameters
				.get(Constants.QUERY_PARAMETER).contains(FullTextFields.COUNTRYCODE.getValue()+":"));
	assertNotNull("spellchecker query should be set when numeric query",parameters
		.get(Constants.SPELLCHECKER_QUERY_PARAMETER));  
    }


}
