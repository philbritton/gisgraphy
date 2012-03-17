package com.gisgraphy.test;

import com.gisgraphy.domain.valueobject.SRID;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

public class GisgraphyUtilsTestHelper {

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
	
}
