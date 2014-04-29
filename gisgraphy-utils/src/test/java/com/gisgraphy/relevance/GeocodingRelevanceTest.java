package com.gisgraphy.relevance;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.addressparser.Address;
import com.gisgraphy.addressparser.AddressQuery;
import com.gisgraphy.addressparser.AddressResultsDto;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.fulltext.FulltextClient;
import com.gisgraphy.fulltext.FulltextQuery;
import com.gisgraphy.fulltext.FulltextResultsDto;
import com.gisgraphy.geocoding.GeocodingClient;
import com.gisgraphy.serializer.common.OutputFormat;

public class GeocodingRelevanceTest {

	private static final String HTTP_LOCALHOST_GEOCODING_URL = "http://services.gisgraphy.com/geocoding";
	String[][] addresses = {
			{"paris","FR","false","paris",null},
			{"rue du caire paris","FR","false","rue du caire","paris",null}
	};

	public void executeSearch(String text,String countryCode,String postal,String nameToTest,String isInToTest) {
		GeocodingClient client = new GeocodingClient(HTTP_LOCALHOST_GEOCODING_URL);
		AddressQuery query = createAddressQuery(text,countryCode,postal);
		AddressResultsDto dto = client.geocode(query);
		Assert.assertNotNull(dto);
		Assert.assertTrue("there is no results for fulltext search '"+text+"'",dto.getResult().size()>=1);
		Address address = dto.getResult().get(0);
		Assert.assertNotNull("address is null",address);
		System.out.println(address);
		if (nameToTest!=null){
			if (address.getName()==null){
				Assert.fail("the name is null but "+nameToTest+ " was expected");
			}
			Assert.assertEquals("the name is not correct for geocoding query "+text,nameToTest.toLowerCase(),address.getName().toLowerCase());
		}
		if (isInToTest!=null){
			if (address.getCity()==null){
				Assert.fail("the city is null but "+isInToTest+ " was expected");
			}
			Assert.assertEquals("the city is not correct for geocoding query '"+text+"'",isInToTest.toLowerCase(),address.getCity().toLowerCase());
		}
		

	}

	@Test
	public void testRelevance() {
		for (String[] address:addresses){
			executeSearch(address[0], address[1], address[2],address[3],address[4]);
		}
	}
	
	@Test
	public void geocodeAddressQuery() {
	

	}
	
	private AddressQuery createAddressQuery(String text, String countryCode,String postal) {
		AddressQuery query = new AddressQuery(text, countryCode);
		query.setApikey("apikey");
		query.setFormat(OutputFormat.JSON);
		query.setIndent(true);
		if (postal!=null){
			query.setPostal(true);
		}
		return query;
	}

}
