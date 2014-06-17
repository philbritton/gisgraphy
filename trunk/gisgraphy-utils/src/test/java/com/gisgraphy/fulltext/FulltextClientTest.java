package com.gisgraphy.fulltext;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.placetype.City;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.geoloc.GeolocQuery;
import com.gisgraphy.serializer.common.OutputFormat;
import com.gisgraphy.servlet.GisgraphyServlet;
import com.gisgraphy.test.GisgraphyUtilsTestHelper;

public class FulltextClientTest {

	private static final String HTTP_LOCALHOST_8080_FULLTEXT = "http://services.gisgraphy.com/fulltext";

	@Test
	public void constructorShouldNotAcceptNullBaseUrl() {
		try {
			new FulltextClient(null);
			Assert.fail("constructor should not accept null base url");
		} catch (IllegalArgumentException e) {
			// ignore
		}
	}

	@Test
	public void constructorShouldNotAcceptEmptyBaseUrl() {
		try {
			new FulltextClient(" ");
			Assert.fail("constructor should not accept empty base URL");
		} catch (IllegalArgumentException e) {
			// ignore
		}
	}

	@Test
	public void constructorShouldNotAcceptWrongBaseUrl() {
		try {
			new FulltextClient("foo");
			Assert.fail("constructor should not accept wrong base URL");
		} catch (IllegalArgumentException e) {
			// ignore
		}
	}

	@Test
	public void constructorShouldAccepthttpsBaseUrl() {
		new FulltextClient("https://localhost:8080/geocoding");
	}

//	@Test
	public void constructorShouldAccepthttpBaseUrl() {
		new FulltextClient(HTTP_LOCALHOST_8080_FULLTEXT);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void executeQueryWithNullQuery(){
		FulltextClient client = new FulltextClient(HTTP_LOCALHOST_8080_FULLTEXT);
		client.executeQuery(null);
	}
	
	@Test(expected=RuntimeException.class)
	public void executeQueryToDatabaseObjects(){
		FulltextClient client = new FulltextClient(HTTP_LOCALHOST_8080_FULLTEXT);
		client.executeQueryToDatabaseObjects(new FulltextQuery("test"));
	}
	
	@Test
	public void fulltextQuerytoQueryString(){
		FulltextQuery query = createQuery();
		FulltextClient client = new FulltextClient(HTTP_LOCALHOST_8080_FULLTEXT);
		String queryString = client.fulltextQueryToQueryString(query);
		HashMap<String, String> params = GisgraphyUtilsTestHelper.splitURLParams(queryString,"&");
		Assert.assertEquals(query.getQuery(), params.get(FulltextQuery.QUERY_PARAMETER));
		Assert.assertEquals(query.getLatitude().toString(), params.get(FulltextQuery.LAT_PARAMETER));
		Assert.assertEquals(query.getLongitude().toString(), params.get(FulltextQuery.LONG_PARAMETER));
		Assert.assertEquals(query.getRadius()+"", params.get(FulltextQuery.RADIUS_PARAMETER));
		Assert.assertEquals(query.isAllwordsRequired()+"", params.get(FulltextQuery.ALLWORDSREQUIRED_PARAMETER));
		Assert.assertEquals(query.getFirstPaginationIndex()+"", params.get(GisgraphyServlet.FROM_PARAMETER));
		Assert.assertEquals(query.getLastPaginationIndex()+"", params.get(GisgraphyServlet.TO_PARAMETER));
		Assert.assertEquals(query.getOutputFormat()+"", params.get(GisgraphyServlet.FORMAT_PARAMETER));
		Assert.assertEquals(query.getOutputLanguage(), params.get(FulltextQuery.LANG_PARAMETER));
		Assert.assertEquals(query.getOutputStyle()+"", params.get(FulltextQuery.STYLE_PARAMETER));
		Assert.assertEquals(query.getPlaceTypes()[0].getSimpleName(), params.get(GeolocQuery.PLACETYPE_PARAMETER));
		Assert.assertEquals(query.getApikey()+"", params.get(GisgraphyServlet.APIKEY_PARAMETER));
		Assert.assertEquals(query.isOutputIndented()+"", params.get(GisgraphyServlet.INDENT_PARAMETER));
		Assert.assertEquals(query.getCountryCode()+"", params.get(FulltextQuery.COUNTRY_PARAMETER));
		Assert.assertEquals(query.isSpellcheckingEnabled()+"", params.get(FulltextQuery.SPELLCHECKING_PARAMETER));
		
	}
	
	@Test
	public void executeQuery(){
		FulltextQuery query = createQuery();
		FulltextClient client = new FulltextClient(HTTP_LOCALHOST_8080_FULLTEXT);
		FulltextResultsDto dto = client.executeQuery(query);
		Assert.assertNotNull(dto.getMaxScore());
		Assert.assertNotNull(dto.getNumFound());
		Assert.assertNotNull(dto.getQTime());
		Assert.assertNotNull(dto.getResults());
		Assert.assertNotNull(dto.getResults());
		
	}
	
	 private FulltextQuery createQuery() {
		 FulltextQuery query = new FulltextQuery("paris");
		 query.around(GisgraphyUtilsTestHelper.createPoint(2.349F, 48.853F));
		 query.withRadius(1000);
		 query.withAllWordsRequired(true);
		 query.withPagination(Pagination.paginate().from(1).to(5));
		 query.withOutput(Output.withFormat(OutputFormat.JSON).withLanguageCode("FR").withStyle(OutputStyle.FULL).withIndentation());
		 query.setApikey("123");
		 query.limitToCountryCode("FR");
		 query.withPlaceTypes(new Class[] {City.class});
		 query.withoutSpellChecking();
		 
		 return query;
	 }



}
