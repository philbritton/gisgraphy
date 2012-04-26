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
package com.gisgraphy.importer;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.repository.IIdGenerator;
import com.gisgraphy.domain.repository.IOpenStreetMapDao;
import com.gisgraphy.domain.repository.ISolRSynchroniser;
import com.gisgraphy.domain.valueobject.GeolocResultsDto;
import com.gisgraphy.domain.valueobject.GisFeatureDistance;
import com.gisgraphy.domain.valueobject.GisFeatureDistanceFactory;
import com.gisgraphy.domain.valueobject.GisgraphyConfig;
import com.gisgraphy.domain.valueobject.ImporterStatus;
import com.gisgraphy.domain.valueobject.NameValueDTO;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.fulltext.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.geocoloc.IGeolocSearchEngine;
import com.gisgraphy.geoloc.GeolocQuery;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.service.IInternationalisationService;
import com.gisgraphy.street.StreetType;
import com.vividsolutions.jts.geom.Point;



public class OpenStreetMapSimpleImporterTest extends AbstractIntegrationHttpSolrTestCase {
    
    private IImporterProcessor openStreetMapImporter;
    
    private IOpenStreetMapDao openStreetMapDao;
    
    private IIdGenerator idGenerator;
    
    static boolean setupIsCalled = false;
    
   
  
    @Test
    public void testRollback() throws Exception {
    	OpenStreetMapSimpleImporter openStreetMapImporter = new OpenStreetMapSimpleImporter();
    	IOpenStreetMapDao openStreetMapDao = EasyMock.createMock(IOpenStreetMapDao.class);
    	EasyMock.expect(openStreetMapDao.getPersistenceClass()).andReturn(OpenStreetMap.class);
    	EasyMock.expect(openStreetMapDao.deleteAll()).andReturn(5);
    	EasyMock.replay(openStreetMapDao);
    	openStreetMapImporter.setOpenStreetMapDao(openStreetMapDao);
    	List<NameValueDTO<Integer>> deleted = openStreetMapImporter
    		.rollback();
    	assertEquals(1, deleted.size());
    	assertEquals(5, deleted.get(0).getValue().intValue());
	}
    
    @Test
    public void testImporterShouldImport() throws InterruptedException{
	System.out.println(openStreetMapImporter.getClass());
	openStreetMapImporter.process();
	/*if (openStreetMapImporter.getClass() != OpenStreetMapImporter.class){
	    Thread.sleep(10000L);
	}*/
	assertEquals(4L,openStreetMapDao.count());
	openStreetMapDao.getAll();
	Long firstIdAssigned = (idGenerator.getGid()-4+1);
	OpenStreetMap openStreetMap = openStreetMapDao.getByGid(firstIdAssigned);
	assertTrue("The oneWay attribute is not correct",openStreetMap.isOneWay());
	assertEquals("The countryCode is not correct ","FR",openStreetMap.getCountryCode());
	assertEquals("The is_in is not correct ","a city",openStreetMap.getIsIn());
	assertEquals("The openstreetmapId is not correct ",new Long(11),openStreetMap.getOpenstreetmapId());
	assertEquals("The streetType is not correct",StreetType.RESIDENTIAL, openStreetMap.getStreetType());
	assertEquals("The name is not correct","Bachlettenstrasse", openStreetMap.getName());
	assertEquals("The location->X is not correct ",((Point)GeolocHelper.convertFromHEXEWKBToGeometry("010100000006C82291A0521E4054CC39B16BC64740")).getX(), openStreetMap.getLocation().getX());
	assertEquals("The location->Y is not correct ",((Point)GeolocHelper.convertFromHEXEWKBToGeometry("010100000006C82291A0521E4054CC39B16BC64740")).getY(), openStreetMap.getLocation().getY());
	assertEquals("The length is not correct",0.00142246604529, openStreetMap.getLength());
	assertEquals("The shape is not correct ",GeolocHelper.convertFromHEXEWKBToGeometry("01020000000200000009B254CD6218024038E22428D9EF484075C93846B217024090A8AB96CFEF4840").toString(), openStreetMap.getShape().toString());
    }

   
    
    @Test
    public void testShouldBeSkipShouldReturnCorrectValue(){
	ImporterConfig importerConfig = new ImporterConfig();
	OpenStreetMapSimpleImporter openStreetMapImporter = new OpenStreetMapSimpleImporter();
	openStreetMapImporter.setImporterConfig(importerConfig);
	
	importerConfig.setOpenstreetmapImporterEnabled(false);
	Assert.assertTrue(openStreetMapImporter.shouldBeSkipped());
	
	importerConfig.setOpenstreetmapImporterEnabled(true);
	Assert.assertFalse(openStreetMapImporter.shouldBeSkipped());
		
    }
    
	@Test
	public void testshouldFillIsInFieldShouldReturnCorrectValue() {
		ImporterConfig importerConfig = new ImporterConfig();
		OpenStreetMapSimpleImporter openStreetMapImporter = new OpenStreetMapSimpleImporter();
		openStreetMapImporter.setImporterConfig(importerConfig);

		importerConfig.setGeonamesImporterEnabled(true);
		importerConfig.setOpenStreetMapFillIsIn(true);
		Assert.assertTrue(openStreetMapImporter.shouldFillIsInField());

		importerConfig.setGeonamesImporterEnabled(true);
		importerConfig.setOpenStreetMapFillIsIn(false);
		Assert.assertFalse(openStreetMapImporter.shouldFillIsInField());

		importerConfig.setGeonamesImporterEnabled(false);
		importerConfig.setOpenStreetMapFillIsIn(true);
		Assert.assertFalse(openStreetMapImporter.shouldFillIsInField());

		importerConfig.setGeonamesImporterEnabled(false);
		importerConfig.setOpenStreetMapFillIsIn(false);
		Assert.assertFalse(openStreetMapImporter.shouldFillIsInField());

	}
	
	@Test
	public void testGetNearestCityName(){
		ImporterConfig importerConfig = new ImporterConfig();
		importerConfig.setGeonamesImporterEnabled(true);
		importerConfig.setOpenStreetMapFillIsIn(true);
		OpenStreetMapSimpleImporter openStreetMapImporter = new OpenStreetMapSimpleImporter();
		openStreetMapImporter.setImporterConfig(importerConfig);
		final String  cityName= "cityName";
		final Integer population = 123;
		final GisFeature city = new City();
		
		IGeolocSearchEngine geolocSearchEngine = EasyMock.createMock(IGeolocSearchEngine.class);
		Point location= GeolocHelper.createPoint(2F, 3F);
		GeolocQuery query  = (GeolocQuery) new GeolocQuery(location).withPlaceType(City.class).withDistanceField(false).withPagination(Pagination.ONE_RESULT);
		GeolocResultsDto resultsDto = new GeolocResultsDto() {
			@Override
			public List<GisFeatureDistance> getResult() {
				List<GisFeatureDistance> list = new ArrayList<GisFeatureDistance>();
				city.setName(cityName);
				city.setPopulation(population);
				GisFeatureDistanceFactory factory = new GisFeatureDistanceFactory();
				GisFeatureDistance gisFeatureDistance = factory.fromGisFeature(city,1D);
				list.add(gisFeatureDistance);
				return list;
			}
			@Override
			public int getNumFound() {
				return 1;
			}
		};
		EasyMock.expect(geolocSearchEngine.executeQuery(query)).andReturn(resultsDto);
		EasyMock.replay(geolocSearchEngine);
		
		openStreetMapImporter.setGeolocSearchEngine(geolocSearchEngine);
		
		GisFeatureDistance actual = openStreetMapImporter.getNearestCityName(location);
		Assert.assertEquals(cityName, actual.getName());
		Assert.assertEquals(population, actual.getPopulation());
		EasyMock.verify(geolocSearchEngine);
		
	}
    
   
    
    @Test
    public void testProcessLineWithBadShapeShouldNotTryToSaveLine(){
	String line = "		010100000029F2C9850F79E4BFFCAEFE473CE14740	19406.7343711266	FR	8257014	road	false	BADSHAPE";
	OpenStreetMapSimpleImporter importer = new OpenStreetMapSimpleImporter();
	IOpenStreetMapDao dao = EasyMock.createMock(IOpenStreetMapDao.class);
	//now we simulate the fact that the dao should not be called
	EasyMock.expect(dao.save((OpenStreetMap)EasyMock.anyObject())).andThrow(new RuntimeException());
	EasyMock.replay(dao);
	importer.setOpenStreetMapDao(dao);
	IIdGenerator idGenerator = EasyMock.createMock(IIdGenerator.class);
    	EasyMock.expect(idGenerator.getNextGId()).andReturn(1L);
    	EasyMock.replay(idGenerator);
    	importer.setIdGenerator(idGenerator);
	importer.processData(line);
	EasyMock.verify(idGenerator);
    }
    
    @Test
    public void testImportWithErrors(){
	OpenStreetMapSimpleImporter importer = createImporterThatThrows();
	try {
	    importer.process();
	    fail("The import should have failed");
	} catch (Exception ignore) {
	    //ok
	}
	Assert.assertNotNull("status message is not set",importer.getStatusMessage() );
	Assert.assertFalse("status message should not be empty",importer.getStatusMessage().trim().length()==0 );
	Assert.assertEquals(ImporterStatus.ERROR, importer.getStatus());
    }

    private OpenStreetMapSimpleImporter createImporterThatThrows() {
	OpenStreetMapSimpleImporter importer = new OpenStreetMapSimpleImporter(){
	    @Override
	    public boolean shouldBeSkipped() {
	        return false;
	    }
	    
	    @Override
	    public long getNumberOfLinesToProcess() {
	        return 2L;
	    }
	    
	    @Override
	    protected void tearDown() {
	       return;
	    }
	};
	
	//ImporterConfig config = new ImporterConfig();
	//config.setOpenStreetMapDir(this.openStreetMapImporter.importerConfig.getOpenStreetMapDir());
	IOpenStreetMapDao dao = EasyMock.createNiceMock(IOpenStreetMapDao.class);
	//now we simulate the fact that the dao should not be called
	EasyMock.expect(dao.save((OpenStreetMap)EasyMock.anyObject())).andThrow(new RuntimeException("message"));
	EasyMock.replay(dao);
	importer.setOpenStreetMapDao(dao);
	importer.setImporterConfig(new ImporterConfig());
	//importer.setTransactionManager(openStreetMapImporter.transactionManager);
	return importer;
    }
    
    @Test
    public void testSetup(){
    	OpenStreetMapSimpleImporter importer = new OpenStreetMapSimpleImporter();
    	IIdGenerator idGenerator = EasyMock.createMock(IIdGenerator.class);
    	idGenerator.sync();
    	EasyMock.replay(idGenerator);
    	importer.setIdGenerator(idGenerator);
    	
    	importer.setup();
    	EasyMock.verify(idGenerator);
    }
    
    @Test
    public void testSetupIsCalled(){
    	
    	OpenStreetMapSimpleImporterTest.setupIsCalled = false;
    	OpenStreetMapSimpleImporter importer = new OpenStreetMapSimpleImporter(){
    		@Override
    		protected void setup() {
    			OpenStreetMapSimpleImporterTest.setupIsCalled = true;
    		}
    		@Override
    		protected void tearDown() {
    			return;
    		}
    		
    		@Override
    		public long getNumberOfLinesToProcess() {
    			return 0;
    		}
    		
    		@Override
    		public boolean shouldBeSkipped() {
    			return false;
    		}
    		
    		@Override
    		protected File[] getFiles() {
    			return new File[]{};
    		}
    	};
    	importer.process();
    	assertTrue(OpenStreetMapSimpleImporterTest.setupIsCalled);
    }
    
    
    @Required
    public void setOpenStreetMapDao(IOpenStreetMapDao openStreetMapDao) {
        this.openStreetMapDao = openStreetMapDao;
    }

    @Required
    public void setOpenStreetMapImporter(IImporterProcessor openStreetMapImporter) {
        this.openStreetMapImporter = openStreetMapImporter;
    }
    
    @Test
    public void testTearDown(){
	if (GisgraphyConfig.PARTIAL_SEARH_EXPERIMENTAL){
	OpenStreetMapSimpleImporter importer = new OpenStreetMapSimpleImporter(){
	    //simulate an error
	    public boolean shouldBeSkipped() {throw new RuntimeException("errormessage");};
	};
	IOpenStreetMapDao dao = createMock(IOpenStreetMapDao.class);
	openStreetMapDao.createSpatialIndexes();
	dao.clearPartialSearchName();
	EasyMock.replay(dao);
	importer.setOpenStreetMapDao(dao);
	
	ISolRSynchroniser solRSynchroniser = EasyMock.createMock(ISolRSynchroniser.class);
	solRSynchroniser.optimize();
	EasyMock.replay(solRSynchroniser);
	importer.setSolRSynchroniser(solRSynchroniser);
	
		IInternationalisationService internationalisationService = createMock(IInternationalisationService.class);
		expect(internationalisationService.getString("import.message.createIndex")).andReturn("localizedString");
    	expect(internationalisationService.getString("import.fulltext.optimize")).andReturn("localizedString");
    	replay(internationalisationService);
    	importer.setInternationalisationService(internationalisationService);
	
	    try {
		importer.process();
	    } catch (Exception ignore) {
		
	    }
	    Assert.assertTrue(importer.getStatusMessage().contains("errormessage"));
	    EasyMock.verify(dao);
	    EasyMock.verify(internationalisationService);
	    EasyMock.verify(solRSynchroniser);
    }
    }
	  
    @Test
    public void testPplxToPPL(){
    	OpenStreetMapSimpleImporter importer = new OpenStreetMapSimpleImporter();
    	Assert.assertEquals(null,importer.pplxToPPL(null));
    	Assert.assertEquals("Paris",importer.pplxToPPL("Paris"));
    	Assert.assertEquals("Paris",importer.pplxToPPL("Paris 10 Entrepôt"));
    	Assert.assertEquals("Marseille",importer.pplxToPPL("Marseille 01"));
    }
    
    
    @Required
    public void setIdGenerator(IIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }
}
