package com.gisgraphy.geocoding;

import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.gisgraphy.addressparser.Address;
import com.gisgraphy.addressparser.AddressQuery;
import com.gisgraphy.addressparser.AddressResultsDto;
import com.gisgraphy.rest.BeanToRestParameter;
import com.gisgraphy.rest.IRestClient;
import com.gisgraphy.rest.RestClient;
import com.gisgraphy.serializer.common.OutputFormat;

/**
 * 
 * Geocoding client
 * Can be instanciate once as a singleton<br/>
 * For premium services don't forget to set apikey.<br/>
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 *
 */
public class GeocodingClient implements IGeocodingService{
	
	private String baseUrl;
	IRestClient restClient = new RestClient();

	/**
	 * @param baseUrl the base url to use. example : 
	 * http://localhost:8080/geocoding/
	 */
	public GeocodingClient(String baseUrl) {
		if (baseUrl==null || "".equals(baseUrl.trim())){
			throw new IllegalArgumentException("Geocoding base URL is empty or null");
		}
		try {
			new URL(baseUrl);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("baseurl should be correct");
		}
		this.baseUrl = baseUrl;
	}

	/* (non-Javadoc)
	 * @see com.gisgraphy.geocoding.IGeocodingService#geocode(com.gisgraphy.addressparser.AddressQuery)
	 */
	public AddressResultsDto geocode(AddressQuery query) throws GeocodingException {
		if (query==null){
			throw new IllegalArgumentException("can not geocode a null query");
		}
		query.setCallback(null);
		String queryString = BeanToRestParameter.toQueryString(query);
		return restClient.get(baseUrl+queryString, AddressResultsDto.class, OutputFormat.JSON);
	}

	/* (non-Javadoc)
	 * @see com.gisgraphy.geocoding.IGeocodingService#geocodeAndSerialize(com.gisgraphy.addressparser.AddressQuery, java.io.OutputStream)
	 */
	public void geocodeAndSerialize(AddressQuery query, OutputStream outputStream) throws GeocodingException {
		if (query==null){
			throw new IllegalArgumentException("can not geocode a null query");
		}
		if (outputStream==null){
			throw new IllegalArgumentException("can not serialize to a null outputstream");
		}
		String queryString = BeanToRestParameter.toQueryString(query);
		restClient.get(baseUrl+queryString, outputStream, query.getFormat());
	}

	/* (non-Javadoc)
	 * @see com.gisgraphy.geocoding.IGeocodingService#geocodeToString(com.gisgraphy.addressparser.AddressQuery)
	 */
	public String geocodeToString(AddressQuery query) throws GeocodingException {
		if (query==null){
			throw new IllegalArgumentException("can not geocode a null query");
		}
		String queryString = BeanToRestParameter.toQueryString(query);
		return restClient.get(baseUrl+queryString, String.class, query.getFormat());
	}

	/* (non-Javadoc)
	 * @see com.gisgraphy.geocoding.IGeocodingService#geocode(com.gisgraphy.addressparser.Address, java.lang.String)
	 */
	public AddressResultsDto geocode(Address address, String countryCode) throws GeocodingException {
		throw new RuntimeException("not implemented !");
	}

	public void setRestClient(IRestClient restClient) {
		this.restClient = restClient;
	}
	
	  
  

}
