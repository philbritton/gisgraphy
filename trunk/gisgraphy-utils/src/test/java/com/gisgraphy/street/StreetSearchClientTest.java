package com.gisgraphy.street;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.domain.valueobject.StreetDistance;
import com.gisgraphy.domain.valueobject.StreetSearchResultsDto;
import com.gisgraphy.rest.IRestClient;
import com.gisgraphy.serializer.common.OutputFormat;
import com.gisgraphy.servlet.GisgraphyServlet;
import com.gisgraphy.test.GisgraphyUtilsTestHelper;

public class StreetSearchClientTest {

	private static final String HTTP_LOCALHOST_8080_GEOLOC = "	http://localhost:8080/street/streetsearch";

	@Test
	public void constructorShouldNotAcceptNullBaseUrl() {
		try {
			new StreetSearchClient(null);
			Assert.fail("constructor should not accept null base url");
		} catch (IllegalArgumentException e) {
			// ignore
		}
	}

	@Test
	public void constructorShouldNotAcceptEmptyBaseUrl() {
		try {
			new StreetSearchClient(" ");
			Assert.fail("constructor should not accept empty base URL");
		} catch (IllegalArgumentException e) {
			// ignore
		}
	}

	@Test
	public void constructorShouldNotAcceptWrongBaseUrl() {
		try {
			new StreetSearchClient("foo");
			Assert.fail("constructor should not accept wrong base URL");
		} catch (IllegalArgumentException e) {
			// ignore
		}
	}

	@Test
	public void constructorShouldAccepthttpsBaseUrl() {
		new StreetSearchClient("https://localhost:8080/geocoding");
	}

	// @Test
	public void constructorShouldAccepthttpBaseUrl() {
		new StreetSearchClient(HTTP_LOCALHOST_8080_GEOLOC);
	}

	@Test(expected = IllegalArgumentException.class)
	public void executeQueryWithNullQuery() {
		StreetSearchClient client = new StreetSearchClient(HTTP_LOCALHOST_8080_GEOLOC);
		client.executeQuery(null);
	}

	@Test
	public void executeQuery() {
		StreetSearchClient client = new StreetSearchClient(HTTP_LOCALHOST_8080_GEOLOC);
		StreetSearchQuery query = createQuery();
		StreetSearchQuery queryWOCallback = createQuery();
		queryWOCallback.withCallback(null);
		IRestClient restClient = EasyMock.createMock(IRestClient.class);
		String queryString = client.streetsearchQuerytoQueryString(queryWOCallback);//check that callback is disabled
		StreetSearchResultsDto dto = new StreetSearchResultsDto(new ArrayList<StreetDistance>(), 5L, "query");
		String url = HTTP_LOCALHOST_8080_GEOLOC + queryString;
		System.out.println("url=" + url);
		EasyMock.expect(restClient.get(url, StreetSearchResultsDto.class, OutputFormat.JSON)).andReturn(dto);
		EasyMock.replay(restClient);
		client.setRestClient(restClient);
		Assert.assertEquals(dto, client.executeQuery(query));
		EasyMock.verify(restClient);
	}

	@Test(expected = IllegalArgumentException.class)
	public void executeQueryToStringWithNullQuery() {
		StreetSearchClient client = new StreetSearchClient(HTTP_LOCALHOST_8080_GEOLOC);
		client.executeQueryToString(null);
	}

	@Test
	public void executeQueryToString() {

		StreetSearchClient client = new StreetSearchClient(HTTP_LOCALHOST_8080_GEOLOC);
		StreetSearchQuery query = createQuery();
		IRestClient restClient = EasyMock.createMock(IRestClient.class);
		String queryString = client.streetsearchQuerytoQueryString(query);
		String url = HTTP_LOCALHOST_8080_GEOLOC + queryString;
		System.out.println("url=" + url);
		String result = "result";
		EasyMock.expect(restClient.get(url, String.class, OutputFormat.PHP)).andReturn(result);
		EasyMock.replay(restClient);
		client.setRestClient(restClient);
		Assert.assertEquals(result, client.executeQueryToString(query));
		EasyMock.verify(restClient);
	}

	@Test(expected = IllegalArgumentException.class)
	public void executeAndSerializeWithNullQuery() {
		StreetSearchClient client = new StreetSearchClient(HTTP_LOCALHOST_8080_GEOLOC);
		client.executeAndSerialize(null, new ByteArrayOutputStream());
	}

	@Test(expected = IllegalArgumentException.class)
	public void executeAndSerializeWithNullOutputStream() {
		StreetSearchClient client = new StreetSearchClient(HTTP_LOCALHOST_8080_GEOLOC);
		client.executeAndSerialize(new StreetSearchQuery(GisgraphyUtilsTestHelper.createPoint(3F, 2F)), null);
	}

	@Test
	public void executeQueryToOuputStream() {
		StreetSearchClient client = new StreetSearchClient(HTTP_LOCALHOST_8080_GEOLOC);
		StreetSearchQuery query = createQuery();
		IRestClient restClient = EasyMock.createMock(IRestClient.class);
		String queryString = client.streetsearchQuerytoQueryString(query);
		String url = HTTP_LOCALHOST_8080_GEOLOC + queryString;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.out.println("url=" + url);
		restClient.get(url, outputStream, OutputFormat.PHP);
		EasyMock.replay(restClient);
		client.setRestClient(restClient);
		client.executeAndSerialize(query, outputStream);
		EasyMock.verify(restClient);

	}

	@Test
	public void geolocQuerytoQueryString() {
		StreetSearchQuery query = createQuery();
		StreetSearchClient client = new StreetSearchClient(HTTP_LOCALHOST_8080_GEOLOC);
		String queryString = client.streetsearchQuerytoQueryString(query);
		HashMap<String, String> params = GisgraphyUtilsTestHelper.splitURLParams(queryString, "&");
		Assert.assertEquals(query.getLatitude().toString(), params.get(StreetSearchQuery.LAT_PARAMETER));
		Assert.assertEquals(query.getLongitude().toString(), params.get(StreetSearchQuery.LONG_PARAMETER));
		Assert.assertEquals(query.getRadius() + "", params.get(StreetSearchQuery.RADIUS_PARAMETER));
		Assert.assertEquals(query.getFirstPaginationIndex() + "", params.get(GisgraphyServlet.FROM_PARAMETER));
		Assert.assertEquals(query.getLastPaginationIndex() + "", params.get(GisgraphyServlet.TO_PARAMETER));
		Assert.assertEquals(query.getCallback() + "", params.get(StreetSearchQuery.CALLBACK_PARAMETER));
		Assert.assertEquals(query.getOutputFormat() + "", params.get(GisgraphyServlet.FORMAT_PARAMETER));
		Assert.assertEquals(query.getPlaceType().getSimpleName() + "", params.get(StreetSearchQuery.PLACETYPE_PARAMETER));
		Assert.assertEquals(query.hasDistanceField() + "", params.get(StreetSearchQuery.DISTANCE_PARAMETER));
		Assert.assertEquals(query.isOutputIndented() + "", params.get(GisgraphyServlet.INDENT_PARAMETER));
		Assert.assertEquals(query.getApikey() + "", params.get(GisgraphyServlet.APIKEY_PARAMETER));

		Assert.assertEquals(query.getStreetType() + "", params.get(StreetSearchQuery.STREETTYPE_PARAMETER));
		Assert.assertEquals(query.getName() + "", params.get(StreetSearchQuery.NAME_PARAMETER));
		Assert.assertEquals(query.getOneWay() + "", params.get(StreetSearchQuery.ONEWAY_PARAMETER));

	}

	private StreetSearchQuery createQuery() {
		Float lng = 2F;
		Float lat = 3F;
		StreetSearchQuery query = new StreetSearchQuery(GisgraphyUtilsTestHelper.createPoint(lng, lat));
		query.setApikey("apikey");
		query.withRadius(20D);
		query.withOutput(Output.withFormat(OutputFormat.PHP).withIndentation());
		query.withPagination(Pagination.paginate().from(2).to(4));
		query.withCallback("callback");
		query.withPlaceType(Object.class);
		query.withDistanceField(true);
		query.withStreetType(StreetType.BRIDLEWAY);
		query.withOneWay(true);
		query.withName("name");
		return query;
	}


}
