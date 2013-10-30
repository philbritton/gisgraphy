package com.gisgraphy.importer;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.geoloc.entity.Shop;

public class OsmAmenityToPlacetypeTest {

	@Test
	public void getObjectWithNull() {
		OsmAmenityToPlacetype osmAmenityToPlacetype = new OsmAmenityToPlacetype();
		GisFeature o = osmAmenityToPlacetype.getObject(null);
		assertEquals(GisFeature.class.getSimpleName(), o.getClass().getSimpleName());
		Assert.assertEquals(OsmAmenityToPlacetype.DEFAULT_OSM_FEATURE_CLASS, o.getFeatureClass());
		Assert.assertEquals(OsmAmenityToPlacetype.DEFAULT_OSM_FEATURE_CODE, o.getFeatureCode());
		Assert.assertEquals(null, o.getAmenity());
	}
	
	@Test
	public void getObjectWithEmptyString() {
		OsmAmenityToPlacetype osmAmenityToPlacetype = new OsmAmenityToPlacetype();
		GisFeature o = osmAmenityToPlacetype.getObject("");
		Assert.assertEquals(GisFeature.class, o.getClass());
		Assert.assertEquals(OsmAmenityToPlacetype.DEFAULT_OSM_FEATURE_CLASS, o.getFeatureClass());
		Assert.assertEquals(OsmAmenityToPlacetype.DEFAULT_OSM_FEATURE_CODE, o.getFeatureCode());
		Assert.assertEquals(null, o.getAmenity());
	}
	
	@Test
	public void getObjectWithUnknowAmenity() {
		OsmAmenityToPlacetype osmAmenityToPlacetype = new OsmAmenityToPlacetype();
		GisFeature o = osmAmenityToPlacetype.getObject("foo");
		Assert.assertEquals(GisFeature.class, o.getClass());
		Assert.assertEquals(OsmAmenityToPlacetype.DEFAULT_OSM_FEATURE_CLASS, o.getFeatureClass());
		Assert.assertEquals(OsmAmenityToPlacetype.DEFAULT_OSM_FEATURE_CODE, o.getFeatureCode());
		Assert.assertEquals("foo", o.getAmenity());
	}
	
	@Test
	public void getObjectWithknowAmenity_caseandTrim() {
		OsmAmenityToPlacetype osmAmenityToPlacetype = new OsmAmenityToPlacetype();
		GisFeature o = osmAmenityToPlacetype.getObject("ShOp ");
		Assert.assertEquals(Shop.class, o.getClass());
		Assert.assertEquals(OsmAmenityToPlacetype.DEFAULT_OSM_FEATURE_CODE, o.getFeatureCode());
		Assert.assertEquals("SHOP", o.getFeatureClass());
		Assert.assertEquals("shop", o.getAmenity());
	}
	@Test
	public void getObjectWithknowAmenity() {
		OsmAmenityToPlacetype osmAmenityToPlacetype = new OsmAmenityToPlacetype();
		GisFeature o = osmAmenityToPlacetype.getObject("shop");
		Assert.assertEquals(Shop.class, o.getClass());
		Assert.assertEquals(OsmAmenityToPlacetype.DEFAULT_OSM_FEATURE_CODE, o.getFeatureCode());
		Assert.assertEquals("SHOP", o.getFeatureClass());
		Assert.assertEquals("shop", o.getAmenity());
	}

}
