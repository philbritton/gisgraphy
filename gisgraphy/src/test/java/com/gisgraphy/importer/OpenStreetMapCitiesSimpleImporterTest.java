package com.gisgraphy.importer;

import static com.gisgraphy.fulltext.Constants.ONLY_CITY_PLACETYPE;
import static com.gisgraphy.importer.OpenStreetMapCitiesSimpleImporter.MINIMUM_OUTPUT_STYLE;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.Adm;
import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.repository.IAdmDao;
import com.gisgraphy.domain.repository.ICityDao;
import com.gisgraphy.domain.repository.IIdGenerator;
import com.gisgraphy.domain.valueobject.GISSource;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.fulltext.Constants;
import com.gisgraphy.fulltext.FulltextQuery;
import com.gisgraphy.fulltext.FulltextResultsDto;
import com.gisgraphy.fulltext.IFullTextSearchEngine;
import com.gisgraphy.fulltext.SolrResponseDto;
import com.gisgraphy.helper.GeolocHelper;
import com.vividsolutions.jts.geom.Point;

public class OpenStreetMapCitiesSimpleImporterTest {

	@Test
	public void createNewCity() {
		OpenStreetMapCitiesSimpleImporter importer = new OpenStreetMapCitiesSimpleImporter();
		IIdGenerator idGenerator = EasyMock.createMock(IIdGenerator.class);
    	EasyMock.expect(idGenerator.getNextFeatureId()).andReturn(1234L);
    	EasyMock.replay(idGenerator);
    	importer.setIdGenerator(idGenerator);
		importer.idGenerator= idGenerator;
		
		Point location = GeolocHelper.createPoint(3D, 2D);
		City actual = importer.createNewCity("name","FR",location );
		Assert.assertEquals(GISSource.OPENSTREETMAP, actual.getSource());
		Assert.assertEquals(1234L, actual.getFeatureId().longValue());
		Assert.assertEquals("name", actual.getName());
		Assert.assertEquals("FR", actual.getCountryCode());
		Assert.assertEquals(location, actual.getLocation());
		
	}

	@Test
	public void getNearestCity(){
		OpenStreetMapCitiesSimpleImporter importer = new OpenStreetMapCitiesSimpleImporter();
		
		List<SolrResponseDto> results = new ArrayList<SolrResponseDto>();
		SolrResponseDto solrResponseDto = EasyMock.createNiceMock(SolrResponseDto.class);
		results.add(solrResponseDto);
		FulltextResultsDto mockResultDTO = EasyMock.createMock(FulltextResultsDto.class);
		EasyMock.expect(mockResultDTO.getResultsSize()).andReturn(1);
		EasyMock.expect(mockResultDTO.getResults()).andReturn(results);
		EasyMock.replay(mockResultDTO);
		
		
		String text = "toto";
		String countryCode = "FR";
		Point location = GeolocHelper.createPoint(3F, 4F);
		IFullTextSearchEngine mockfullFullTextSearchEngine = EasyMock.createMock(IFullTextSearchEngine.class);
		FulltextQuery query = new FulltextQuery(text, Pagination.ONE_RESULT, OpenStreetMapCitiesSimpleImporter.MINIMUM_OUTPUT_STYLE, ONLY_CITY_PLACETYPE, countryCode);
		query.withAllWordsRequired(false).withoutSpellChecking();
		
		EasyMock.expect(mockfullFullTextSearchEngine.executeQuery(query)).andReturn(mockResultDTO);
		EasyMock.replay(mockfullFullTextSearchEngine);
		
		importer.setFullTextSearchEngine(mockfullFullTextSearchEngine);
		
		SolrResponseDto actual = importer.getNearestCity(location, text, countryCode);
		Assert.assertEquals(solrResponseDto, actual);
		EasyMock.verify(mockfullFullTextSearchEngine);
	}
	
	@Test
	public void getNearestCityWithNullName(){
		OpenStreetMapCitiesSimpleImporter importer = new OpenStreetMapCitiesSimpleImporter();
		Point location = GeolocHelper.createPoint(3F, 4F);
		Assert.assertNull(importer.getNearestCity(location, "", "FR"));
		Assert.assertNull(importer.getNearestCity(location, " ", "FR"));
		Assert.assertNull(importer.getNearestCity(location, null, "FR"));
	}
	
	@Test
	public void getNearestCityWithNullLocation(){
		OpenStreetMapCitiesSimpleImporter importer = new OpenStreetMapCitiesSimpleImporter();
		Assert.assertNull(importer.getNearestCity(null, "paris", "FR"));
		
	}
	
	@Test
	public void getAdm(){
		OpenStreetMapCitiesSimpleImporter importer = new OpenStreetMapCitiesSimpleImporter();
		
		List<SolrResponseDto> results = new ArrayList<SolrResponseDto>();
		SolrResponseDto solrResponseDto = EasyMock.createNiceMock(SolrResponseDto.class);
		results.add(solrResponseDto);
		FulltextResultsDto mockResultDTO = EasyMock.createMock(FulltextResultsDto.class);
		EasyMock.expect(mockResultDTO.getResultsSize()).andReturn(1);
		EasyMock.expect(mockResultDTO.getResults()).andReturn(results);
		EasyMock.replay(mockResultDTO);
		
		
		String text = "toto";
		String countryCode = "FR";
		IFullTextSearchEngine mockfullFullTextSearchEngine = EasyMock.createMock(IFullTextSearchEngine.class);
		FulltextQuery query = new FulltextQuery(text, Pagination.ONE_RESULT, MINIMUM_OUTPUT_STYLE, Constants.ONLY_ADM_PLACETYPE, countryCode);
		query.withAllWordsRequired(false).withoutSpellChecking();
		
		EasyMock.expect(mockfullFullTextSearchEngine.executeQuery(query)).andReturn(mockResultDTO);
		EasyMock.replay(mockfullFullTextSearchEngine);
		
		importer.setFullTextSearchEngine(mockfullFullTextSearchEngine);
		
		SolrResponseDto actual = importer.getAdm(text, countryCode);
		Assert.assertEquals(solrResponseDto, actual);
		
		EasyMock.verify(mockfullFullTextSearchEngine);
		
	}
	
	@Test
	public void processWithUnknownCityAndKnownAdm(){
		
		final SolrResponseDto solrResponseDtoAdm = EasyMock.createMock(SolrResponseDto.class);
		EasyMock.expect(solrResponseDtoAdm.getFeature_id()).andReturn(4356L);
		EasyMock.expect(solrResponseDtoAdm.getName()).andReturn("admName");
		EasyMock.replay(solrResponseDtoAdm);
		OpenStreetMapCitiesSimpleImporter importer = new OpenStreetMapCitiesSimpleImporter(){
			protected SolrResponseDto getNearestCity(Point location, String name, String countryCode) {
				if (!name.equals("paris") || !countryCode.equals("FR")){
					throw new RuntimeException("the function is not called with the correct parameter");
				}
				return null;
			};
			
			@Override
			protected SolrResponseDto getAdm(String name, String countryCode) {
				if (!name.equals("Europe") || !countryCode.equals("FR")){
					throw new RuntimeException("the function is not called with the correct parameter");
				}
				return solrResponseDtoAdm;
			}
			@Override
			void savecity(City city) {
				super.savecity(city);
				Assert.assertEquals("city", city.getAmenity());
				Assert.assertEquals("paris", city.getName());
				Assert.assertEquals("FR", city.getCountryCode());
				Assert.assertEquals(48.2, city.getLatitude().doubleValue(),0.1);
				Assert.assertEquals(16.3, city.getLongitude().doubleValue(),0.1);
				
				Assert.assertEquals(1234L, city.getOpenstreetmapId().longValue());
				Assert.assertEquals(1000000L, city.getPopulation().longValue());
				Assert.assertEquals("admName", city.getAdm().getName());
				Assert.assertEquals("5678", city.getZipCodes().get(0).getCode());
				Assert.assertFalse("city shouldn't be a municipality because it is present NOT in both db",city.isMunicipality());
			}
		};
		
		ICityDao cityDao = EasyMock.createMock(ICityDao.class);
		City city=new City();
		city.setFeatureId(9876L);
		EasyMock.expect(cityDao.save(city)).andReturn(city);
		EasyMock.replay(cityDao);
		importer.setCityDao(cityDao);
		
		IIdGenerator idGenerator = EasyMock.createMock(IIdGenerator.class);
		EasyMock.expect(idGenerator.getNextFeatureId()).andReturn(9876L);
		EasyMock.replay(idGenerator);
		importer.setIdGenerator(idGenerator);
		
		IAdmDao admDao = EasyMock.createMock(IAdmDao.class);
		Adm adm = new Adm(2);
		adm.setName("admName");
		EasyMock.expect(admDao.getByFeatureId(4356L)).andReturn(adm);
		EasyMock.replay(admDao);
		importer.setAdmDao(admDao);
		
		
		String line= "N\t1234\tparis\tFR\t5678\t1000000\t0101000020E61000004070F0E0825F30405F65C80CAF1A4840\t\tcity\tEurope";
		
		importer.processData(line);
		
		EasyMock.verify(cityDao);
		EasyMock.verify(admDao);
		EasyMock.verify(idGenerator);
	}
	
	@Test
	public void processWithknownCityAndAdm(){
		final SolrResponseDto solrResponseDtoCity = EasyMock.createMock(SolrResponseDto.class);
		EasyMock.expect(solrResponseDtoCity.getFeature_id()).andReturn(123L);

		EasyMock.replay(solrResponseDtoCity);
		
		final SolrResponseDto solrResponseDtoAdm = EasyMock.createMock(SolrResponseDto.class);
		EasyMock.expect(solrResponseDtoAdm.getFeature_id()).andReturn(4356L);
		EasyMock.replay(solrResponseDtoAdm);
		OpenStreetMapCitiesSimpleImporter importer = new OpenStreetMapCitiesSimpleImporter(){
			protected SolrResponseDto getNearestCity(Point location, String name, String countryCode) {
				if (!name.equals("paris") || !countryCode.equals("FR")){
					throw new RuntimeException("the function is not called with the correct parameter");
				}
				return solrResponseDtoCity;
			};
			
			@Override
			protected SolrResponseDto getAdm(String name, String countryCode) {
				if (!name.equals("Europe") || !countryCode.equals("FR")){
					throw new RuntimeException("the function is not called with the correct parameter");
				}
				return solrResponseDtoAdm;
			}
			
			@Override
			void savecity(City city) {
				super.savecity(city);
				Assert.assertEquals("city", city.getAmenity());
				Assert.assertEquals("When a city is already present, we keep the name","initial name", city.getName());
				Assert.assertEquals("When a city is already present, we keep the countrycode","DE", city.getCountryCode());
				Assert.assertEquals("When a city is already present, we keep the lat",20D, city.getLatitude().doubleValue(),0.01);
				Assert.assertEquals("When a city is already present, we keep the long",30D, city.getLongitude().doubleValue(),0.01);
				
				Assert.assertEquals(1234L, city.getOpenstreetmapId().longValue());
				Assert.assertEquals(1000000L, city.getPopulation().longValue());
				Assert.assertEquals("admName", city.getAdm().getName());
				Assert.assertEquals("5678", city.getZipCodes().get(0).getCode());
				Assert.assertTrue("city should be a municipality because it is present in both db",city.isMunicipality());
			}
		};
		
		ICityDao cityDao = EasyMock.createMock(ICityDao.class);
		City city=new City();
		city.setName("initial name");
		city.setCountryCode("DE");
		city.setLocation(GeolocHelper.createPoint(30D, 20D));
		city.setFeatureId(123L);
		EasyMock.expect(cityDao.getByFeatureId(123L)).andReturn(city);
		EasyMock.expect(cityDao.save(city)).andReturn(city);
		EasyMock.replay(cityDao);
		importer.setCityDao(cityDao);
		
		
		IAdmDao admDao = EasyMock.createMock(IAdmDao.class);
		Adm adm = new Adm(2);
		adm.setName("admName");
		EasyMock.expect(admDao.getByFeatureId(4356L)).andReturn(adm);
		EasyMock.replay(admDao);
		importer.setAdmDao(admDao);
		
		
		String line= "N\t1234\tparis\tFR\t5678\t1000000\t0101000020E61000004070F0E0825F30405F65C80CAF1A4840\t\tcity\tEurope";
		
		importer.processData(line);
		
		EasyMock.verify(cityDao);
		EasyMock.verify(admDao);
	}
	
	@Test
	public void processWithunKnownCityAndUnknownAdm(){
		
		OpenStreetMapCitiesSimpleImporter importer = new OpenStreetMapCitiesSimpleImporter(){
			protected SolrResponseDto getNearestCity(Point location, String name, String countryCode) {
				if (!name.equals("paris") || !countryCode.equals("FR")){
					throw new RuntimeException("the function is not called with the correct parameter");
				}
				return null;
			};
			
			@Override
			protected SolrResponseDto getAdm(String name, String countryCode) {
				if (!name.equals("Europe") || !countryCode.equals("FR")){
					throw new RuntimeException("the function is not called with the correct parameter");
				}
				return null;
			}
			
			@Override
			void savecity(City city) {
				super.savecity(city);
				Assert.assertFalse("city shouldn't be a municipality because it is present NOT in both db",city.isMunicipality());
			}
		};
		
		ICityDao cityDao = EasyMock.createMock(ICityDao.class);
		City city=new City();
		city.setFeatureId(9876L);
		EasyMock.expect(cityDao.save(city)).andReturn(city);
		EasyMock.replay(cityDao);
		importer.setCityDao(cityDao);
		
		IIdGenerator idGenerator = EasyMock.createMock(IIdGenerator.class);
		EasyMock.expect(idGenerator.getNextFeatureId()).andReturn(9876L);
		EasyMock.replay(idGenerator);
		importer.setIdGenerator(idGenerator);
		
		
		IAdmDao admDao = EasyMock.createMock(IAdmDao.class);
		EasyMock.replay(admDao);
		importer.setAdmDao(admDao);
		
		
		String line= "N\t1234\tparis\tFR\t5678\t1000000\t0101000020E61000004070F0E0825F30405F65C80CAF1A4840\t\tcity\tEurope";
		
		importer.processData(line);
		
		EasyMock.verify(cityDao);
		EasyMock.verify(admDao);
		EasyMock.verify(idGenerator);
	}
	
	@Test
	public void processWithKnownCityAndUnknownAdm(){
		final SolrResponseDto solrResponseDtoCity = EasyMock.createMock(SolrResponseDto.class);
		EasyMock.expect(solrResponseDtoCity.getFeature_id()).andReturn(123L);
		EasyMock.replay(solrResponseDtoCity);
		
		OpenStreetMapCitiesSimpleImporter importer = new OpenStreetMapCitiesSimpleImporter(){
			protected SolrResponseDto getNearestCity(Point location, String name, String countryCode) {
				if (!name.equals("paris") || !countryCode.equals("FR")){
					throw new RuntimeException("the function is not called with the correct parameter");
				}
				return solrResponseDtoCity;
			};
			
			@Override
			protected SolrResponseDto getAdm(String name, String countryCode) {
				if (!name.equals("Europe") || !countryCode.equals("FR")){
					throw new RuntimeException("the function is not called with the correct parameter");
				}
				return null;
			}
			
			@Override
			void savecity(City city) {
				super.savecity(city);
				Assert.assertTrue("city should be a municipality because it is present in both db",city.isMunicipality());
			}
		};
		
		ICityDao cityDao = EasyMock.createMock(ICityDao.class);
		City city=new City();
		city.setFeatureId(123L);
		EasyMock.expect(cityDao.getByFeatureId(123L)).andReturn(city);
		EasyMock.expect(cityDao.save(city)).andReturn(city);
		EasyMock.replay(cityDao);
		importer.setCityDao(cityDao);
		
		
		IAdmDao admDao = EasyMock.createMock(IAdmDao.class);
		EasyMock.replay(admDao);
		importer.setAdmDao(admDao);
		
		
		String line= "N\t1234\tparis\tFR\t5678\t1000000\t0101000020E61000004070F0E0825F30405F65C80CAF1A4840\t\tcity\tEurope";
		
		importer.processData(line);
		
		EasyMock.verify(cityDao);
		EasyMock.verify(admDao);
	}
}
