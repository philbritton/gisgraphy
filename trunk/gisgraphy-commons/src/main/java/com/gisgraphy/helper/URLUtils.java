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
/**
 * 
 */
package com.gisgraphy.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.gisgraphy.domain.valueobject.Constants;
import com.vividsolutions.jts.geom.Point;

/**
 * Provides URL utilities
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 * 
 */
public class URLUtils {

    /**
     * A constant to create Google map URL
     */
    public static final String GOOGLE_MAP_BASE_URL = "http://maps.google.com/maps?f=q&amp;ie=UTF-8&amp;iwloc=addr&amp;om=1&amp;z=12&amp;q=";

    /**
     * The default Google map URL
     */
    public static final String DEFAULT_GOOGLE_MAP_BASE_URL = "http://maps.google.com/maps?f=q&amp;ie=UTF-8&amp;iwloc=addr&amp;om=1&amp;z=1&amp;q=";

    /**
     * A Constant to create Yahoo map URL
     */
    public static final String YAHOO_MAP_BASE_URL = "http://maps.yahoo.com/broadband?mag=6&amp;mvt=m";

    /**
     * The default Yahoo map URL
     */
    public static final String DEFAULT_YAHOO_MAP_BASE_URL = "http://maps.yahoo.com/";

    /**
     * A constant to construct country flag URL
     */
    public static final String COUNTRY_FLAG_BASE_URL = "/images/flags/";

    /**
     * The default country flag URL
     */
    public static final String DEFAULT_COUNTRY_FLAG_URL = "/images/flags/default.png";

    /**
     * @param point
     *                The Point we'd like to map
     * @param label
     *                the text we'd like to display on the map
     * @see #DEFAULT_GOOGLE_MAP_BASE_URL
     * @return an Google Map URL for the specified point and label
     */
    public static String createGoogleMapUrl(Point point, String label) {
	if (point == null) {
	    return DEFAULT_GOOGLE_MAP_BASE_URL;
	}
	try {
	    StringBuffer sb = new StringBuffer(GOOGLE_MAP_BASE_URL);
	    sb.append(
		    URLEncoder.encode(label == null ? "" : label,
			    Constants.CHARSET)).append("&amp;ll=").append(
		    point.getY() + 0.03)// add
		    // an
		    // offset
		    .append(",").append(point.getX());
	    return sb.toString();
	} catch (UnsupportedEncodingException e) {
	    return DEFAULT_GOOGLE_MAP_BASE_URL;
	}
    }

    /**
     * @param point
     *                The Point we'd like to map
     * @see #DEFAULT_YAHOO_MAP_BASE_URL
     * @return an Yahoo Map URL for the specified point and label
     */
    public static String createYahooMapUrl(Point point) {
	if (point == null) {
	    return DEFAULT_YAHOO_MAP_BASE_URL;
	}
	StringBuffer sb = new StringBuffer(YAHOO_MAP_BASE_URL);
	sb.append("&amp;lon=").append(point.getX()).append("&amp;lat=").append(
		point.getY());
	return sb.toString();
    }

    /**
     * @param countryCode
     *                The countryCode to construct the country flag URL
     * @return A url relative to the current server
     * @see #DEFAULT_COUNTRY_FLAG_URL
     */
    public static String createCountryFlagUrl(String countryCode) {
	if (countryCode == null) {
	    return DEFAULT_COUNTRY_FLAG_URL;
	}
	StringBuffer sb = new StringBuffer(COUNTRY_FLAG_BASE_URL);
	sb.append(countryCode.toUpperCase()).append(".png");
	return sb.toString();
    }

}
