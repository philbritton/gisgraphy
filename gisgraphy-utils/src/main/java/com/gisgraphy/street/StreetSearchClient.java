package com.gisgraphy.street;

import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.gisgraphy.domain.valueobject.StreetSearchResultsDto;
import com.gisgraphy.geoloc.GeolocQuery;
import com.gisgraphy.rest.IRestClient;
import com.gisgraphy.rest.RestClient;
import com.gisgraphy.serializer.common.OutputFormat;
import com.gisgraphy.service.ServiceException;
import com.gisgraphy.servlet.GisgraphyServlet;

public class StreetSearchClient implements IStreetSearchEngine{
	
	private String baseUrl;

	IRestClient restClient = new RestClient();
	
	/**
	 * @param baseUrl
	 *            the base url to use. example :
	 *            http://localhost:8080/street/
	 */
	public StreetSearchClient(String baseUrl) {
		if (baseUrl == null || "".equals(baseUrl.trim())) {
			throw new IllegalArgumentException("street base URL is empty or null");
		}
		try {
			new URL(baseUrl);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("street should be correct");
		}
		this.baseUrl = baseUrl;
	}

	public void executeAndSerialize(StreetSearchQuery query, OutputStream outputStream) throws ServiceException {
		if (query==null){
			throw new IllegalArgumentException("can not execute a null query");
		}
		if (outputStream==null){
			throw new IllegalArgumentException("can not serialize to a null outputStream");
		}
		String queryString = streetsearchQuerytoQueryString(query);
		restClient.get(baseUrl+queryString, outputStream, query.getOutputFormat());
		
	}

	public String executeQueryToString(StreetSearchQuery query) throws ServiceException {
		if (query==null){
			throw new IllegalArgumentException("can not geocode a null query");
		}
		String queryString = streetsearchQuerytoQueryString(query);
		return restClient.get(baseUrl+queryString, String.class, query.getOutputFormat());
	}

	public StreetSearchResultsDto executeQuery(StreetSearchQuery query) throws ServiceException {
		if (query==null){
			throw new IllegalArgumentException("can not geocode a null query");
		}
		query.withCallback(null);//force callback to null
		String queryString = streetsearchQuerytoQueryString(query);
		return restClient.get(baseUrl + queryString , StreetSearchResultsDto.class, OutputFormat.JSON);
	}
	
	protected String streetsearchQuerytoQueryString(StreetSearchQuery query) {
		StringBuffer sb = new StringBuffer("?");
		addParameter(sb, GeolocQuery.LAT_PARAMETER, query.getLatitude());
		addParameter(sb, GeolocQuery.LONG_PARAMETER, query.getLongitude());
		addParameter(sb, GeolocQuery.RADIUS_PARAMETER, query.getRadius());
		addParameter(sb, GisgraphyServlet.FROM_PARAMETER, query.getFirstPaginationIndex());
		addParameter(sb, GisgraphyServlet.TO_PARAMETER, query.getLastPaginationIndex());
		addParameter(sb, GeolocQuery.CALLBACK_PARAMETER, query.getCallback());
		addParameter(sb, GisgraphyServlet.FORMAT_PARAMETER, query.getOutputFormat());
		if (query.getPlaceType()!=null){
			addParameter(sb, GeolocQuery.PLACETYPE_PARAMETER, query.getPlaceType().getSimpleName());
		}
		addParameter(sb, GeolocQuery.DISTANCE_PARAMETER, query.hasDistanceField());
		addParameter(sb, GisgraphyServlet.INDENT_PARAMETER, query.isOutputIndented());
		addParameter(sb, GisgraphyServlet.APIKEY_PARAMETER, query.getApikey());
		addParameter(sb, StreetSearchQuery.STREETTYPE_PARAMETER, query.getStreetType());
		addParameter(sb, StreetSearchQuery.NAME_PARAMETER, query.getName());
		addParameter(sb, StreetSearchQuery.ONEWAY_PARAMETER, query.getOneWay());
		return sb.toString();

	}

	private void addParameter(StringBuffer sb, String httpParameterName, Object parameterValue) {
		if (parameterValue != null) {
			sb.append(httpParameterName).append("=").append(parameterValue.toString()).append("&");
		}
	}

	public void setRestClient(IRestClient restClient) {
		this.restClient = restClient;
	}

}
