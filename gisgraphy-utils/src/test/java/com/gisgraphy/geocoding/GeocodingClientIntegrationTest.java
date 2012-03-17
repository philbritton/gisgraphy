package com.gisgraphy.geocoding;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.addressparser.AddressQuery;
import com.gisgraphy.addressparser.AddressResultsDto;
import com.gisgraphy.serializer.common.OutputFormat;

public class GeocodingClientIntegrationTest {

	private static final String HTTP_LOCALHOST_GEOCODING_URL = "http://services.gisgraphy.com/geocoding";

	
	@Test
	public void geocodeAddressQuery() {
		GeocodingClient client = new GeocodingClient(HTTP_LOCALHOST_GEOCODING_URL);
		AddressQuery query = createAddressQuery();
		AddressResultsDto dto = client.geocode(query);
		Assert.assertNotNull(dto);
		Assert.assertEquals(10, dto.getResult().size());

	}
	
	private AddressQuery createAddressQuery() {
		AddressQuery query = new AddressQuery("75 rue des archives 75003 paris", "FR");
		query.setApikey("apikey");
		query.setFormat(OutputFormat.JSON);
		query.setIndent(true);
		query.setPostal(true);
		query.setCallback("callback");//test that callback will be set to null
		return query;
	}

}
