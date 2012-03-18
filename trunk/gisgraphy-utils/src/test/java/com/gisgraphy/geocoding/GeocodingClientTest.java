package com.gisgraphy.geocoding;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.addressparser.Address;
import com.gisgraphy.addressparser.AddressQuery;
import com.gisgraphy.addressparser.AddressResultsDto;
import com.gisgraphy.rest.BeanToRestParameter;
import com.gisgraphy.rest.IRestClient;
import com.gisgraphy.serializer.common.OutputFormat;

public class GeocodingClientTest {

	private static final String HTTP_LOCALHOST_8080_GEOCODING = "http://localhost:8080/geocoding";

	@Test
	public void constructorShouldNotAcceptNullBaseUrl() {
		try {
			new GeocodingClient(null);
			Assert.fail("constructor should not accept null base url");
		} catch (IllegalArgumentException e) {
			// ignore
		}
	}

	@Test
	public void constructorShouldNotAcceptEmptyBaseUrl() {
		try {
			new GeocodingClient(" ");
			Assert.fail("constructor should not accept empty base URL");
		} catch (IllegalArgumentException e) {
			// ignore
		}
	}

	@Test
	public void constructorShouldNotAcceptWrongBaseUrl() {
		try {
			new GeocodingClient("foo");
			Assert.fail("constructor should not accept wrong base URL");
		} catch (IllegalArgumentException e) {
			// ignore
		}
	}

	@Test
	public void constructorShouldAccepthttpsBaseUrl() {
		new GeocodingClient("https://localhost:8080/geocoding");
	}

	@Test
	public void constructorShouldAccepthttpBaseUrl() {
		new GeocodingClient(HTTP_LOCALHOST_8080_GEOCODING);
	}
	@Test(expected=IllegalArgumentException.class)
	public void geocodeAddressQueryWithNullQuery() {
		GeocodingClient client = new GeocodingClient(HTTP_LOCALHOST_8080_GEOCODING);
		client.geocode(null);
	}
	
	@Test
	public void geocodeAddressQuery() {
		GeocodingClient client = new GeocodingClient(HTTP_LOCALHOST_8080_GEOCODING);
		AddressQuery query = createAddressQuery();
		AddressQuery queryWoCallback = createAddressQuery();
		queryWoCallback.setCallback(null);
		IRestClient restClient = EasyMock.createMock(IRestClient.class);
		String queryString = BeanToRestParameter.toQueryString(queryWoCallback);
		AddressResultsDto addressResultsDto = new AddressResultsDto(new ArrayList<Address>(), 5L);
		String url = HTTP_LOCALHOST_8080_GEOCODING + queryString;
		System.out.println("url=" + url);
		EasyMock.expect(restClient.get(url, AddressResultsDto.class, OutputFormat.JSON)).andReturn(addressResultsDto);
		EasyMock.replay(restClient);
		client.setRestClient(restClient);
		Assert.assertEquals(addressResultsDto,client.geocode(query));
		EasyMock.verify(restClient);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void geocodeAddressQueryToOuputStreamWithNullQuery() {
		GeocodingClient client = new GeocodingClient(HTTP_LOCALHOST_8080_GEOCODING);
		client.geocodeAndSerialize(null, new ByteArrayOutputStream());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void geocodeAddressQueryToOuputStreamWithNullOutputStream() {
		GeocodingClient client = new GeocodingClient(HTTP_LOCALHOST_8080_GEOCODING);
		client.geocodeAndSerialize(new AddressQuery("address", "FR"), null);
	}
	
	@Test
	public void geocodeAddressQueryToOuputStream() {
		GeocodingClient client = new GeocodingClient(HTTP_LOCALHOST_8080_GEOCODING);
		AddressQuery query = createAddressQuery();
		String queryString = BeanToRestParameter.toQueryString(query);
		String url = HTTP_LOCALHOST_8080_GEOCODING + queryString;
		System.out.println("url=" + url);
		IRestClient restClient = EasyMock.createMock(IRestClient.class);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		restClient.get(url, outputStream, OutputFormat.PYTHON);
		EasyMock.replay(restClient);
		client.setRestClient(restClient);
		client.geocodeAndSerialize(query, outputStream);
		EasyMock.verify(restClient);
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void geocodeAddressQueryToStringWithNullQuery() {
		GeocodingClient client = new GeocodingClient(HTTP_LOCALHOST_8080_GEOCODING);
		client.geocodeToString(null);
	}
	
	@Test
	public void geocodeAddressQueryToString() {
		GeocodingClient client = new GeocodingClient(HTTP_LOCALHOST_8080_GEOCODING);
		AddressQuery query = createAddressQuery();
		String queryString = BeanToRestParameter.toQueryString(query);
		String url = HTTP_LOCALHOST_8080_GEOCODING + queryString;
		System.out.println("url=" + url);
		IRestClient restClient = EasyMock.createMock(IRestClient.class);
		String result = "result";
		EasyMock.expect(restClient.get(url, String.class, OutputFormat.PYTHON)).andReturn(result);
		EasyMock.replay(restClient);
		client.setRestClient(restClient);
		Assert.assertEquals(result, client.geocodeToString(query));
		EasyMock.verify(restClient);
	}

	private AddressQuery createAddressQuery() {
		AddressQuery query = new AddressQuery("address", "country");
		query.setApikey("apikey");
		query.setCallback("callback");
		query.setIndent(true);
		query.setPostal(true);
		query.setFormat(OutputFormat.PYTHON);
		return query;
	}

}
