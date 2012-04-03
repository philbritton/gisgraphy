package com.gisgraphy.geocoloc;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.placetype.City;
import com.gisgraphy.domain.valueobject.GeolocResultsDto;
import com.gisgraphy.domain.valueobject.GisFeatureDistance;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.geoloc.GeolocQuery;
import com.gisgraphy.rest.IRestClient;
import com.gisgraphy.serializer.common.OutputFormat;
import com.gisgraphy.servlet.GisgraphyServlet;
import com.gisgraphy.test.GisgraphyUtilsTestHelper;

public class GeolocClientTest {

	private static final String HTTP_LOCALHOST_8080_GEOLOC = "http://localhost:8080/geoloc";

	@Test
	public void constructorShouldNotAcceptNullBaseUrl() {
		try {
			new GeolocClient(null);
			Assert.fail("constructor should not accept null base url");
		} catch (IllegalArgumentException e) {
			// ignore
		}
	}

	@Test
	public void constructorShouldNotAcceptEmptyBaseUrl() {
		try {
			new GeolocClient(" ");
			Assert.fail("constructor should not accept empty base URL");
		} catch (IllegalArgumentException e) {
			// ignore
		}
	}

	@Test
	public void constructorShouldNotAcceptWrongBaseUrl() {
		try {
			new GeolocClient("foo");
			Assert.fail("constructor should not accept wrong base URL");
		} catch (IllegalArgumentException e) {
			// ignore
		}
	}

	@Test
	public void constructorShouldAccepthttpsBaseUrl() {
		new GeolocClient("https://localhost:8080/geocoding");
	}

//	@Test
	public void constructorShouldAccepthttpBaseUrl() {
		new GeolocClient(HTTP_LOCALHOST_8080_GEOLOC);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void executeQueryWithNullQuery(){
		GeolocClient client = new GeolocClient(HTTP_LOCALHOST_8080_GEOLOC);
		client.executeQuery(null);
	}
	
	@Test
	public void executeQuery(){
		GeolocClient client = new GeolocClient(HTTP_LOCALHOST_8080_GEOLOC);
		GeolocQuery query = createQuery();
		GeolocQuery queryWOCallback = createQuery();
		queryWOCallback.withCallback(null);
		IRestClient restClient = EasyMock.createMock(IRestClient.class);
		String queryString = client.geolocQuerytoQueryString(queryWOCallback);//check that callback is disabled
		GeolocResultsDto geolocResultsDto = new GeolocResultsDto(new ArrayList<GisFeatureDistance>(), 5L);
		String url = HTTP_LOCALHOST_8080_GEOLOC + queryString;
		System.out.println("url=" + url);
		EasyMock.expect(restClient.get(url, GeolocResultsDto.class, OutputFormat.JSON)).andReturn(geolocResultsDto);
		EasyMock.replay(restClient);
		client.setRestClient(restClient);
		Assert.assertEquals(geolocResultsDto, client.executeQuery(query));
		EasyMock.verify(restClient);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void executeQueryToStringWithNullQuery(){
		GeolocClient client = new GeolocClient(HTTP_LOCALHOST_8080_GEOLOC);
		client.executeQueryToString(null);
	}
	
	@Test
	public void executeQueryToString(){
		GeolocClient client = new GeolocClient(HTTP_LOCALHOST_8080_GEOLOC);
		GeolocQuery query = createQuery();
		IRestClient restClient = EasyMock.createMock(IRestClient.class);
		String queryString = client.geolocQuerytoQueryString(query);
		String url = HTTP_LOCALHOST_8080_GEOLOC + queryString;
		System.out.println("url=" + url);
		String result = "result";
		EasyMock.expect(restClient.get(url, String.class, OutputFormat.PHP)).andReturn(result);
		EasyMock.replay(restClient);
		client.setRestClient(restClient);
		Assert.assertEquals(result,client.executeQueryToString(query));
		EasyMock.verify(restClient);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void executeAndSerializeWithNullQuery(){
		GeolocClient client = new GeolocClient(HTTP_LOCALHOST_8080_GEOLOC);
		client.executeAndSerialize(null,new ByteArrayOutputStream());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void executeAndSerializeWithNullOutputStream(){
		GeolocClient client = new GeolocClient(HTTP_LOCALHOST_8080_GEOLOC);
		client.executeAndSerialize(new GeolocQuery(GisgraphyUtilsTestHelper.createPoint(3F, 2F)),null);
	}
	
	@Test
	public void executeQueryToOuputStream() {
		GeolocClient client = new GeolocClient(HTTP_LOCALHOST_8080_GEOLOC);
		GeolocQuery query = createQuery();
		IRestClient restClient = EasyMock.createMock(IRestClient.class);
		String queryString = client.geolocQuerytoQueryString(query);
		String url = HTTP_LOCALHOST_8080_GEOLOC + queryString;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.out.println("url=" + url);
		String result = "result";
		restClient.get(url, outputStream, OutputFormat.PHP);
		EasyMock.replay(restClient);
		client.setRestClient(restClient);
		client.executeAndSerialize(query, outputStream);
		EasyMock.verify(restClient);
	}
	
	
	@Test
	public void geolocQuerytoQueryString(){
		GeolocQuery query = createQuery();
		GeolocClient client = new GeolocClient(HTTP_LOCALHOST_8080_GEOLOC);
		String queryString = client.geolocQuerytoQueryString(query);
		HashMap<String, String> params = splitURLParams(queryString,"&");
		Assert.assertEquals(query.getLatitude().toString(), params.get(GeolocQuery.LAT_PARAMETER));
		Assert.assertEquals(query.getLongitude().toString(), params.get(GeolocQuery.LONG_PARAMETER));
		Assert.assertEquals(query.getRadius()+"", params.get(GeolocQuery.RADIUS_PARAMETER));
		Assert.assertEquals(query.getFirstPaginationIndex()+"", params.get(GisgraphyServlet.FROM_PARAMETER));
		Assert.assertEquals(query.getLastPaginationIndex()+"", params.get(GisgraphyServlet.TO_PARAMETER));
		Assert.assertEquals(query.getCallback()+"", params.get(GeolocQuery.CALLBACK_PARAMETER));
		Assert.assertEquals(query.getOutputFormat()+"", params.get(GisgraphyServlet.FORMAT_PARAMETER));
		Assert.assertEquals(query.getPlaceType().getSimpleName()+"", params.get(GeolocQuery.PLACETYPE_PARAMETER));
		Assert.assertEquals(query.hasDistanceField()+"", params.get(GeolocQuery.DISTANCE_PARAMETER));
		Assert.assertEquals(query.isOutputIndented()+"", params.get(GisgraphyServlet.INDENT_PARAMETER));
		Assert.assertEquals(query.getApikey()+"", params.get(GisgraphyServlet.APIKEY_PARAMETER));
		
	}
	
	

	private GeolocQuery createQuery() {
		Float lng = 2F;
		Float lat = 3F;
		GeolocQuery query = new GeolocQuery(GisgraphyUtilsTestHelper.createPoint(lng, lat));
		query.setApikey("apikey");
		query.withRadius(20D);
		query.withOutput(Output.withFormat(OutputFormat.PHP).withIndentation());
		query.withPagination(Pagination.paginate().from(2).to(4));
		query.withCallback("callback");
		query.withPlaceType(City.class);
		query.withDistanceField(true);
		return query;
	}
	
	 private static HashMap<String, String> splitURLParams(String completeURL,
			    String andSign) {
			int i;
			HashMap<String, String> searchparms = new HashMap<String, String>();
			;
			i = completeURL.indexOf("?");
			if (i > -1) {
			    String searchURL = completeURL
				    .substring(completeURL.indexOf("?") + 1);

			    String[] paramArray = searchURL.split(andSign);
			    for (int c = 0; c < paramArray.length; c++) {
				String[] paramSplited = paramArray[c].split("=");
				try {
				    searchparms.put(paramSplited[0], java.net.URLDecoder
					    .decode(paramSplited[1], "UTF-8"));
				} catch (UnsupportedEncodingException e) {
				    return new HashMap<String, String>();
				}

			    }
			    // dumpHashtable;
			    java.util.Iterator<String> keys = searchparms.keySet().iterator();
			    while (keys.hasNext()) {
				String s = (String) keys.next();
			    }

			}
			return searchparms;
		    }


}
