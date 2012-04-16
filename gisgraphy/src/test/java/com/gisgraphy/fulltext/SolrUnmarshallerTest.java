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
package com.gisgraphy.fulltext;

import static com.gisgraphy.domain.valueobject.Pagination.paginate;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.Adm;
import com.gisgraphy.domain.geoloc.entity.AlternateName;
import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.Country;
import com.gisgraphy.domain.geoloc.entity.Language;
import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.repository.IAdmDao;
import com.gisgraphy.domain.repository.ICountryDao;
import com.gisgraphy.domain.repository.ILanguageDao;
import com.gisgraphy.domain.repository.IOpenStreetMapDao;
import com.gisgraphy.domain.valueobject.AlternateNameSource;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.helper.URLUtils;
import com.gisgraphy.serializer.common.OutputFormat;
import com.gisgraphy.street.StreetType;
import com.gisgraphy.test.GisgraphyTestHelper;

public class SolrUnmarshallerTest extends AbstractIntegrationHttpSolrTestCase {

    @Resource
    private GisgraphyTestHelper geolocTestHelper;
    
    @Resource
    private ICountryDao countryDao;
    
    @Resource
    private ILanguageDao languageDao;
    
    @Resource
    private IOpenStreetMapDao openStreetMapDao;
    
    @Resource
    private IAdmDao admDao;

    @Test
    public void testUnmarshallSolrDocumentShouldReallyUnmarshall() {
	Long featureId = 1002L;
	City city = geolocTestHelper
		.createAndSaveCityWithFullAdmTreeAndCountry(featureId);
	this.solRSynchroniser.commit();
	Pagination pagination = paginate().from(1).to(10);
	Output output = Output.withFormat(OutputFormat.XML).withLanguageCode(
		"FR").withStyle(OutputStyle.FULL).withIndentation();
	FulltextQuery query = new FulltextQuery(city.getName(), pagination,
		output, null, null);
	FulltextResultsDto response = this.fullTextSearchEngine
		.executeQuery(query);
	List<SolrResponseDto> results = response.getResults();
	assertNotNull(
		"There should have a result for a fulltextSearch for "
			+ city.getName()
			+ " and even If no results are return: an empty list should be return,  not null ",
		results);
	assertTrue("There should have a result for a fulltextSearch for "
		+ city.getName(), results.size() == 1);
	SolrResponseDto result = results.get(0);
	assertEquals(city.getName(), result.getName());
	assertEquals(city.getCountryCode(), result.getCountry_code());
	assertEquals(city.getFeatureId(), result.getFeature_id());
	assertEquals(city.getFeatureClass(), result.getFeature_class());
	assertEquals(city.getFeatureCode(), result.getFeature_code());
	assertEquals(city.getAsciiName(), result.getName_ascii());
	assertEquals(city.getElevation(), result.getElevation());
	assertEquals(city.getGtopo30(), result.getGtopo30());
	assertEquals(city.getTimezone(), result.getTimezone());
	assertEquals(city.getFullyQualifiedName(false), result
		.getFully_qualified_name());
	assertEquals(city.getClass().getSimpleName(), result.getPlacetype());
	assertEquals(city.getPopulation(), result.getPopulation());
	assertEquals(city.getLatitude(), result.getLat());
	assertEquals(city.getLongitude(), result.getLng());
	assertEquals(city.getAdm1Code(), result.getAdm1_code());
	assertEquals(city.getAdm2Code(), result.getAdm2_code());
	assertEquals(city.getAdm3Code(), result.getAdm3_code());
	assertEquals(city.getAdm4Code(), result.getAdm4_code());
	assertEquals(city.getAdm1Name(), result.getAdm1_name());
	assertEquals(city.getAdm2Name(), result.getAdm2_name());
	assertEquals(city.getAdm3Name(), result.getAdm3_name());
	assertEquals(city.getAdm4Name(), result.getAdm4_name());
	assertEquals(city.getZipCodes().get(0).getCode(), result.getZipcodes().get(0));
	assertEquals(city.getZipCodes().get(1).getCode(), result.getZipcodes().get(1));
	assertEquals(city.getCountry().getName(), result.getCountry_name());
	assertEquals(URLUtils.createGoogleMapUrl(city.getLocation(), city
		.getName()), result.getGoogle_map_url());
	assertEquals(URLUtils.createCountryFlagUrl(city.getCountryCode()),
		result.getCountry_flag_url());
	assertEquals(URLUtils.createYahooMapUrl(city.getLocation()), result
		.getYahoo_map_url());
	assertEquals(URLUtils.createOpenstreetmapMapUrl(city.getLocation()), result
			.getOpenstreetmap_map_url());

	assertEquals(1, result.getName_alternates().size());
	assertEquals("cityalternate", result.getName_alternates().get(0));

	assertEquals(1, result.getName_alternates_localized().size());
	assertEquals("cityalternateFR", result.getName_alternates_localized()
		.get("FR").get(0));

	assertEquals(2, result.getAdm1_names_alternate().size());
	assertEquals(city.getAdm().getParent().getParent().getAlternateNames()
		.get(0).getName(), result.getAdm1_names_alternate().get(0));
	assertEquals("admGGPalternate2", result.getAdm1_names_alternate()
		.get(1));

	assertEquals(1, result.getAdm2_names_alternate().size());
	assertEquals(city.getAdm().getParent().getAlternateNames().get(0)
		.getName(), result.getAdm2_names_alternate().get(0));

	assertEquals(1, result.getCountry_names_alternate().size());
	assertEquals(city.getCountry().getAlternateNames().get(0).getName(),
		result.getCountry_names_alternate().get(0));

	assertEquals(1, result.getCountry_names_alternate_localized().size());
	assertEquals("franciaFR", result.getCountry_names_alternate_localized()
		.get("FR").get(0));

	assertEquals(1, result.getAdm1_names_alternate_localized().size());
	assertEquals("admGGPalternateFR", result
		.getAdm1_names_alternate_localized().get("FR").get(0));

	assertEquals(1, result.getAdm2_names_alternate_localized().size());
	assertEquals("admGPalternateFR", result
		.getAdm2_names_alternate_localized().get("FR").get(0));
    }

    @Test
    public void testUnmarshallSolrDocumentShouldReallyUnmarshallCountry() {
	Country country = GisgraphyTestHelper
		.createFullFilledCountry();
	
	Language lang = new Language("french", "FR", "FRA");
	Language savedLang = languageDao.save(lang);
	Language retrievedLang = languageDao.get(savedLang.getId());
	assertEquals(savedLang, retrievedLang);

	country.addSpokenLanguage(lang);
	
	AlternateName alternateNameLocalized = new AlternateName("alternateFR",AlternateNameSource.ALTERNATENAMES_FILE);
	alternateNameLocalized.setLanguage("FR");
	AlternateName alternateName = new AlternateName("alternate",AlternateNameSource.ALTERNATENAMES_FILE);
	country.addAlternateName(alternateName);
	country.addAlternateName(alternateNameLocalized);
	countryDao.save(country);
	this.solRSynchroniser.commit();
	Pagination pagination = paginate().from(1).to(10);
	Output output = Output.withFormat(OutputFormat.XML).withLanguageCode(
		"FR").withStyle(OutputStyle.FULL).withIndentation();
	FulltextQuery query = new FulltextQuery(country.getName(), pagination,
		output, null, null);
	FulltextResultsDto response = this.fullTextSearchEngine
		.executeQuery(query);
	List<SolrResponseDto> results = response.getResults();
	assertNotNull(
		"There should have a result for a fulltextSearch for "
			+ country.getName()
			+ " and even If no results are return: an empty list should be return,  not null ",
		results);
	assertTrue("There should have a result for a fulltextSearch for "
		+ country.getName(), results.size() == 1);
	SolrResponseDto result = results.get(0);
	assertNotNull(result.getName());
	assertEquals(country.getName(), result.getName());
	assertNotNull(result.getFeature_id());
	assertEquals(country.getFeatureId(), result.getFeature_id());
	assertNotNull(result.getFeature_class());
	assertEquals(country.getFeatureClass(), result.getFeature_class());
	assertNotNull(result.getFeature_code());
	assertEquals(country.getFeatureCode(), result.getFeature_code());
	assertNotNull(result.getName_ascii());
	assertEquals(country.getAsciiName(), result.getName_ascii());
	assertNotNull(result.getElevation());
	assertEquals(country.getElevation(), result.getElevation());
	assertNotNull(result.getGtopo30());
	assertEquals(country.getGtopo30(), result.getGtopo30());
	assertNotNull(result.getTimezone());
	assertEquals(country.getTimezone(), result.getTimezone());
	assertNotNull(result.getFully_qualified_name());
	assertEquals(country.getFullyQualifiedName(false), result
		.getFully_qualified_name());
	assertNotNull(result.getPlacetype());
	assertEquals(country.getClass().getSimpleName(), result.getPlacetype());
	assertNotNull(result.getPopulation());
	assertEquals(country.getPopulation(), result.getPopulation());
	assertNotNull(result.getLat());
	assertEquals(country.getLatitude(), result.getLat());
	assertNotNull(result.getLng());
	assertEquals(country.getLongitude(), result.getLng());
	assertNotNull(result.getGoogle_map_url());
	assertEquals(URLUtils.createGoogleMapUrl(country.getLocation(), country
		.getName()), result.getGoogle_map_url());
	assertNotNull(result.getCountry_flag_url());
	assertEquals(URLUtils.createCountryFlagUrl(country.getCountryCode()),
		result.getCountry_flag_url());
	assertNotNull(result.getYahoo_map_url());
	assertEquals(URLUtils.createYahooMapUrl(country.getLocation()), result
		.getYahoo_map_url());

	assertEquals(1, result.getCountry_names_alternate().size());
	assertNotNull(result.getCountry_names_alternate().get(0));
	assertEquals(country.getAlternateNames().get(0).getName(),
		result.getCountry_names_alternate().get(0));

	assertEquals(1, result.getCountry_names_alternate_localized().size());
	assertNotNull(result.getCountry_names_alternate_localized()
		.get(alternateNameLocalized.getLanguage()).get(0));
	assertEquals(alternateNameLocalized.getName(), result.getCountry_names_alternate_localized()
		.get(alternateNameLocalized.getLanguage()).get(0));
	assertNotNull(result.getContinent());
	assertEquals(country.getContinent(), result.getContinent());
	assertNotNull(result.getCurrency_code());
	assertEquals(country.getCurrencyCode(), result.getCurrency_code());
	assertNotNull(result.getCurrency_name());
	assertEquals(country.getCurrencyName(), result.getCurrency_name());
	assertNotNull(result.getFips_code());
	assertEquals(country.getFipsCode(), result.getFips_code());
	assertNotNull(result.getIsoalpha3_country_code());
	assertEquals(country.getIso3166Alpha2Code(), result.getIsoalpha2_country_code());
	assertNotNull(result.getIsoalpha3_country_code());
	assertEquals(country.getIso3166Alpha3Code(), result.getIsoalpha3_country_code());
	assertNotNull(result.getPostal_code_mask());
	assertEquals(country.getPostalCodeMask(), result.getPostal_code_mask());
	assertNotNull(result.getPostal_code_regex());
	assertEquals(country.getPostalCodeRegex(), result.getPostal_code_regex());
	assertNotNull(result.getPhone_prefix());
	assertEquals(country.getPhonePrefix(), result.getPhone_prefix());
	assertNotNull(result.getSpoken_languages().get(0));
	assertEquals(country.getSpokenLanguages().get(0).getIso639LanguageName(), result.getSpoken_languages().get(0));
	assertNotNull(result.getTld());
	assertEquals(country.getTld(), result.getTld());
	assertNotNull(result.getCapital_name());
	assertEquals(country.getCapitalName(), result.getCapital_name());
	assertNotNull(result.getArea());
	assertEquals(country.getArea(), result.getArea());
	
    }

    @Test
    public void testUnmarshallSolrDocumentShouldReallyUnmarshallAdm() {
	Adm adm = geolocTestHelper
		.createAdm("AdmName", "FR", "A1", "B2", null, null, null, 2);

	admDao.save(adm);
	
	this.solRSynchroniser.commit();
	Pagination pagination = paginate().from(1).to(10);
	Output output = Output.withFormat(OutputFormat.XML).withLanguageCode(
		"FR").withStyle(OutputStyle.FULL).withIndentation();
	FulltextQuery query = new FulltextQuery(adm.getName(), pagination,
		output, null, null);
	FulltextResultsDto response = this.fullTextSearchEngine
		.executeQuery(query);
	List<SolrResponseDto> results = response.getResults();
	assertNotNull(
		"There should have a result for a fulltextSearch for "
			+ adm.getName()
			+ " and even If no results are return: an empty list should be return,  not null ",
		results);
	assertTrue("There should have a result for a fulltextSearch for "
		+ adm.getName(), results.size() == 1);
	SolrResponseDto result = results.get(0);
	assertNotNull(result.getName());
	assertEquals(adm.getName(), result.getName());
	assertNotNull(result.getAdm1_code());
	assertEquals(adm.getAdm1Code(), result.getAdm1_code());
	assertNotNull(result.getAdm2_code());
	assertEquals(adm.getAdm2Code(), result.getAdm2_code());
	assertEquals("Level should be fill when an Adm is saved ",adm.getLevel(), result.getLevel());
	
	
    }
    
    @Test
    public void testUnmarshallSolrDocumentShouldReallyUnmarshallStreet() {
	OpenStreetMap street = GisgraphyTestHelper.createOpenStreetMapForPeterMartinStreet();
	street.setOneWay(true);
	StreetType streetType = StreetType.BRIDLEWAY;
	street.setStreetType(streetType);
	double length = 1.6D;
	street.setLength(length);
	openStreetMapDao.save(street);
	
	this.solRSynchroniser.commit();
	Pagination pagination = paginate().from(1).to(10);
	Output output = Output.withFormat(OutputFormat.XML).withStyle(OutputStyle.FULL).withIndentation();
	FulltextQuery query = new FulltextQuery(street.getName(), pagination,
		output, null, null);
	FulltextResultsDto response = this.fullTextSearchEngine
		.executeQuery(query);
	List<SolrResponseDto> results = response.getResults();
	assertNotNull(
		"There should have a result for a fulltextSearch for "
			+ street.getName()
			+ " and even If no results are return: an empty list should be return,  not null ",
		results);
	assertTrue("There should have a result for a fulltextSearch for "
		+ street.getName(), results.size() == 1);
	SolrResponseDto result = results.get(0);
	assertNotNull(result.getName());
	 Assert.assertEquals("The results are not correct", street.getName(),
		 result.getName());
	    Assert.assertEquals("The length is not correct", length,
		    result.getLength());
	    Assert.assertEquals("The one_way is not correct", true,
		    result.getOne_way().booleanValue());
	    Assert.assertEquals("The street type is not correct", streetType.toString(),
		    result.getStreet_type().toString());
	    Assert.assertEquals("The is_in is not correct", street.getIsIn(),
			    result.getIs_in());
	    Assert.assertEquals("The openstreetmap id is not correct", street.getOpenstreetmapId(),
			    result.getOpenstreetmap_id());
	
	
    }

    
    
    @Test
    public void testUnmarshallQueryResponseShouldReturnAnEmptyListIfNoResultsAreFound() {
	FulltextQuery query = new FulltextQuery("fake");
	FulltextResultsDto response = this.fullTextSearchEngine
		.executeQuery(query);
	List<SolrResponseDto> results = response.getResults();
	assertNotNull(
		"If no results are return: an empty list should be return,  not null ",
		results);
	assertTrue("If no results are return: an empty list should be return",
		results.size() == 0);
    }

}
