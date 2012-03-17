package com.gisgraphy.fulltext;

import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.gisgraphy.rest.IRestClient;
import com.gisgraphy.rest.RestClient;
import com.gisgraphy.service.ServiceException;
import com.gisgraphy.servlet.GisgraphyServlet;

public class FulltextClient implements IFullTextSearchEngine {
	
	private String baseUrl;

	IRestClient restClient = new RestClient();

	/**
	 * @param baseUrl
	 *            the base url to use. example :
	 *            http://localhost:8080/geoloc/
	 */
	public FulltextClient(String baseUrl) {
		if (baseUrl == null || "".equals(baseUrl.trim())) {
			throw new IllegalArgumentException("fulltext base URL is empty or null");
		}
		try {
			new URL(baseUrl);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("fulltext base URL should be correct");
		}
		this.baseUrl = baseUrl;
	}

	public FulltextResultsDto executeQuery(FulltextQuery query) throws ServiceException {
		if (query==null){
			throw new IllegalArgumentException("can not geocode a null query");
		}
		String queryString = fulltextQueryToQueryString(query);
		return restClient.get(baseUrl+queryString, FulltextResultsDto.class, query.getOutputFormat());
	}
	
	public void executeAndSerialize(FulltextQuery arg0, OutputStream arg1) throws ServiceException {
	}

	public String executeQueryToString(FulltextQuery arg0) throws ServiceException {
		return null;
	}

	public List<?> executeQueryToDatabaseObjects(FulltextQuery query) throws ServiceException {
		throw new RuntimeException("executeQueryToDatabaseObjects is not implemented");
	}


	/* (non-Javadoc)
	 * @see com.gisgraphy.fulltext.IFullTextSearchEngine#isAlive()
	 */
	public boolean isAlive() {
		return true;
	}

	public String getURL() {
		return this.baseUrl;
	}
	
	protected String fulltextQueryToQueryString(FulltextQuery query){
		StringBuffer sb = new StringBuffer("?");
		addParameter(sb,FulltextQuery.QUERY_PARAMETER,query.getQuery());
		addParameter(sb,FulltextQuery.ALLWORDSREQUIRED_PARAMETER,query.isAllwordsRequired());
		addParameter(sb,GisgraphyServlet.FROM_PARAMETER,query.getFirstPaginationIndex());
		addParameter(sb,GisgraphyServlet.TO_PARAMETER,query.getLastPaginationIndex());
		addParameter(sb,GisgraphyServlet.FORMAT_PARAMETER,query.getOutputFormat());
		addParameter(sb,FulltextQuery.LANG_PARAMETER,query.getOutputLanguage());
		addParameter(sb,FulltextQuery.STYLE_PARAMETER,query.getOutputStyle());
		addParameter(sb,FulltextQuery.PLACETYPE_PARAMETER,query.getPlaceTypes());
		addParameter(sb,FulltextQuery.COUNTRY_PARAMETER,query.getCountryCode());
		addParameter(sb,GisgraphyServlet.INDENT_PARAMETER,query.isOutputIndented());
		addParameter(sb,FulltextQuery.SPELLCHECKING_PARAMETER,query.isSpellcheckingEnabled());
		addParameter(sb,GisgraphyServlet.APIKEY_PARAMETER, query.getApikey());
		return sb.toString();
	}
	
	private void addParameter(StringBuffer sb, String httpParameterName, Object parameterValue) {
		if (parameterValue != null) {
			sb.append(httpParameterName).append("=").append(parameterValue.toString()).append("&");
		}
	}

}
