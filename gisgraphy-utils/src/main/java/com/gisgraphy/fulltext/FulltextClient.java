package com.gisgraphy.fulltext;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.rest.IRestClient;
import com.gisgraphy.rest.RestClient;
import com.gisgraphy.serializer.common.OutputFormat;
import com.gisgraphy.service.ServiceException;
import com.gisgraphy.servlet.GisgraphyServlet;

public class FulltextClient implements IFullTextSearchEngine {
	
	private String baseUrl;

	private ObjectMapper mapper = new ObjectMapper();
	
	private IRestClient restClient = new RestClient();


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
		query.withOutput(Output.withFormat(OutputFormat.JSON).withStyle(OutputStyle.FULL));
		String queryString = fulltextQueryToQueryString(query);
		
		String jsonFeed = restClient.get(baseUrl+queryString, String.class, OutputFormat.JSON);
		
		Map<String, Object> userData;
		try {
			
			userData = mapper.readValue(jsonFeed, Map.class);
		} catch (JsonParseException e) {
			throw new ServiceException(e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new ServiceException(e.getMessage(), e);
		} catch (IOException e) {
			throw new ServiceException(e.getMessage(), e);
		}
		//System.out.println(userData);
		FulltextResultsDto dto = new FulltextResultsDto();
		LinkedHashMap<String,Object> responseHeaders = (LinkedHashMap<String,Object>) userData.get("responseHeader");
		if (responseHeaders.size()>0 && responseHeaders.get("QTime")!=null){
			dto.QTime=(Integer) responseHeaders.get("QTime");
		}
		LinkedHashMap<String,Object> response = (LinkedHashMap<String,Object>) userData.get("response");
		if (response.size()>0){
			if (response.get("numFound")!=null){
				dto.numFound=(Integer) response.get("numFound");
			}
			if (response.get(" maxScore")!=null){
				dto.maxScore=(Float) response.get("maxScore");
			}
		}
		ArrayList<LinkedHashMap<String,Object>> docs = (ArrayList<LinkedHashMap<String,Object>>) response.get("docs");
		dto.resultsSize= docs.size();
		List<SolrResponseDto> results =new ArrayList<SolrResponseDto>();
		for (LinkedHashMap<String,Object> doc :docs){
			SolrResponseDto solrResponseDto = new SolrResponseDto();
		/*for (Entry<String,Object> key :doc.entrySet()){
			Entry<String,Object> entry = (Entry<String,Object>)key;
			Object key2 = entry.getKey();
			if (key2 !=null){
			System.out.println(key2+":"+entry.getValue());
			}
		}*/
			if (doc.get(FullTextFields.FEATUREID.getValue())!=null){
				solrResponseDto.feature_id=Long.valueOf(doc.get(FullTextFields.FEATUREID.getValue()).toString());
			}
			if (doc.get(FullTextFields.FEATURECLASS.getValue())!=null){
				solrResponseDto.feature_class=doc.get(FullTextFields.FEATURECLASS.getValue()).toString();
			}
			if (doc.get(FullTextFields.FEATURECODE.getValue())!=null){
				solrResponseDto.feature_code=doc.get(FullTextFields.FEATURECODE.getValue()).toString();
			}
			if (doc.get(FullTextFields.NAME.getValue())!=null){
				solrResponseDto.name=doc.get(FullTextFields.NAME.getValue()).toString();
			}
			if (doc.get(FullTextFields.NAMEASCII.getValue())!=null){
				solrResponseDto.name_ascii=doc.get(FullTextFields.NAMEASCII.getValue()).toString();
			}
			if (doc.get(FullTextFields.ELEVATION.getValue())!=null){
				solrResponseDto.elevation=(Integer)doc.get(FullTextFields.ELEVATION.getValue());
			}
			if (doc.get(FullTextFields.GTOPO30.getValue())!=null){
				solrResponseDto.gtopo30=(Integer)doc.get(FullTextFields.GTOPO30.getValue());
			}
			if (doc.get(FullTextFields.TIMEZONE.getValue())!=null){
				solrResponseDto.timezone=doc.get(FullTextFields.TIMEZONE.getValue()).toString();
			}
			if (doc.get(FullTextFields.FULLY_QUALIFIED_NAME.getValue())!=null){
				solrResponseDto.fully_qualified_name=doc.get(FullTextFields.FULLY_QUALIFIED_NAME.getValue()).toString();
			}
			if (doc.get(FullTextFields.PLACETYPE.getValue())!=null){
				solrResponseDto.placetype=doc.get(FullTextFields.PLACETYPE.getValue()).toString();
			}
			if (doc.get(FullTextFields.POPULATION.getValue())!=null){
				solrResponseDto.population=(Integer)doc.get(FullTextFields.POPULATION.getValue());
			}
			if (doc.get(FullTextFields.LAT.getValue())!=null){
				solrResponseDto.lat=(Double)doc.get(FullTextFields.LAT.getValue());
			}
			if (doc.get(FullTextFields.LONG.getValue())!=null){
				solrResponseDto.lng=(Double)doc.get(FullTextFields.LONG.getValue());
			}
			if (doc.get(FullTextFields.ADM1CODE.getValue())!=null){
				solrResponseDto.adm1_code=doc.get(FullTextFields.ADM1CODE.getValue()).toString();
			}
			if (doc.get(FullTextFields.ADM2CODE.getValue())!=null){
				solrResponseDto.adm2_code=doc.get(FullTextFields.ADM2CODE.getValue()).toString();
			}
			if (doc.get(FullTextFields.ADM3CODE.getValue())!=null){
				solrResponseDto.adm3_code=doc.get(FullTextFields.ADM3CODE.getValue()).toString();
			}
			if (doc.get(FullTextFields.ADM4CODE.getValue())!=null){
				solrResponseDto.adm4_code=doc.get(FullTextFields.ADM4CODE.getValue()).toString();
			}
			if (doc.get(FullTextFields.ADM1NAME.getValue())!=null){
				solrResponseDto.adm1_name=doc.get(FullTextFields.ADM1NAME.getValue()).toString();
			}
			if (doc.get(FullTextFields.ADM2NAME.getValue())!=null){
				solrResponseDto.adm2_name=doc.get(FullTextFields.ADM2NAME.getValue()).toString();
			}
			if (doc.get(FullTextFields.ADM3NAME.getValue())!=null){
				solrResponseDto.adm3_name=doc.get(FullTextFields.ADM3NAME.getValue()).toString();
			}
			if (doc.get(FullTextFields.ADM4NAME.getValue())!=null){
				solrResponseDto.adm4_name=doc.get(FullTextFields.ADM4NAME.getValue()).toString();
			}
			/*if (doc.get(FullTextFields.ZIPCODE.getValue())!=null){
				solrResponseDto.adm1_code=doc.get(FullTextFields.ADM1CODE.getValue()).toString();
			}*///TODO
			if (doc.get(FullTextFields.COUNTRYCODE.getValue())!=null){
				solrResponseDto.country_code=doc.get(FullTextFields.COUNTRYCODE.getValue()).toString();
			}
			if (doc.get(FullTextFields.COUNTRYNAME.getValue())!=null){
				solrResponseDto.country_name=doc.get(FullTextFields.COUNTRYNAME.getValue()).toString();
			}
			if (doc.get(FullTextFields.COUNTRY_FLAG_URL.getValue())!=null){
				solrResponseDto.country_flag_url=doc.get(FullTextFields.COUNTRY_FLAG_URL.getValue()).toString();
			}
			if (doc.get(FullTextFields.GOOGLE_MAP_URL.getValue())!=null){
				solrResponseDto.google_map_url=doc.get(FullTextFields.GOOGLE_MAP_URL.getValue()).toString();
			}
			if (doc.get(FullTextFields.YAHOO_MAP_URL.getValue())!=null){
				solrResponseDto.yahoo_map_url=doc.get(FullTextFields.YAHOO_MAP_URL.getValue()).toString();
			}
			if (doc.get(FullTextFields.CONTINENT.getValue())!=null){
				solrResponseDto.continent=doc.get(FullTextFields.CONTINENT.getValue()).toString();
			}
			if (doc.get(FullTextFields.CURRENCY_CODE.getValue())!=null){
				solrResponseDto.currency_code=doc.get(FullTextFields.CURRENCY_CODE.getValue()).toString();
			}
			if (doc.get(FullTextFields.CURRENCY_NAME.getValue())!=null){
				solrResponseDto.currency_name=doc.get(FullTextFields.CURRENCY_NAME.getValue()).toString();
			}
			if (doc.get(FullTextFields.FIPS_CODE.getValue())!=null){
				solrResponseDto.fips_code=doc.get(FullTextFields.FIPS_CODE.getValue()).toString();
			}
			if (doc.get(FullTextFields.ISOALPHA2_COUNTRY_CODE.getValue())!=null){
				solrResponseDto.isoalpha2_country_code=doc.get(FullTextFields.ISOALPHA2_COUNTRY_CODE.getValue()).toString();
			}
			if (doc.get(FullTextFields.ISOALPHA3_COUNTRY_CODE.getValue())!=null){
				solrResponseDto.isoalpha3_country_code=doc.get(FullTextFields.ISOALPHA3_COUNTRY_CODE.getValue()).toString();
			}
			if (doc.get(FullTextFields.POSTAL_CODE_MASK.getValue())!=null){
				solrResponseDto.postal_code_mask=doc.get(FullTextFields.POSTAL_CODE_MASK.getValue()).toString();
			}
			if (doc.get(FullTextFields.POSTAL_CODE_REGEX.getValue())!=null){
				solrResponseDto.postal_code_regex=doc.get(FullTextFields.POSTAL_CODE_REGEX.getValue()).toString();
			}
			if (doc.get(FullTextFields.PHONE_PREFIX.getValue())!=null){
				solrResponseDto.phone_prefix=doc.get(FullTextFields.PHONE_PREFIX.getValue()).toString();
			}
		/*	if (doc.get(FullTextFields.SPOKEN_LANGUAGES.getValue())!=null){
				solrResponseDto.spoken_languages=doc.get(FullTextFields.ADM1CODE.getValue()).toString();
			}*/
			if (doc.get(FullTextFields.TLD.getValue())!=null){
				solrResponseDto.tld=doc.get(FullTextFields.TLD.getValue()).toString();
			}
			if (doc.get(FullTextFields.CAPITAL_NAME.getValue())!=null){
				solrResponseDto.capital_name=doc.get(FullTextFields.CAPITAL_NAME.getValue()).toString();
			}
			if (doc.get(FullTextFields.LEVEL.getValue())!=null){
				solrResponseDto.level=(Integer)doc.get(FullTextFields.LEVEL.getValue());
			}
			if (doc.get(FullTextFields.LENGTH.getValue())!=null){
				solrResponseDto.length=(Double)doc.get(FullTextFields.LENGTH.getValue());
			}
			if (doc.get(FullTextFields.ONE_WAY.getValue())!=null){
				solrResponseDto.one_way=Boolean.valueOf(doc.get(FullTextFields.ONE_WAY.getValue()).toString());
			}
			if (doc.get(FullTextFields.STREET_TYPE.getValue())!=null){
				solrResponseDto.street_type=doc.get(FullTextFields.STREET_TYPE.getValue()).toString();
			}
			if (doc.get(FullTextFields.OPENSTREETMAP_ID.getValue())!=null){
				solrResponseDto.openstreetmap_id=Long.valueOf(doc.get(FullTextFields.OPENSTREETMAP_ID.getValue()).toString());
			}
			if (doc.get(FullTextFields.IS_IN.getValue())!=null){
				solrResponseDto.is_in=doc.get(FullTextFields.IS_IN.getValue()).toString();
			}
			
			
			results.add(solrResponseDto);
		}
		dto.results=results;
		return dto;
	}
	
	public void executeAndSerialize(FulltextQuery arg0, OutputStream arg1) throws ServiceException {
		throw new RuntimeException("executeQueryToDatabaseObjects is not implemented");
	}

	public String executeQueryToString(FulltextQuery arg0) throws ServiceException {
		throw new RuntimeException("executeQueryToDatabaseObjects is not implemented");
	}

	public List<?> executeQueryToDatabaseObjects(FulltextQuery query) throws ServiceException {
		throw new RuntimeException("executeQueryToDatabaseObjects is not implemented");
	}


	/*
	 * always return true
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
		addParameter(sb,FulltextQuery.LAT_PARAMETER,query.getLatitude());
		addParameter(sb,FulltextQuery.LONG_PARAMETER,query.getLongitude());
		addParameter(sb,FulltextQuery.RADIUS_PARAMETER,query.getRadius());
		addParameter(sb,FulltextQuery.ALLWORDSREQUIRED_PARAMETER,query.isAllwordsRequired());
		addParameter(sb,GisgraphyServlet.FROM_PARAMETER,query.getFirstPaginationIndex());
		addParameter(sb,GisgraphyServlet.TO_PARAMETER,query.getLastPaginationIndex());
		addParameter(sb,GisgraphyServlet.FORMAT_PARAMETER,query.getOutputFormat());
		addParameter(sb,FulltextQuery.LANG_PARAMETER,query.getOutputLanguage());
		addParameter(sb,FulltextQuery.STYLE_PARAMETER,query.getOutputStyle());
		if (query.getPlaceTypes()!=null){
			for (Class clazz : query.getPlaceTypes())
			addParameter(sb,FulltextQuery.PLACETYPE_PARAMETER,clazz.getSimpleName());
		}
		addParameter(sb,GisgraphyServlet.APIKEY_PARAMETER, query.getApikey());
					addParameter(sb,GisgraphyServlet.INDENT_PARAMETER,query.isOutputIndented());
		addParameter(sb,FulltextQuery.COUNTRY_PARAMETER,query.getCountryCode());
		addParameter(sb,FulltextQuery.SPELLCHECKING_PARAMETER,query.isSpellcheckingEnabled());
		return sb.toString();
	}
	
	private void addParameter(StringBuffer sb, String httpParameterName, Object parameterValue) {
		if (parameterValue != null) {
			sb.append(httpParameterName).append("=").append(parameterValue.toString()).append("&");
		}
	}

}
