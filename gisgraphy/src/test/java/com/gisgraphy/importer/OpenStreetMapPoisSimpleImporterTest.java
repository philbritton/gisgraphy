package com.gisgraphy.importer;

import static com.gisgraphy.test.GisgraphyTestHelper.alternateNameContains;

import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.AlternateName;
import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.geoloc.entity.Religious;
import com.gisgraphy.domain.geoloc.entity.Shop;
import com.gisgraphy.domain.repository.IAdmDao;
import com.gisgraphy.domain.repository.ICityDao;
import com.gisgraphy.domain.repository.IGisFeatureDao;
import com.gisgraphy.domain.repository.IIdGenerator;
import com.gisgraphy.domain.valueobject.AlternateNameSource;
import com.gisgraphy.domain.valueobject.GISSource;
import com.gisgraphy.fulltext.SolrResponseDto;
import com.vividsolutions.jts.geom.Point;

public class OpenStreetMapPoisSimpleImporterTest {

	@Test
	public void populateAlternateNames() {
		String RawAlternateNames="Karl-Franzens-Universität Graz___Universidad de Graz___Université de Graz___Грацский университет имени Карла и Франца";
		OpenStreetMapPoisSimpleImporter importer = new OpenStreetMapPoisSimpleImporter();
		GisFeature poi = new GisFeature();
		poi = importer.populateAlternateNames(poi, RawAlternateNames);
		Assert.assertEquals(4, poi.getAlternateNames().size());
		Assert.assertTrue(alternateNameContains(poi.getAlternateNames(),"Karl-Franzens-Universität Graz"));
		Assert.assertTrue(alternateNameContains(poi.getAlternateNames(),"Universidad de Graz"));
		Assert.assertTrue(alternateNameContains(poi.getAlternateNames(),"Université de Graz"));
		Assert.assertTrue(alternateNameContains(poi.getAlternateNames(),"Грацский университет имени Карла и Франца"));
		
		Iterator<AlternateName> iterator = poi.getAlternateNames().iterator();
		while (iterator.hasNext()){
			Assert.assertEquals(AlternateNameSource.OPENSTREETMAP,iterator.next().getSource());
		}
		
	}

	
	@Test
	public void populatePoi(){
		OpenStreetMapPoisSimpleImporter importer = new OpenStreetMapPoisSimpleImporter();
		
		IIdGenerator idGenerator = EasyMock.createMock(IIdGenerator.class);
    	EasyMock.expect(idGenerator.getNextFeatureId()).andReturn(1234L);
    	EasyMock.replay(idGenerator);
    	importer.setIdGenerator(idGenerator);
		
		String line= "W\t90139043\tPfarrkirche Heiliger Johannes der Täufer\tAT\tPfarrkirche Heiliger Johannes der Täufer___Parish church Saint John Baptist\t0101000020E61000000E6D653509482C40B01EF706AB514740\tplace_of_worship__________________________________________";
		String[] fields = line.split("\t");
		String amenity= fields[6];
		List<GisFeature> pois = importer.createAndpopulatePoi(fields, amenity);
		Assert.assertEquals(1, pois.size());
		GisFeature poi = pois.get(0);
		Assert.assertEquals(90139043L, poi.getOpenstreetmapId().longValue());
		Assert.assertEquals("Pfarrkirche Heiliger Johannes der Täufer", poi.getName());
		Assert.assertEquals("AT", poi.getCountryCode());
		Assert.assertEquals("place_of_worship", poi.getAmenity());
		Assert.assertEquals(GISSource.OSM, poi.getSource());;
		Assert.assertEquals(OsmAmenityToPlacetype.DEFAULT_OSM_FEATURE_CODE, poi.getFeatureCode());
		Assert.assertEquals(OsmAmenityToPlacetype.DEFAULT_OSM_FEATURE_CLASS, poi.getFeatureClass());
		Assert.assertEquals(Religious.class, poi.getClass());
		Assert.assertEquals(1234L, poi.getFeatureId().longValue());
		Assert.assertNotNull(poi.getLocation());
		
		
		//an
		Assert.assertEquals(2, poi.getAlternateNames().size());
		
		Assert.assertTrue(alternateNameContains(poi.getAlternateNames(),"Pfarrkirche Heiliger Johannes der Täufer"));
		Assert.assertTrue(alternateNameContains(poi.getAlternateNames(),"Parish church Saint John Baptist"));
		
		Iterator<AlternateName> iterator = poi.getAlternateNames().iterator();
		while (iterator.hasNext()){
			Assert.assertEquals(AlternateNameSource.OPENSTREETMAP,iterator.next().getSource());
		}
		
	}
	
	
	@Test
	public void processDataWithWrongName(){
		
		OpenStreetMapPoisSimpleImporter importer = new OpenStreetMapPoisSimpleImporter();
		

		IGisFeatureDao gisFeatureDao = EasyMock.createMock(IGisFeatureDao.class);
		//The dao should not be called
		EasyMock.replay(gisFeatureDao);
		importer.setGisFeatureDao(gisFeatureDao);
		
		IIdGenerator idGenerator = EasyMock.createMock(IIdGenerator.class);
    	EasyMock.expect(idGenerator.getNextFeatureId()).andReturn(1234L);
    	EasyMock.replay(idGenerator);
    	importer.setIdGenerator(idGenerator);
		
    	String line= "W\t90139043\tPfarrkirche Heiliger Johannes der Täufer\tAT\tPfarrkirche Heiliger Johannes der Täufer___Parish church Saint John Baptist\tfoo\tplace_of_worship";
		
		importer.processData(line);
		EasyMock.verify(gisFeatureDao);
		
	}
	
	@Test
	public void processData(){
		
		OpenStreetMapPoisSimpleImporter importer = new OpenStreetMapPoisSimpleImporter();
		

		IGisFeatureDao gisFeatureDao = EasyMock.createMock(IGisFeatureDao.class);
		Religious poi = new Religious();
		long featureId = 1234L;
		poi.setFeatureId(featureId);
		EasyMock.expect(gisFeatureDao.save(poi)).andReturn(poi);
		EasyMock.replay(gisFeatureDao);
		importer.setGisFeatureDao(gisFeatureDao);
		
		IIdGenerator idGenerator = EasyMock.createMock(IIdGenerator.class);
    	EasyMock.expect(idGenerator.getNextFeatureId()).andReturn(featureId);
    	EasyMock.replay(idGenerator);
    	importer.setIdGenerator(idGenerator);
		
    	String line= "W\t90139043\tPfarrkirche Heiliger Johannes der Täufer\tAT\tPfarrkirche Heiliger Johannes der Täufer___Parish church Saint John Baptist\t0101000020E61000000E6D653509482C40B01EF706AB514740\tplace_of_worship";
		
		importer.processData(line);
		EasyMock.verify(gisFeatureDao);
		
	}
	
	@Test
	public void testSplitTags(){
		OpenStreetMapPoisSimpleImporter importer = new OpenStreetMapPoisSimpleImporter();
		String[] tags = importer.splitTags("place_of_worship");
		Assert.assertEquals(14, tags.length);
		Assert.assertEquals("place_of_worship", tags[0]);
		
		 tags = importer.splitTags("place_of_worship___f______y______u___i________________________");
		 Assert.assertEquals(14, tags.length);
		 Assert.assertEquals("place_of_worship", tags[0]);
		 Assert.assertEquals("f", tags[1]);
		 Assert.assertEquals("y", tags[3]);
		 Assert.assertEquals("u", tags[5]);
		 Assert.assertEquals("i", tags[6]);
		 
		 tags = importer.splitTags("place_of_worship___f______y______u___i_____________________toto");
		 Assert.assertEquals(14, tags.length);
		 Assert.assertEquals("place_of_worship", tags[0]);
		 Assert.assertEquals("f", tags[1]);
		 Assert.assertEquals("y", tags[3]);
		 Assert.assertEquals("u", tags[5]);
		 Assert.assertEquals("i", tags[6]);
		 Assert.assertEquals("toto", tags[13]);
		 
		 tags = importer.splitTags("_______________________________________");
		 Assert.assertEquals(14, tags.length);
		 for (int i=0;i<tags.length;i++){
			 Assert.assertEquals(null, tags[0]);
		 }
		
	}
	/*
	@Test
	public void process(){
		final SolrResponseDto solrResponseDtoCity = EasyMock.createMock(SolrResponseDto.class);
		EasyMock.expect(solrResponseDtoCity.getFeature_id()).andReturn(123L);
		EasyMock.replay(solrResponseDtoCity);
		
		OpenStreetMapPoisSimpleImporter importer = new OpenStreetMapPoisSimpleImporter();
		
		IIdGenerator idGenerator = EasyMock.createMock(IIdGenerator.class);
    	EasyMock.expect(idGenerator.getNextFeatureId()).andReturn(1234L);
    	EasyMock.replay(idGenerator);
    	importer.setIdGenerator(idGenerator);
		
		IGisFeatureDao cityDao = EasyMock.createMock(IGisFeatureDao.class);
		City city=new City();
		city.setFeatureId(123L);
		EasyMock.expect(cityDao.save(city)).andReturn(city);
		EasyMock.replay(cityDao);
		importer.setCityDao(cityDao);
		
		
		IAdmDao admDao = EasyMock.createMock(IAdmDao.class);
		EasyMock.replay(admDao);
		importer.setAdmDao(admDao);
		
		
		String line= "W\t90139043\tPfarrkirche Heiliger Johannes der Täufer\tAT\tPfarrkirche Heiliger Johannes der Täufer___Parish church Saint John Baptist\t0101000020E61000000E6D653509482C40B01EF706AB514740\tplace_of_worship";
		
		importer.processData(line);
		
		EasyMock.verify(cityDao);
		EasyMock.verify(admDao);
	}
	*/
	
}
