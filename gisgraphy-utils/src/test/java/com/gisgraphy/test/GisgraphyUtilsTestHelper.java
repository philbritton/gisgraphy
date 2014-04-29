package com.gisgraphy.test;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.gisgraphy.domain.valueobject.SRID;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

public class GisgraphyUtilsTestHelper {
	
	public static final String HTTP_BASE_URL = "http://services.gisgraphy.com/";

	 public static Point createPoint(Float longitude, Float latitude) {
			if (longitude < -180 || longitude > 180) {
			    throw new IllegalArgumentException(
				    "Longitude should be between -180 and 180");
			}
			if (latitude < -90 || latitude > 90) {
			    throw new IllegalArgumentException(
				    "latitude should be between -90 and 90");
			}
			GeometryFactory factory = new GeometryFactory(new PrecisionModel(
				PrecisionModel.FLOATING), SRID.WGS84_SRID.getSRID());
			Point point = (Point) factory.createPoint(new Coordinate(longitude,
				latitude));
			return point;
		    }
	 
	 public static HashMap<String, String> splitURLParams(String completeURL,
			    String andSign) {
			int i;
			HashMap<String, String> searchparms = new HashMap<String, String>();
			;
			i = completeURL.indexOf("?");
			if (i > -1) {
			    String searchURL = completeURL
				    .substring(completeURL.indexOf("?") + 1);

			    String[] paramArray = searchURL.split(andSign);
			    for (int c = 0; c < paramArray.length; c++) {
				String[] paramSplited = paramArray[c].split("=");
				try {
				    searchparms.put(paramSplited[0], java.net.URLDecoder
					    .decode(paramSplited[1], "UTF-8"));
				} catch (UnsupportedEncodingException e) {
				    return new HashMap<String, String>();
				}

			    }
			    // dumpHashtable;
			    java.util.Iterator<String> keys = searchparms.keySet().iterator();
			    while (keys.hasNext()) {
				String s = (String) keys.next();
			    }

			}
			return searchparms;
		    }

	
}
