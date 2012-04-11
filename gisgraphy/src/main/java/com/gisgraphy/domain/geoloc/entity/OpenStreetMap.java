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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

import com.gisgraphy.domain.valueobject.GisgraphyConfig;
import com.gisgraphy.domain.valueobject.SRID;
import com.gisgraphy.helper.IntrospectionIgnoredField;
import com.gisgraphy.street.StreetSearchMode;
import com.gisgraphy.street.StreetType;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * Represents a street in OpenStreetMap. it is different from {@link Street} that represent a street in Geonames.
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SequenceGenerator(name = "streetosmsequence", sequenceName = "street_osm_sequence")
public class OpenStreetMap {
	

    public static final String SHAPE_COLUMN_NAME = "shape";

    /**
     * Name of the column that is equals to to_tsvector(
     * {@link #FULLTEXTSEARCH_COLUMN_NAME} It is used to do Fulltext search with
     * the postgres text search module (to use the index). This value should be
     * change if the getter and the setter of the {@link #textSearchName} change
     */
    public static final String FULLTEXTSEARCH_VECTOR_PROPERTY_NAME = "textsearchVector";

    /**
     * (Experimental) Name of the column that is equals to to_tsvector(
     * {@link #PARTIALSEARCH_COLUMN_NAME} It is used to do Fulltext search with
     * the postgres text search module (to use the index) This value should be
     * change if the getter and the setter of the {@link #partialSearchName}
     * change
     * @see GisgraphyConfig#PARTIAL_SEARH_EXPERIMENTAL
     */
    public static final String PARTIALSEARCH_VECTOR_PROPERTY_NAME = "partialsearchVector";

    /**
     * Name of the field property in hibernate. This is a string that is used
     * for fulltext and contains search without postgres fulltext engine. this
     * fields will have the name without accent and special char This value
     * should be changed if the getter and the setter of the
     * {@link #getTextsearchVector()} change
     * 
     * @see StreetSearchMode#FULLTEXT
     */
    public static final String FULLTEXTSEARCH_PROPERTY_NAME = "textSearchName";

    /**
     * Name of the column that is equals to store a string that is used for
     * fulltext search. it deffer form the @{@link #FULLTEXTSEARCH_COLUMN_NAME}
     * because Hibernate, by default, lowercase the property to get the column
     * name This value should be change if the getter and the setter of the
     * {@link #getTextsearchVector()} change
     * 
     * @see StreetSearchMode#FULLTEXT
     */
    public static final String FULLTEXTSEARCH_COLUMN_NAME = FULLTEXTSEARCH_PROPERTY_NAME.toLowerCase();

    /**
     * Name of the column that is used to store a string used for partial search
     * with postgres fulltext engine This value should be change if the getter
     * and the setter of the {@link #getPartialsearchVector()} change
     * 
     * @see GisgraphyConfig#PARTIAL_SEARH_EXPERIMENTAL
     * @see StreetSearchMode#CONTAINS
     */
    public static final String PARTIALSEARCH_COLUMN_NAME = "partialsearchname";

    public static final String LOCATION_COLUMN_NAME = "location";

    /**
     * Needed by CGLib
     */
    public OpenStreetMap() {
    }

    @IntrospectionIgnoredField
    private Long id;

    private Long gid;
    
    private Long openstreetmapId;

    private String name;

    private StreetType streetType;

    private boolean oneWay = false;

    private Point location;

    @IntrospectionIgnoredField
    private LineString shape;
    
    @IntrospectionIgnoredField
    private Integer population;
    
    private String isIn;

    private String countryCode;

    private Double length;

    @IntrospectionIgnoredField
    private String partialSearchName;

    @IntrospectionIgnoredField
    private String textSearchName;

    /**
     * (Experimental) This String is used to search for a part of a street name
     * 
     * @see StreetSearchMode#CONTAINS
     * @return the partialSearchName
     * @see GisgraphyConfig#PARTIAL_SEARH_EXPERIMENTAL
     */
    @Column(unique = false, nullable = true, columnDefinition = "text")
    public String getPartialSearchName() {
	return partialSearchName;
    }

    /**
     * @param partialSearchName
     *            the partialSearchName to set
     */
    public void setPartialSearchName(String partialSearchName) {
	this.partialSearchName = partialSearchName;
    }

    /**
     * This value is use to do a Fulltext search for a street name with index
     * 
     * @return the textSearchName
     */
    @Column(unique = false, nullable = true, columnDefinition = "text")
    public String getTextSearchName() {
	return textSearchName;
    }

    /**
     * @param textSearchName
     *            the textSearchName to set
     */
    public void setTextSearchName(String textSearchName) {
	this.textSearchName = textSearchName;
    }

    /**
     * IT DOES NOTHING. ONLY USE BY HIBERNATE This field is only use for the
     * text search to improve performance, you should not set / get a value, it
     * is declared here, to create the column
     * 
     * @return null ALWAYS
     */
    @Column(unique = false, nullable = true, insertable = false, updatable = true, columnDefinition = "tsvector")
    @Type(type = "com.gisgraphy.hibernate.type.TsVectorStringType")
    public String getTextsearchVector() {
	return null;
    }

    /**
     * IT DOES NOTHING. ONLY USE BY HIBERNATE
     * 
     * @param textsearchVector
     *            the textsearchVector to set
     * 
     */
    public void setTextsearchVector(String textsearchVector) {
    }

    /**
     * IT DOES NOTHING. ONLY USE BY HIBERNATE This field is only use for the
     * autocomplete search to improve performance, you should not set / get a
     * value, it is declared here, to create the column
     * 
     * @return null ALWAYS
     */
    @Column(unique = false, nullable = true, insertable = false, updatable = true, columnDefinition = "tsvector")
    @Type(type = "com.gisgraphy.hibernate.type.TsVectorStringType")
    public String getPartialsearchVector() {
	return null;
    }

    /**
     * IT DOES NOTHING. ONLY USE BY HIBERNATE
     * 
     * @param partialsearchVector
     *            the ilikesearch to set
     */
    public void setPartialsearchVector(String partialsearchVector) {

    }

    /**
     * @return the id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "streetosmsequence")
    public Long getId() {
	return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
	this.id = id;
    }

    /**
     * @return an uniqueid that identify the street, it differs from {@link OpenStreetMap#openstreetmapId}
     *  because the value can not be in conflict between geonames and openstreetmap
     */
    @Index(name = "streetosmgidindex")
    @Column(unique = true, nullable = false)
    public Long getGid() {
	return gid;
    }

    /**
     * @param gid
     *            the gid to set
     */
    public void setGid(Long gid) {
	this.gid = gid;
    }
    
    /**
     * @return the openstreetmap internal id
     */
    @Index(name = "streetosmopenstreetmapidindex")
    @Column(unique = false, nullable = true)
    public Long getOpenstreetmapId() {
        return openstreetmapId;
    }

    /**
     * @param openstreetmapId the openstreetmap internal id
     */
    public void setOpenstreetmapId(Long openstreetmapId) {
        this.openstreetmapId = openstreetmapId;
    }

    /**
     * @return the name
     */
    @Column(length = 255)
    public String getName() {
	return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return the type of the street
     */
    @Index(name = "streetosmtypeIndex")
    @Enumerated(EnumType.STRING)
    public StreetType getStreetType() {
	return streetType;
    }

    /**
     * @param streetType
     *            the streetType to set
     */
    public void setStreetType(StreetType streetType) {
	this.streetType = streetType;
    }

    /**
     * @return the oneway
     */
    @Index(name = "streetosmonewayIndex")
    @Column(length = 9)
    public boolean isOneWay() {
	return oneWay;
    }

    /**
     * @param oneWay
     *            the oneWay to set
     */
    public void setOneWay(boolean oneWay) {
	this.oneWay = oneWay;
    }

    /**
     * Returns The JTS location point of the current street : The Geometry
     * representation for the latitude, longitude. The Return type is a JTS
     * point. The Location is calculate from the 4326 {@link SRID}
     * 
     * @see SRID
     * @return The JTS Point
     */
    @Type(type = "org.hibernatespatial.GeometryUserType")
    @Column(name = OpenStreetMap.LOCATION_COLUMN_NAME)
    public Point getLocation() {
	return location;
    }
    
    /**
     * @return Returns the latitude (north-south) from the Location
     *         {@link #getLocation()}.
     * @see #getLongitude()
     * @see #getLocation()
     */
    @Transient
    public Double getLatitude() {
	Double latitude = null;
	if (this.location != null) {
	    latitude = this.location.getY();
	}
	return latitude;
    }
    
    /**
     * @return Returns the longitude (east-west) from the Location
     *         {@link #getLocation()}.
     * @see #getLongitude()
     * @see #getLocation()
     */
    @Transient
    public Double getLongitude() {
	Double longitude = null;
	if (this.location != null) {
	    longitude = this.location.getX();
	}
	return longitude;
    }

    /**
     * @param location
     *            the location to set
     */
    public void setLocation(Point location) {
	this.location = location;
    }

    /**
     * @return the shape
     */
    @Type(type = "org.hibernatespatial.GeometryUserType")
    @Column(nullable = false)
    public LineString getShape() {
	return shape;
    }

    /**
     * @param shape
     *            the shape to set
     */
    public void setShape(LineString shape) {
	this.shape = shape;
    }

    /**
     * @return The ISO 3166 alpha-2 letter code.
     */
    @Index(name = "openstreetmapcountryindex")
    @Column(length = 3)
    public String getCountryCode() {
	return countryCode;
    }

    /**
     * @param countryCode
     *            the countryCode to set
     */
    public void setCountryCode(String countryCode) {
	this.countryCode = countryCode;
    }

    /**
     * @return the length of the street in meters
     */
    public Double getLength() {
	return length;
    }

    /**
     * @param length
     *            the length to set
     */
    public void setLength(Double length) {
	this.length = length;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	return result;
    }

    @Override
    //TODO use business key
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	OpenStreetMap other = (OpenStreetMap) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	return true;
    }


    /**
     * @return The city or state or any information where the street is located
     */
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

	public Integer getPopulation() {
		return population;
	}

	public void setPopulation(Integer population) {
		this.population = population;
	}

}
