package com.gisgraphy.geocoloc;

import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.gisgraphy.geoloc.GeolocQuery;
import com.gisgraphy.geoloc.GeolocResultsDto;
import com.gisgraphy.rest.IRestClient;
import com.gisgraphy.rest.RestClient;
import com.gisgraphy.serializer.common.OutputFormat;
import com.gisgraphy.service.ServiceException;
import com.gisgraphy.servlet.GisgraphyServlet;

public class GeolocClient implements IGeolocSearchEngine {

	private String baseUrl;

	IRestClient restClient = new RestClient();

	/**
	 * @param baseUrl
	 *            the base url to use. example :
	 *            http://localhost:8080/geoloc/
	 */
	public GeolocClient(String baseUrl) {
		if (baseUrl == null || "".equals(baseUrl.trim())) {
			throw new IllegalArgumentException("geoloc base URL is empty or null");
		}
		try {
			new URL(baseUrl);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("geoloc base URL should be correct");
		}
		this.baseUrl = baseUrl;
	}

	/* (non-Javadoc)
	 * @see com.gisgraphy.service.IQueryProcessor#executeAndSerialize(com.gisgraphy.service.AbstractGisQuery, java.io.OutputStream)
	 */
	public void executeAndSerialize(GeolocQuery query, OutputStream outputStream) throws ServiceException {
		if (query==null){
			throw new IllegalArgumentException("can not execute a null query");
		}
		if (outputStream==null){
			throw new IllegalArgumentException("can not serialize to a null outputStream");
		}
		String queryString = geolocQuerytoQueryString(query);
		restClient.get(baseUrl+queryString, outputStream, query.getOutputFormat());
	}

	/* (non-Javadoc)
	 * @see com.gisgraphy.service.IQueryProcessor#executeQueryToString(com.gisgraphy.service.AbstractGisQuery)
	 */
	public String executeQueryToString(GeolocQuery query) throws ServiceException {
		if (query==null){
			throw new IllegalArgumentException("can not geocode a null query");
		}
		String queryString = geolocQuerytoQueryString(query);
		return restClient.get(baseUrl+queryString, String.class, query.getOutputFormat());
	}

	/* (non-Javadoc)
	 * @see com.gisgraphy.geocoloc.service.IGeolocSearchEngine#executeQuery(com.gisgraphy.domain.geoloc.service.geoloc.GeolocQuery)
	 */
	public GeolocResultsDto executeQuery(GeolocQuery query) throws ServiceException {
		if (query==null){
			throw new IllegalArgumentException("can not geocode a null query");
		}
		query.withCallback(null);//force callback to null
		String queryString = geolocQuerytoQueryString(query);
		return restClient.get(baseUrl + queryString , GeolocResultsDto.class, OutputFormat.JSON);
	}

	protected String geolocQuerytoQueryString(GeolocQuery query) {
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
