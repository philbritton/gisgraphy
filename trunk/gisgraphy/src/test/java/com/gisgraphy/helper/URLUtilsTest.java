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
package com.gisgraphy.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

import com.gisgraphy.test.GisgraphyTestHelper;
import com.vividsolutions.jts.geom.Point;

public class URLUtilsTest  {

    @Test
    public void createGoogleMapUrlShouldReturnCorrectURL() {
	Point point = GisgraphyTestHelper.createPoint(2.33333F, 48.86667F);
	String URL = URLUtils.createGoogleMapUrl(point, "Paris");
	HashMap<String, String> map = GisgraphyTestHelper.splitURLParams(URL,
		"&amp;");
	assertTrue(map.get("q").equals("Paris"));
	String[] ll = map.get("ll").split(",");
	assertTrue(ll[0].startsWith("48.896"));
	assertTrue(ll[1].startsWith("2.333"));

    }

    @Test
    public void createGoogleMapUrlShouldReturnDefaultGoogleURLIfPointIsNull() {
	assertEquals(URLUtils.DEFAULT_GOOGLE_MAP_BASE_URL, URLUtils
		.createGoogleMapUrl(null, null));
    }

    @Test
    public void createYahooMapUrlShouldReturnCorrectURL() {
	Point point = GisgraphyTestHelper.createPoint(2.33333F, 48.86667F);
	String URL = URLUtils.createYahooMapUrl(point);
	HashMap<String, String> map = GisgraphyTestHelper.splitURLParams(URL,
		"&amp;");
	assertTrue(map.get("lat").startsWith("48.866"));
	assertTrue(map.get("lon").startsWith("2.333"));
    }

    @Test
    public void createYahooMapUrlShouldReturnDefaultyahooURLIfPointIsNull() {
	assertEquals(URLUtils.DEFAULT_YAHOO_MAP_BASE_URL, URLUtils
		.createYahooMapUrl(null));
    }

    @Test
    public void createCountryFlagUrlShouldReturnCorrectURL() {
	assertEquals(URLUtils.COUNTRY_FLAG_BASE_URL + "FR.png", URLUtils
		.createCountryFlagUrl("FR"));
    }

    @Test
    public void createCountryFlagUrlShouldupperCaseTheCountryCode() {
	assertEquals(URLUtils.COUNTRY_FLAG_BASE_URL + "FR.png", URLUtils
		.createCountryFlagUrl("fr"));
    }

    @Test
    public void createCountryFlagUrlShouldReturnDefaultCountryFlagURLIfCountryCodeIsNull() {
	assertEquals(URLUtils.DEFAULT_COUNTRY_FLAG_URL, URLUtils
		.createCountryFlagUrl(null));
    }

}
