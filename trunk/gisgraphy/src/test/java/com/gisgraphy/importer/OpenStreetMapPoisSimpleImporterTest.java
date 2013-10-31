package com.gisgraphy.importer;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.geoloc.entity.Religious;
import com.gisgraphy.domain.geoloc.entity.Shop;
import com.gisgraphy.domain.repository.IAdmDao;
import com.gisgraphy.domain.repository.ICityDao;
import com.gisgraphy.domain.repository.IGisFeatureDao;
import com.gisgraphy.domain.repository.IIdGenerator;
import com.gisgraphy.domain.valueobject.AlternateNameSource;
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
		Assert.assertEquals("Karl-Franzens-Universität Graz",poi.getAlternateNames().get(0).getName());
		Assert.assertEquals(AlternateNameSource.OPENSTREETMAP,poi.getAlternateNames().get(0).getSource());
		Assert.assertEquals("Universidad de Graz",poi.getAlternateNames().get(1).getName());
		Assert.assertEquals(AlternateNameSource.OPENSTREETMAP,poi.getAlternateNames().get(1).getSource());
		Assert.assertEquals("Université de Graz",poi.getAlternateNames().get(2).getName());
		Assert.assertEquals(AlternateNameSource.OPENSTREETMAP,poi.getAlternateNames().get(2).getSource());
		Assert.assertEquals("Грацский университет имени Карла и Франца",poi.getAlternateNames().get(3).getName());
		Assert.assertEquals(AlternateNameSource.OPENSTREETMAP,poi.getAlternateNames().get(3).getSource());
		
	}

	
	@Test
	public void populatePoi(){
		OpenStreetMapPoisSimpleImporter importer = new OpenStreetMapPoisSimpleImporter();
		
		IIdGenerator idGenerator = EasyMock.createMock(IIdGenerator.class);
    	EasyMock.expect(idGenerator.getNextFeatureId()).andReturn(1234L);
    	EasyMock.replay(idGenerator);
    	importer.setIdGenerator(idGenerator);
		
		String line= "W\t90139043\tPfarrkirche Heiliger Johannes der Täufer\tAT\tPfarrkirche Heiliger Johannes der Täufer___Parish church Saint John Baptist\t0101000020E61000000E6D653509482C40B01EF706AB514740\tplace_of_worship";
		String[] fields = line.split("\t");
		String amenity= fields[6];
		GisFeature poi = importer.createAndpopulatePoi(fields, amenity);
		Assert.assertEquals(90139043L, poi.getOpenstreetmapId().longValue());
		Assert.assertEquals("Pfarrkirche Heiliger Johannes der Täufer", poi.getName());
		Assert.assertEquals("AT", poi.getCountryCode());
		Assert.assertEquals("place_of_worship", poi.getAmenity());
		Assert.assertEquals("OSM", poi.getFeatureCode());
		Assert.assertEquals("RELIGIOUS", poi.getFeatureClass());
		Assert.assertEquals(Religious.class, poi.getClass());
		Assert.assertEquals(1234L, poi.getFeatureId().longValue());
		Assert.assertNotNull(poi.getLocation());
		
		//an
		Assert.assertEquals(2, poi.getAlternateNames().size());
		Assert.assertEquals("Pfarrkirche Heiliger Johannes der Täufer",poi.getAlternateNames().get(0).getName());
		Assert.assertEquals(AlternateNameSource.OPENSTREETMAP,poi.getAlternateNames().get(0).getSource());
		Assert.assertEquals("Parish church Saint John Baptist",poi.getAlternateNames().get(1).getName());
		Assert.assertEquals(AlternateNameSource.OPENSTREETMAP,poi.getAlternateNames().get(1).getSource());
		
		
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
		
    	String line= "W\t90139043\tPfarrkirche Heiliger Johannes der Täufer\tAT\tPfarrkirche Heiliger Johannes der Täufer___Parish church Saint John Baptist\t010100WRONGPOINT0020E61000000E6D653509482C40B01EF706AB514740\tplace_of_worship";
		
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
