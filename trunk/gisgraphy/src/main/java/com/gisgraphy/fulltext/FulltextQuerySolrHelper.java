/*******************************************************************************
 *   Gisgraphy Project 
 * 
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 * 
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *   Lesser General Public License for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA
 * 
 *  Copyright 2008  Gisgraphy project 
 *  David Masclet <davidmasclet@gisgraphy.com>
 *  
 *  
 *******************************************************************************/
package com.gisgraphy.fulltext;

import java.util.Locale;

import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.params.ModifiableSolrParams;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.geoloc.entity.Street;
import com.gisgraphy.domain.valueobject.Constants;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.fulltext.spell.SpellCheckerConfig;
import com.gisgraphy.serializer.common.OutputFormat;

/**
 * 
 * usefullmethod to process fulltext query by solr
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 * 
 */
public class FulltextQuerySolrHelper {

	private static OutputStyleHelper outputStyleHelper = new OutputStyleHelper();

	private final static String IS_IN_SENTENCE = " "+FullTextFields.IS_IN.getValue()+"^0.8 "+FullTextFields.IS_IN_PLACE.getValue()+"^0.8  "+FullTextFields.IS_IN_ADM.getValue()+"^0.5 "+FullTextFields.IS_IN_ZIP.getValue()+"^0.8 "+FullTextFields.IS_IN_CITIES.getValue()+"^0.8 ";
	protected static final String NESTED_QUERY_TEMPLATE =                   "_query_:\"{!dismax qf='all_name^1.1 iso_all_name^1 zipcode^1.2 all_adm1_name^0.5 all_adm2_name^0.5 all_country_name^0.5 %s' pf=name^1.1 bq='%s population^2' bf='pow(population,0.4) pow(city_population,0.4)'}%s\"";
	//below the all_adm1_name^0.5 all_adm2_name^0.5 has been kept
	//protected static final String NESTED_QUERY_TEMPLATE = "_query_:\"{!dismax qf='all_name^1.1 iso_all_name^1 zipcode^1.1 all_adm1_name^0.5 all_adm2_name^0.5 all_country_name^0.5 %s' pf=name^1.1 bf=population^2.0}%s\"";
	// protected static final String NESTED_QUERY_INTEXT_BASIC_TEMPLATE=
	// "_query_:\"{!dismax qf='name^1.1 zipcode^1.1'  mm='1<-100%% 2<-50%% 3<-0%%' bq='_val_:\\\"pow(population,0.3)\\\"' }%s\"";
	protected static final String NESTED_QUERY_NOT_ALL_WORDS_REQUIRED_TEMPLATE = "_query_:\"{!dismax qf=' all_name^1.1 iso_all_name^1 zipcode^1.2 all_adm1_name^0.5 all_adm2_name^0.5 %s' mm='1<1 2<1 3<1'   pf='all_adm1_name all_adm2_name' ps=6 bq='%s population^2' bf='pow(population,0.3) pow(city_population,0.3)' }%s\"";
	protected static final String CITY_BOOST_QUERY="placetype:city^16";
	// we need to consider adm1name for andora and brooklin
	protected static final String NESTED_QUERY_NUMERIC_TEMPLATE =          "_query_:\"{!dismax qf='feature_id^1.1 openstreetmap_id^1.1 zipcode^1.2 pf=name^1.1' bf=population^2.0}%s\"";

	protected static final String GEOLOC_QUERY_TEMPLATE = "_query_:\"{!bbox "
			+ Constants.SPATIAL_FIELD_PARAMETER + "="
			+ GisFeature.LOCATION_COLUMN_NAME + " " + Constants.POINT_PARAMETER
			+ "=%f,%f " + Constants.DISTANCE_PARAMETER + "=%f}\"";

	/**
	 * @return A Representation of all the needed parameters
	 */
	public static ModifiableSolrParams parameterize(FulltextQuery query) {
	ModifiableSolrParams parameters = new ModifiableSolrParams();
	
	parameters.set(Constants.INDENT_PARAMETER, query.isOutputIndented() ? "on"
		: "off");
	parameters.set(Constants.ECHOPARAMS_PARAMETER, "none");
	parameters.set(Constants.START_PARAMETER, String
		.valueOf(query.getFirstPaginationIndex() - 1));// sub
	// 1
	// because
	// solr
	// start
	// at 0
	parameters.set(Constants.ROWS_PARAMETER, String.valueOf(query.getPagination()
		.getMaxNumberOfResults()));
	if (query.getOutputFormat() == OutputFormat.ATOM) {
	    parameters.set(Constants.STYLESHEET_PARAMETER,
		    Constants.ATOM_STYLESHEET);
	} else if (query.getOutputFormat() == OutputFormat.GEORSS) {
	    parameters.set(Constants.STYLESHEET_PARAMETER,
		    Constants.GEORSS_STYLESHEET);
	}
	parameters.set(Constants.OUTPUT_FORMAT_PARAMETER, query.getOutputFormat()
		.getParameterValue());
	// force Medium style if ATOM or Geo RSS
	if (query.getOutputFormat() == OutputFormat.ATOM
		|| query.getOutputFormat() == OutputFormat.GEORSS) {
	    parameters.set(Constants.FL_PARAMETER,outputStyleHelper.getFulltextFieldList(OutputStyle.MEDIUM, query.getOutput().getLanguageCode()));
	} else {
	    parameters.set(Constants.FL_PARAMETER, outputStyleHelper.getFulltextFieldList(query.getOutput()));
	}
	String trimedCountryCode = query.getCountryCode()!=null?query.getCountryCode().trim():null;
	boolean isFeatureIdRequest = query.getQuery().startsWith(FullTextFields.FEATUREID.getValue()+":");
	boolean isAdvancedQuery = ((trimedCountryCode!=null && !"".equals(trimedCountryCode)) || query.getPlaceTypes() != null || !query.isAllwordsRequired() || query.getPoint()!=null || isFeatureIdRequest);
	boolean isNumericQuery = isNumericQuery(query.getQuery());
	StringBuffer querybuffer ;
	if (isAdvancedQuery){
		String boost_city=query.getPlaceTypes() == null?CITY_BOOST_QUERY:"";
		String is_in = isStreetQuery(query)?IS_IN_SENTENCE:"";
	    if (isNumericQuery){
	    	querybuffer = new StringBuffer(String.format(NESTED_QUERY_NUMERIC_TEMPLATE,query.getQuery()));
	    } else if (isFeatureIdRequest){
	    	querybuffer= new StringBuffer(query.getQuery());
	    } else if (!query.isAllwordsRequired()){
	    	querybuffer = new StringBuffer(String.format(NESTED_QUERY_NOT_ALL_WORDS_REQUIRED_TEMPLATE,is_in,boost_city,query.getQuery()));
	    } else {
	    	querybuffer = new StringBuffer(String.format(NESTED_QUERY_TEMPLATE,is_in,boost_city,query.getQuery()));
	    }
	    parameters.set(Constants.QT_PARAMETER, Constants.SolrQueryType.advanced
			.toString());
	    if (query.getCountryCode() != null && !query.getCountryCode().trim().equals("")) {
	    	querybuffer.append(" AND ").append(FullTextFields.COUNTRYCODE.getValue()+":"+query.getCountryCode());

		}
	    if (query.getPoint() != null) {
	    	querybuffer.append(" AND ").append(String.format(Locale.US,GEOLOC_QUERY_TEMPLATE,query.getPoint().getY(),query.getPoint().getX(),query.getRadius()/1000));

		}
	    if (query.getPlaceTypes() != null && containsOtherThingsThanNull(query.getPlaceTypes())) {
	    	 querybuffer.append(" AND (");
	    	 boolean firstAppend=false;
	    	for (int i=0;i< query.getPlaceTypes().length;i++){
	    		if (query.getPlaceTypes()[i] != null){
	    			if (firstAppend){
	    				querybuffer.append(" OR ");
	    			}
	    		querybuffer.append(FullTextFields.PLACETYPE.getValue()+":"+query.getPlaceTypes()[i].getSimpleName());
	    		firstAppend=true;
	    	}
		}
	    	querybuffer.append(") ");
	    }
	    parameters.set(Constants.QUERY_PARAMETER, querybuffer.toString());
	}  else if (isNumericQuery) {
	    parameters.set(Constants.QT_PARAMETER, Constants.SolrQueryType.numeric
			.toString());
	    parameters.set(Constants.QUERY_PARAMETER, query.getQuery());
	} else {
	    // we overide the query type
	    /*parameters.set(Constants.QT_PARAMETER,
		    Constants.SolrQueryType.standard.toString());
	    parameters.set(Constants.QUERY_PARAMETER, query.getQuery());*/
		String boost_city=CITY_BOOST_QUERY;//we force boost to city because it is not a 'Typed' query
		String is_in = isStreetQuery(query)?IS_IN_SENTENCE:"";
	    if (!query.isAllwordsRequired()){
	    	querybuffer = new StringBuffer(String.format(NESTED_QUERY_NOT_ALL_WORDS_REQUIRED_TEMPLATE,is_in,boost_city,query.getQuery()));
	    } else {
	    	querybuffer = new StringBuffer(String.format(NESTED_QUERY_TEMPLATE,is_in,boost_city,query.getQuery()));
		
	    }
	    parameters.set(Constants.QT_PARAMETER, Constants.SolrQueryType.advanced
			.toString());
	    parameters.set(Constants.QUERY_PARAMETER, querybuffer.toString());
	}

	
	
	
	if (SpellCheckerConfig.enabled && query.hasSpellChecking() && !isNumericQuery){
		parameters.set(Constants.SPELLCHECKER_ENABLED_PARAMETER,"true");
		if(isAdvancedQuery){
		    parameters.set(Constants.SPELLCHECKER_QUERY_PARAMETER, query.getQuery());
		}
		parameters.set(Constants.SPELLCHECKER_COLLATE_RESULTS_PARAMETER,SpellCheckerConfig.collateResults);
		parameters.set(Constants.SPELLCHECKER_NUMBER_OF_SUGGESTION_PARAMETER,SpellCheckerConfig.numberOfSuggestion);
		parameters.set(Constants.SPELLCHECKER_DICTIONARY_NAME_PARAMETER,SpellCheckerConfig.spellcheckerDictionaryName.toString());
	}

	return parameters;
    }

	private static boolean containsOtherThingsThanNull(Class[] array) {
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				if (array[i] != null) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean isNumericQuery(String queryString) {
		try {
			Integer.parseInt(queryString);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	protected static boolean isStreetQuery(FulltextQuery query) {
		if (query.getPlaceTypes() != null
				&& containsOtherThingsThanNull(query.getPlaceTypes())) {
			for (int i = 0; i < query.getPlaceTypes().length; i++) {
				if (query.getPlaceTypes()[i] != null
						&& query.getPlaceTypes()[i] == Street.class) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @return A query string for the specified parameter (starting with '?')
	 *         the name of the parameters are defined in {@link Constants}
	 */
	public static String toQueryString(FulltextQuery fulltextQuery) {
		return ClientUtils.toQueryString(parameterize(fulltextQuery), false);
	}
}
