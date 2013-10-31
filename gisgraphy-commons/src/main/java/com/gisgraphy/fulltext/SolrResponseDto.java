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

import java.util.List;
import java.util.Map;

import com.gisgraphy.street.HouseNumberDto;

/**
 * Java Dto for a solr fulltext response. it is used by
 * {@link FulltextResultsDto}
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 * 
 */
public class SolrResponseDto {

    /**
     * Used by cglib, prefer to use {@link solrResponseDtoBuilder)}
     */
    public SolrResponseDto() {
	super();
    }

  
   // @Transient
    protected Double distance;
    
    protected String name;
    protected List<String> name_alternates;
    protected Map<String, List<String>> name_alternates_localized;

    protected Long feature_id;
    protected String feature_class;
    protected String feature_code;
    protected String name_ascii;
    protected Integer elevation;
    protected Integer gtopo30;
    protected String timezone;
    protected String fully_qualified_name;
    protected String placetype;
    protected Integer population;
    protected Double lat;
    protected Double lng;
    protected String adm1_code;
    protected String adm2_code;
    protected String adm3_code;
    protected String adm4_code;
    
    //country specific fields
    protected String continent;
    protected String currency_code;
    protected String currency_name;
    protected String fips_code;
    protected String isoalpha2_country_code;
    protected String isoalpha3_country_code;
   

    protected String postal_code_mask;
    protected String postal_code_regex;
    protected String phone_prefix;
    protected List<String> spoken_languages;
    protected String tld;
    protected String capital_name;
    protected Double area;
    
    //Adm only
    protected Integer level;

    protected String adm1_name;
    protected List<String> adm1_names_alternate;
    protected Map<String, List<String>> adm1_names_alternate_localized;

    protected String adm2_name;
    protected List<String> adm2_names_alternate;
    protected Map<String, List<String>> adm2_names_alternate_localized;

    protected String adm3_name;
    protected String adm4_name;
    protected List<String> zipcodes;
    protected String country_code;

    protected String country_name;
    protected List<String> country_names_alternate;
    protected Map<String, List<String>> country_names_alternate_localized;

    protected String country_flag_url;
    protected String google_map_url;
    protected String yahoo_map_url;
    protected String openstreetmap_map_url;
    
    protected boolean one_way;
    protected Double length;
    protected String  street_type;
    protected Long openstreetmap_id;
    protected String is_in;
    protected String is_in_place;
    protected String is_in_adm;
    protected String is_in_zip;
    protected String fully_qualified_address;
    protected List<HouseNumberDto> house_numbers;
    protected boolean municipality;
    protected String amenity;
   

	/**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @return the name_alternates
     */
    public List<String> getName_alternates() {
	return name_alternates;
    }

    /**
     * @return the name_alternates_localized
     */
    public Map<String, List<String>> getName_alternates_localized() {
	return name_alternates_localized;
    }

    /**
     * @return the feature_id
     */
    public Long getFeature_id() {
	return feature_id;
    }

    /**
     * @return the feature_class
     */
    public String getFeature_class() {
	return feature_class;
    }

    /**
     * @return the feature_code
     */
    public String getFeature_code() {
	return feature_code;
    }

    /**
     * @return the name_ascii
     */
    public String getName_ascii() {
	return name_ascii;
    }

    /**
     * @return the elevation
     */
    public Integer getElevation() {
	return elevation;
    }

    /**
     * @return the gtopo30
     */
    public Integer getGtopo30() {
	return gtopo30;
    }

    /**
     * @return the timezone
     */
    public String getTimezone() {
	return timezone;
    }

    /**
     * @return the fully_qualified_name
     */
    public String getFully_qualified_name() {
	return fully_qualified_name;
    }

    /**
     * @return the placetype
     */
    public String getPlacetype() {
	return placetype;
    }

    /**
     * @return the population
     */
    public Integer getPopulation() {
	return population;
    }

    /**
     * @return the lat
     */
    public Double getLat() {
	return lat;
    }

    /**
     * @return the lng
     */
    public Double getLng() {
	return lng;
    }

    /**
     * @return the adm1_code
     */
    public String getAdm1_code() {
	return adm1_code;
    }

    /**
     * @return the adm2_code
     */
    public String getAdm2_code() {
	return adm2_code;
    }

    /**
     * @return the adm3_code
     */
    public String getAdm3_code() {
	return adm3_code;
    }

    /**
     * @return the adm4_code
     */
    public String getAdm4_code() {
	return adm4_code;
    }

    /**
     * @return the adm1_name
     */
    public String getAdm1_name() {
	return adm1_name;
    }

    /**
     * @return the adm1_names_alternate
     */
    public List<String> getAdm1_names_alternate() {
	return adm1_names_alternate;
    }

    /**
     * @return the adm1_names_alternate_localized
     */
    public Map<String, List<String>> getAdm1_names_alternate_localized() {
	return adm1_names_alternate_localized;
    }

    /**
     * @return the adm2_name
     */
    public String getAdm2_name() {
	return adm2_name;
    }

    /**
     * @return the adm2_names_alternate
     */
    public List<String> getAdm2_names_alternate() {
	return adm2_names_alternate;
    }

    /**
     * @return the adm2_names_alternate_localized
     */
    public Map<String, List<String>> getAdm2_names_alternate_localized() {
	return adm2_names_alternate_localized;
    }

    /**
     * @return the adm3_name
     */
    public String getAdm3_name() {
	return adm3_name;
    }

    /**
     * @return the adm4_name
     */
    public String getAdm4_name() {
	return adm4_name;
    }

    /**
     * @return the zipcode
     */
    public List<String> getZipcodes() {
	return zipcodes;
    }

    /**
     * @return the country_code
     */
    public String getCountry_code() {
	return country_code;
    }

    /**
     * @return the country_name
     */
    public String getCountry_name() {
	return country_name;
    }

    /**
     * @return the country_names_alternate
     */
    public List<String> getCountry_names_alternate() {
	return country_names_alternate;
    }

    /**
     * @return the country_names_alternate_localized
     */
    public Map<String, List<String>> getCountry_names_alternate_localized() {
	return country_names_alternate_localized;
    }

    /**
     * @return the country_flag_url
     */
    public String getCountry_flag_url() {
	return country_flag_url;
    }

    /**
     * @return the google_map_url
     */
    public String getGoogle_map_url() {
	return google_map_url;
    }

    /**
     * @return the yahoo_map_url
     */
    public String getYahoo_map_url() {
	return yahoo_map_url;
    }

    public String getOpenstreetmap_map_url() {
		return openstreetmap_map_url;
	}

	/**
     * @return the continent
     */
    public String getContinent() {
        return continent;
    }

    /**
     * @return the currency_code
     */
    public String getCurrency_code() {
        return currency_code;
    }

    /**
     * @return the currency_name
     */
    public String getCurrency_name() {
        return currency_name;
    }

    /**
     * @return the fips_code
     */
    public String getFips_code() {
        return fips_code;
    }

    /**
     * @return the isoalpha2_country_code
     */
    public String getIsoalpha2_country_code() {
        return isoalpha2_country_code;
    }

    /**
     * @return the isoalpha3_country_code
     */
    public String getIsoalpha3_country_code() {
        return isoalpha3_country_code;
    }

    /**
     * @return the postal_code_mask
     */
    public String getPostal_code_mask() {
        return postal_code_mask;
    }

    /**
     * @return the postal_code_regex
     */
    public String getPostal_code_regex() {
        return postal_code_regex;
    }

    /**
     * @return the phone_prefix
     */
    public String getPhone_prefix() {
        return phone_prefix;
    }

    /**
     * @return the spoken_languages
     */
    public List<String> getSpoken_languages() {
        return spoken_languages;
    }

    /**
     * @return the tld
     */
    public String getTld() {
        return tld;
    }

    /**
     * @return the capital_name
     */
    public String getCapital_name() {
        return capital_name;
    }

    /**
     * @return the area
     */
    public Double getArea() {
        return area;
    }
    
    public Integer getLevel() {
        return level;
    }

    public Boolean getOne_way() {
        return one_way;
    }

    public Double getLength() {
        return length;
    }

    public String getStreet_type() {
        return street_type;
    }

    /**
     * @return the openstreetmapId
     */
    public Long getOpenstreetmap_id() {
	return openstreetmap_id;
    }

    /**
     * @return the is_in
     */
    public String getIs_in() {
	return is_in;
    }

    
    
  /**
	 * @return the is_in_place
	 */
	public String getIs_in_place() {
		return is_in_place;
	}

	/**
	 * @return the is_in_adm
	 */
	public String getIs_in_adm() {
		return is_in_adm;
	}

	/**
	 * @return the is_in_zip
	 */
	public String getIs_in_zip() {
		return is_in_zip;
	}

	/**
	 * @return the fully_qualified_address
	 */
	public String getFully_qualified_address() {
		return fully_qualified_address;
	}

	//  @Transient
    public Double getDistance() {
	return distance;
    }

    public void setDistance(Double distance) {
	this.distance = distance;
    }
    
    protected void setFeature_id(Long feature_id) {
        this.feature_id = feature_id;
    }
    
    public List<HouseNumberDto> getHouse_numbers() {
		return house_numbers;
	}

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((feature_id == null) ? 0 : feature_id.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	SolrResponseDto other = (SolrResponseDto) obj;
	if (feature_id == null) {
	    if (other.feature_id != null)
		return false;
	} else if (!feature_id.equals(other.feature_id))
	    return false;
	return true;
    }

	public boolean isMunicipality() {
		return municipality;
	}

	public String getAmenity() {
		return amenity;
	}

   

}
