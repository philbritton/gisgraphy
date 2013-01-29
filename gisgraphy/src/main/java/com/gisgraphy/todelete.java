package com.gisgraphy;

import com.gisgraphy.helper.GeolocHelper;
import com.vividsolutions.jts.geom.Point;

public class todelete {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Point p1 = GeolocHelper.createPoint(1f, 1f);
		Point p2 = GeolocHelper.createPoint(1f, 1.045225f);
		Point p3 = GeolocHelper.createPoint(1f, 1.13567f);
		Point p4 = GeolocHelper.createPoint(1f, 1.27133f);
		System.out.println(GeolocHelper.distance(p1, p2));
		System.out.println(GeolocHelper.distance(p2, p3));
		System.out.println(GeolocHelper.distance(p3, p4));
		

	}

}
