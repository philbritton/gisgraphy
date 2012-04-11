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

/**
 * All the declared fields in Solr
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public enum FullTextFields {
	FEATUREID("feature_id"),
	FEATURECLASS("feature_class"),
	FEATURECODE("feature_code"),
	NAME("name"),
	NAMEASCII("name_ascii"),
	ELEVATION("elevation"),
	GTOPO30("gtopo30"),
	TIMEZONE("timezone"),
	ALL_NAME("all_name"),
	FULLY_QUALIFIED_NAME("fully_qualified_name"),
	PLACETYPE("placetype"),
	POPULATION("population"),
	CITY_POPULATION("city_population"),
	LAT("lat"),
	LONG("lng"),
	ADM1CODE("adm1_code"),
	ADM2CODE("adm2_code"),
	ADM3CODE("adm3_code"),
	ADM4CODE("adm4_code"),
	ADM1NAME("adm1_name"),
	ALL_ADM1_NAME("all_adm1_name"),
	ADM2NAME("adm2_name"),
	ALL_ADM2_NAME("all_adm2_name"),
	ADM3NAME("adm3_name"),
	ADM4NAME("adm4_name"),
	//City only
	ZIPCODE("zipcode"),
	LOCATION("location"),
	//end City only
	COUNTRYCODE("country_code"),
	COUNTRYNAME("country_name"),
	ALL_COUNTRY_NAME("all_country_name"),
	ALTERNATE_NAME_SUFFIX("_alternate"),
	ALTERNATE_NAME_DYNA_SUFFIX("_alternate_"),
	COUNTRY_FLAG_URL("country_flag_url"),
	GOOGLE_MAP_URL("google_map_url"),
	YAHOO_MAP_URL("yahoo_map_url"),
	SPELLCHECK("spellcheck"),
	SPELLCHECK_SUGGESTIONS("suggestions"),
	SPELLCHECK_SUGGESTION("suggestion"),
	//Country only
	CONTINENT("continent"),
	CURRENCY_CODE("currency_code"),
	CURRENCY_NAME("currency_name"),
	FIPS_CODE("fips_code"),
	ISOALPHA2_COUNTRY_CODE("isoalpha2_country_code"),
	ISOALPHA3_COUNTRY_CODE("isoalpha3_country_code"),
	POSTAL_CODE_MASK("postal_code_mask"),
	POSTAL_CODE_REGEX("postal_code_regex"),
	PHONE_PREFIX("phone_prefix"),
	SPOKEN_LANGUAGES("spoken_languages"),
	TLD("tld"),
	CAPITAL_NAME("capital_name"),
	AREA("area"),
	//end country only
	
	
	//Adm Only
	LEVEL("level"),
	//end Adm only
	
	//street only
	LENGTH("length"),
	ONE_WAY("one_way"),
	STREET_TYPE("street_type"),
	OPENSTREETMAP_ID("openstreetmap_id"),
	IS_IN("is_in");
	//end of street only
	
	/** The value of the fulltext field in the schema.xml */
	private final String value;

	/** Default constructor */
	private FullTextFields(String value) {
		this.value = value;
	}

	/** Get the value of the fulltext field in the schema.xml */
	public String getValue() {
		return this.value;
	}

}
