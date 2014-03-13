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
package com.gisgraphy.domain.geoloc.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.gisgraphy.helper.IntrospectionIgnoredField;
import com.gisgraphy.street.StreetType;

/**
 * Represents a {@link Street}.
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Street extends GisFeature {

    private boolean oneWay = false;
    private StreetType streetType;
    private Double length;
    //private Long openstreetmapId;
    //those fields are only used to sync the fulltext engine,
    //there are not used to be stored in Datastore
    
    private String isIn;
    private String isInPlace;
    private String isInAdm;
    private Set<String> isInZip;
    private String fullyQualifiedAddress;
    private SortedSet<HouseNumber> houseNumbers;
    
    /**
     * This field is only for relevance and allow to search for street<->cities in 
     * many alternateNames. It is not in stored
     */
    @IntrospectionIgnoredField
    private Set<String> isInCityAlternateNames;

    

	/*public Long getOpenstreetmapId() {
	return openstreetmapId;
    }

    public void setOpenstreetmapId(Long openstreetmapId) {
	this.openstreetmapId = openstreetmapId;
    }*/

    /**
     * Override the gisFeature value.<br>
     * Default to true;<br>
     * If this field is set to false, then the object won't be synchronized with
     * the fullText search engine
     */
    @Override
    @Transient
    public boolean isFullTextSearchable() {
	return true;
    }

    /**
     * Constructor that populate the {@link Street} with the gisFeature fields<br>
     * 
     * @param gisFeature
     *            The gisFeature from which we want to populate the
     *            {@link Street}
     */
    public Street(GisFeature gisFeature) {
	super(gisFeature);
    }

    /**
     * Needed by CGLib
     */
    public Street() {
	super();
    }

    public boolean isOneWay() {
	return oneWay;
    }

    public void setOneWay(boolean oneWay) {
	this.oneWay = oneWay;
    }

    public StreetType getStreetType() {
	return streetType;
    }

    public void setStreetType(StreetType streetType) {
	this.streetType = streetType;
    }

    public Double getLength() {
	return length;
    }

    public void setLength(Double length) {
	this.length = length;
    }

    /**
     * @return The city or state or any information where the street is located
     */
    //we don't sync it, the is_in field is only to transfer data from openstreetmap to street object 
    @Transient
    public String getIsIn() {
	return isIn;
    }

    /**
     * @param isIn
     *            The city or state or any information where the street is
     *            located
     */
    public void setIsIn(String isIn) {
	this.isIn = isIn;
    }
    
    /**
	 * @return the place where the street is located, 
	 * this field is filled when {@link OpenStreetMap#isIn}
	 *  is filled and we got more specific details (generally quarter, neighborhood)
	 */
    @Transient
    //we don't sync it, the is_in field is only to transfer data from openstreetmap to street object
	public String getIsInPlace() {
		return isInPlace;
	}

	/**
	 * @param isInPlace the most precise information on where the street is located,
	 * generally quarter neighborhood
	 */
	public void setIsInPlace(String isInPlace) {
		this.isInPlace = isInPlace;
	}

	/**
	 * @return the adm (aka administrative division) where the street is located.
	 */
	@Transient
	//we don't sync it, the is_in field is only to transfer data from openstreetmap to street object
	public String getIsInAdm() {
		return isInAdm;
	}

	/**
	 * @param isInAdm  the adm (aka administrative division) where the street is located
	 */
	public void setIsInAdm(String isInAdm) {
		this.isInAdm = isInAdm;
	}

	/**
	 * @return the zipcode where the street is located
	 */
	@Transient
	//we don't sync it, the is_in field is only to transfer data from openstreetmap to street object
	public Set<String> getIsInZip() {
		return isInZip;
	}

	/**
	 * @param isInZip the zipcode where the street is located.
	 */
	public void setIsInZip(Set<String> isInZip) {
		this.isInZip = isInZip;
	}

	@Transient
	public String getFullyQualifiedAddress() {
		return fullyQualifiedAddress;
	}

	public void setFullyQualifiedAddress(String fullyQualifiedAddress) {
		this.fullyQualifiedAddress = fullyQualifiedAddress;
	}
	
	@Transient
    public SortedSet<HouseNumber> getHouseNumbers() {
		return houseNumbers;
	}

	public void setHouseNumbers(SortedSet<HouseNumber> houseNumbers) {
		this.houseNumbers = houseNumbers;
	}
	
	/**
	 * This field is only for relevance and allow to search for street<->cities in 
     * many alternateNames. It is not in stored
	 * 
	 */
	@Transient
	public Set<String> getIsInCityAlternateNames() {
		return isInCityAlternateNames;
	}

	public void setIsInCityAlternateNames(Set<String> isInCityAlternateNames) {
		this.isInCityAlternateNames = isInCityAlternateNames;
	}
	

    public void addIsInCitiesAlternateName(String isInCityAlternateName) {
	Set<String> currentCitiesAlternateNames = getIsInCityAlternateNames();
	if (currentCitiesAlternateNames == null) {
		currentCitiesAlternateNames = new HashSet<String>();
	}
	currentCitiesAlternateNames.add(isInCityAlternateName);
	this.setIsInCityAlternateNames(currentCitiesAlternateNames);
    }

    public void addIsInCitiesAlternateNames(Collection<String> isInCityAlternateNames) {
	if (isInCityAlternateNames != null) {
	    for (String isInCityAlternateName : isInCityAlternateNames) {
	    	addIsInCitiesAlternateName(isInCityAlternateName);
	    }
	}
    }
}
