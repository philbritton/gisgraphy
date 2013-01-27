package com.gisgraphy.importer;

import net.sf.jstester.util.Assert;

import org.junit.Test;

import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.importer.dto.AssociatedStreetHouseNumber;
import com.gisgraphy.importer.dto.AssociatedStreetMember;
import com.vividsolutions.jts.geom.Point;

public class OpenStreetMapHouseNumberSimpleImporterTest {

	@Test
	public void parseAssociatedStreetHouseNumber() {
		String line = "A	" +
				"2069647	1661205474___0101000020E6100000046DBC85BFA81D40DA7D22AA4BDD4540___24___Avenue de Fontvieille___N___house;" +
				"158189815___0101000020E61000002AA4070C99A81D40227F492749DD4540___Avenue de Fontvieille___Avenue de Fontvieille___W___street;" +
				"176577460___0101000020E61000004522EE9504A81D4081BAA66957DD4540___Avenue de Fontvieille___Avenue de Fontvieille___W___street";
		OpenStreetMapHouseNumberSimpleImporter importer = new OpenStreetMapHouseNumberSimpleImporter();
			AssociatedStreetHouseNumber actual = importer.parseAssociatedStreetHouseNumber(line);
			//TODO wrong number of fields, null
			Assert.assertEquals("2069647", actual.getRelationID());
			Assert.assertNotNull(actual.getAssociatedStreetMember());
			Assert.assertEquals(3,actual.getAssociatedStreetMember().size());
			AssociatedStreetMember m1 = new AssociatedStreetMember("1661205474", (Point)GeolocHelper.convertFromHEXEWKBToGeometry("0101000020E6100000046DBC85BFA81D40DA7D22AA4BDD4540"),"24",null,"N","house");
			AssociatedStreetMember m2 = new AssociatedStreetMember("158189815", (Point)GeolocHelper.convertFromHEXEWKBToGeometry("0101000020E61000002AA4070C99A81D40227F492749DD4540"),null,"Avenue de Fontvieille","W","street");
			AssociatedStreetMember m3 = new AssociatedStreetMember("176577460", (Point)GeolocHelper.convertFromHEXEWKBToGeometry("0101000020E61000004522EE9504A81D4081BAA66957DD4540"),null,"Avenue de Fontvieille","W","street");
			
			Assert.assertTrue(actual.getAssociatedStreetMember().contains(m1));
			Assert.assertTrue(actual.getAssociatedStreetMember().contains(m2));
			Assert.assertTrue(actual.getAssociatedStreetMember().contains(m3));
	}
	
	public void parseAssociatedStreetHouseNumberWrongType() {
		String line = "X	" +
				"2069647	1661205474___0101000020E6100000046DBC85BFA81D40DA7D22AA4BDD4540___24___Avenue de Fontvieille___N___house;" +
				"158189815___0101000020E61000002AA4070C99A81D40227F492749DD4540___Avenue de Fontvieille___Avenue de Fontvieille___W___street;" +
				"176577460___0101000020E61000004522EE9504A81D4081BAA66957DD4540___Avenue de Fontvieille___Avenue de Fontvieille___W___street";
		OpenStreetMapHouseNumberSimpleImporter importer = new OpenStreetMapHouseNumberSimpleImporter();
			Assert.assertNull(importer.parseAssociatedStreetHouseNumber(line));
	}
	
	@Test
	public void parseAssociatedStreetHouseNumberUnderscoreInName() {
		String line = "A	" +
				"2069647	1661205474___0101000020E6100000046DBC85BFA81D40DA7D22AA4BDD4540___24___Ave_nue de Fontvieille___N___house;" +
				"176577460___0101000020E61000004522EE9504A81D4081BAA66957DD4540___Avenue de Fontvieil_le___Avenue_ de Fontvieille___W___street";
		OpenStreetMapHouseNumberSimpleImporter importer = new OpenStreetMapHouseNumberSimpleImporter();
			AssociatedStreetHouseNumber actual = importer.parseAssociatedStreetHouseNumber(line);
			//TODO wrong number of fields, null
			Assert.assertEquals("2069647", actual.getRelationID());
			Assert.assertNotNull(actual.getAssociatedStreetMember());
			Assert.assertEquals(2,actual.getAssociatedStreetMember().size());
			AssociatedStreetMember m1 = new AssociatedStreetMember("1661205474", (Point)GeolocHelper.convertFromHEXEWKBToGeometry("0101000020E6100000046DBC85BFA81D40DA7D22AA4BDD4540"),"24",null,"N","house");
			AssociatedStreetMember m2 = new AssociatedStreetMember("176577460", (Point)GeolocHelper.convertFromHEXEWKBToGeometry("0101000020E61000004522EE9504A81D4081BAA66957DD4540"),null,"Avenue de Fontvieil_le","W","street");
			
			Assert.assertTrue(actual.getAssociatedStreetMember().contains(m1));
			Assert.assertTrue(actual.getAssociatedStreetMember().contains(m2));
	}
	
	
	@Test
	public void parseAssociatedStreetHouseNumberWrongHouseNumberType() {
		String line = "B	" +
				"2069647	1661205474___0101000020E6100000046DBC85BFA81D40DA7D22AA4BDD4540___24___Avenue de Fontvieille___N___house;" +
				"158189815___0101000020E61000002AA4070C99A81D40227F492749DD4540___Avenue de Fontvieille___Avenue de Fontvieille___W___street;" +
				"176577460___0101000020E61000004522EE9504A81D4081BAA66957DD4540___Avenue de Fontvieille___Avenue de Fontvieille___W___street";
		OpenStreetMapHouseNumberSimpleImporter importer = new OpenStreetMapHouseNumberSimpleImporter();
		Assert.assertNull(importer.parseAssociatedStreetHouseNumber(line));
	}
	
	@Test
	public void parseAssociatedStreetHouseNumberWrongNumberOFields() {
		String line = "A	" +
				"2069647	";
		OpenStreetMapHouseNumberSimpleImporter importer = new OpenStreetMapHouseNumberSimpleImporter();
		Assert.assertNull(importer.parseAssociatedStreetHouseNumber(line));
	}
	
	@Test
	public void parseAssociatedStreetHouseNumberNullOrEmptyLine() {
		String line =null;
		OpenStreetMapHouseNumberSimpleImporter importer = new OpenStreetMapHouseNumberSimpleImporter();
			Assert.assertNull(importer.parseAssociatedStreetHouseNumber(line));
			Assert.assertNull(importer.parseAssociatedStreetHouseNumber(""));
	}
	
	/*--------------------------------------------------interpolation-----------------------------------------------*/
	
	public void parseInterpolationHouseNumberWrongNumberOFields() {
		String line = "I	" +
				"2069647	";
		OpenStreetMapHouseNumberSimpleImporter importer = new OpenStreetMapHouseNumberSimpleImporter();
		Assert.assertNull(importer.parseInterpolationHouseNumber(line));
	}
	
	@Test
	public void parseInterpolationHouseNumberNullOrEmptyLine() {
		String line =null;
		OpenStreetMapHouseNumberSimpleImporter importer = new OpenStreetMapHouseNumberSimpleImporter();
			Assert.assertNull(importer.parseInterpolationHouseNumber(line));
			Assert.assertNull(importer.parseInterpolationHouseNumber(""));
	}
	

}
