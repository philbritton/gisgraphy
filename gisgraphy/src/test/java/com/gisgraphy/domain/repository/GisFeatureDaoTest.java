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
package com.gisgraphy.domain.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.geoloc.entity.Adm;
import com.gisgraphy.domain.geoloc.entity.AlternateName;
import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.geoloc.entity.ZipCode;
import com.gisgraphy.domain.valueobject.AlternateNameSource;
import com.gisgraphy.domain.valueobject.GisFeatureDistance;
import com.gisgraphy.fulltext.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.importer.ImporterConfig;
import com.gisgraphy.test.GisgraphyTestHelper;

public class GisFeatureDaoTest extends AbstractIntegrationHttpSolrTestCase {

    private IGisFeatureDao gisFeatureDao;

    private ICityDao cityDao;

    private IAdmDao admDao;

    private ICountryDao countryDao;

    private IAlternateNameDao alternateNameDao;

    @Resource
    private GisgraphyTestHelper geolocTestHelper;

    /*
     * test delete
     */

    @Test
    public void testRemoveWithNullShouldThrows() {
	try {
	    this.gisFeatureDao.remove(null);
	    fail();
	} catch (IllegalArgumentException e) {
	}
    }

    @Test
    public void testRemoveACityWithGisFeatureDaoShouldRemoveTheCityAndTheInheritedGisFeature() {

	// expected : the city is removed

	GisFeature gisFeature = GisgraphyTestHelper.createCity("cityGisFeature",
		null, null, new Random().nextLong());
	City paris = new City(gisFeature);
	// save city
	City savedParis = this.cityDao.save(paris);
	// chek city is well saved
	Long id = savedParis.getId();
	City retrievedParis = this.cityDao.get(savedParis.getId());
	assertNotNull(retrievedParis);
	assertEquals(paris.getId(), retrievedParis.getId());

	// remove city
	this.gisFeatureDao.remove(retrievedParis);

	// check city is removed
	City retrievedParisafterRemove = this.cityDao.get(id);
	assertEquals(null, retrievedParisafterRemove);

	// check gisFeature is remove
	GisFeature savedGisFeatureafterRemove = this.gisFeatureDao.get(id);
	assertNull(savedGisFeatureafterRemove);
    }

    @Test
    public void testRemoveACityCastInGisFeatureWithGisFeatureDaoShouldREmoveTheCity() {
	GisFeature gisFeature = GisgraphyTestHelper.createCity("cityGisFeature",
		null, null, new Random().nextLong());
	City paris = new City(gisFeature);

	// save city
	City savedParis = this.cityDao.save(paris);

	// chek city is well saved
	Long id = savedParis.getId();
	City retrievedParis = this.cityDao.get(savedParis.getId());
	assertNotNull(retrievedParis);
	assertEquals(paris.getId(), retrievedParis.getId());

	// remove city
	this.gisFeatureDao.remove((GisFeature) retrievedParis);
	// savedParis.setGisFeature(null);
	// check city is removed

	City retrievedParisafterRemove = this.cityDao.get(id);
	assertEquals(null, retrievedParisafterRemove);

	// check gisFeature is remove
	GisFeature savedGisFeatureafterRemove = this.gisFeatureDao.get(id);
	assertNull(savedGisFeatureafterRemove);

    }

    @Test
    public void testRemoveGisFeatureWhichIsAnAdm2() {
	// see admDaotest.testDeleteAdmShouldDeleteAdm()
    }

    @Test
    public void testDeleteAllListShouldThrowsIfListIsNull() {
	try {
	    this.gisFeatureDao.deleteAll(null);
	    fail();
	} catch (IllegalArgumentException e) {

	}
    }

    @Test
    public void testDeleteAllListShouldNotThrowsForAnEmptyList() {
	try {
	    this.gisFeatureDao.deleteAll(new ArrayList<GisFeature>());

	} catch (IllegalArgumentException e) {
	    fail();
	}
    }

    @Test
    public void testDeleteAllListShouldDeleteTheSpecifiedElements() {
	GisFeature gisFeature = GisgraphyTestHelper.createCity("cityGisFeature",
		null, null, new Random().nextLong());
	City paris = new City(gisFeature);

	GisFeature gisFeature2 = GisgraphyTestHelper.createCity("cityGisFeature2",
		null, null, new Random().nextLong());
	City paris2 = new City(gisFeature2);

	// save cities
	City savedParis = this.cityDao.save(paris);
	City savedParis2 = this.cityDao.save(paris2);

	// chek cities are well saved
	City retrievedParis = this.cityDao.get(savedParis.getId());
	assertNotNull(retrievedParis);
	assertEquals(savedParis.getId(), retrievedParis.getId());

	City retrievedParis2 = this.cityDao.get(savedParis2.getId());
	assertNotNull(retrievedParis2);
	assertEquals(savedParis2.getId(), retrievedParis2.getId());

	List<GisFeature> listToDelete = new ArrayList<GisFeature>();
	listToDelete.add(retrievedParis);
	this.gisFeatureDao.deleteAll(listToDelete);

	List<GisFeature> stillStored = this.gisFeatureDao.getAll();
	assertNotNull(stillStored);
	assertEquals(1, stillStored.size());
	assertEquals(savedParis2, stillStored.get(0));

    }

    @Test
    public void testDeleteALLShouldDeleteAlltheElements() {
	GisFeature paris = GisgraphyTestHelper.createGisFeature("GisFeature",
		null, null, new Random().nextLong());

	GisFeature gisFeature2 = GisgraphyTestHelper.createCity("cityGisFeature2",
		null, null, new Random().nextLong());
	City paris2 = new City(gisFeature2);

	// save cities
	GisFeature savedParis = this.gisFeatureDao.save(paris);
	City savedParis2 = this.cityDao.save(paris2);

	// chek cities are well saved
	GisFeature retrievedParis = this.gisFeatureDao.get(savedParis.getId());
	assertNotNull(retrievedParis);
	assertEquals(savedParis.getId(), retrievedParis.getId());

	City retrievedParis2 = this.cityDao.get(savedParis2.getId());
	assertNotNull(retrievedParis2);
	assertEquals(savedParis2.getId(), retrievedParis2.getId());

	assertEquals(1, this.cityDao.deleteAll());

	List<City> stillStoredCity = this.cityDao.getAll();
	assertNotNull(stillStoredCity);
	assertEquals(0, stillStoredCity.size());

	List<GisFeature> stillStoredGis = this.gisFeatureDao.getAll();
	assertNotNull(stillStoredGis);
	assertEquals(1, stillStoredGis.size());
    }

    @Test
    public void testDeleteAdmShouldNotDeleteTheGisFeaturesContainedInCascade() {
	// save Adm
	Adm adm = GisgraphyTestHelper.createAdm("adm", "FR", "A1", "B2", "C3",
		"D4", null, 4);
	Adm savedAdm = this.admDao.save(adm);
	assertNotNull(savedAdm.getId());
	// check adm1 is saved
	Adm retrievedAdm = this.admDao.get(savedAdm.getId());
	assertEquals(savedAdm, retrievedAdm);
	assertEquals(savedAdm.getId(), retrievedAdm.getId());

	// creategisFeatureand set his Adm
	GisFeature gisFeature = GisgraphyTestHelper.createCity("paris", 1.3F, 45F,
		null);
	gisFeature.setAdm(retrievedAdm);

	// save gisFeature
	GisFeature savedGisFeature = gisFeatureDao.save(gisFeature);

	// check it is saved
	GisFeature retrievedGisFeature = this.gisFeatureDao.get(savedGisFeature
		.getId());
	assertNotNull(retrievedGisFeature);
	assertEquals(savedGisFeature, retrievedGisFeature);

    }

    @Test
    public void testDeleteAllExceptAdmAndCountries() {
	geolocTestHelper.createAndSaveCityWithFullAdmTreeAndCountry(3L);
	GisFeature gisFeatureWithNullFeatureCode = GisgraphyTestHelper
		.createGisFeature("gis", 3F, 4F, 4L);
	gisFeatureDao.save(gisFeatureWithNullFeatureCode);
	GisFeature gisFeatureWithNotNullFeatureCode = GisgraphyTestHelper
		.createGisFeature("gis", 3F, 4F, 5L);
	gisFeatureWithNotNullFeatureCode.setFeatureClass("A");
	gisFeatureWithNotNullFeatureCode.setFeatureCode("B");
	gisFeatureDao.save(gisFeatureWithNotNullFeatureCode);
	// check 3 adm, country,city, and gisFeature are saved
	assertEquals(1, countryDao.count());
	assertEquals(7, gisFeatureDao.count());
	// 1 city + 1 gis with null FeatureCode + 1 GIS with not null featureCode
	assertEquals(3, gisFeatureDao.deleteAllExceptAdmsAndCountries());
	assertEquals(3, admDao.count());
	assertEquals(1, countryDao.count());
	// 3 adm + 1 country
	assertEquals(4, gisFeatureDao.count());
	assertEquals(1, countryDao.count());
	assertEquals(0, cityDao.count());

    }

    /*
     * test save
     */

    @Test
    public void testSaveCityCastInGisFeatureShouldSaveTheCity() {
	GisFeature gisFeature = GisgraphyTestHelper.createCity("cityGisFeature",
		null, null, new Random().nextLong());
	City paris = new City(gisFeature);
	// save city
	GisFeature savedParis = this.gisFeatureDao.save((GisFeature) paris);
	// chek city is well saved
	savedParis.getId();
	City retrievedParis = this.cityDao.get(savedParis.getId());
	assertNotNull(retrievedParis);
	assertEquals(paris.getId(), retrievedParis.getId());
    }

    @Test
    public void testSaveWithNullShouldThrows() {
	try {
	    this.gisFeatureDao.save(null);
	    fail();
	} catch (IllegalArgumentException e) {
	}
    }

    @Test
    public void testSaveCityWithGisFeatureDaoShouldSaveTheCity() {
	GisFeature gisFeature = GisgraphyTestHelper.createCity("cityGisFeature",
		null, null, new Random().nextLong());
	City paris = new City(gisFeature);
	// save city
	GisFeature savedParis = this.gisFeatureDao.save(paris);
	// chek city is well saved
	City retrievedParis = this.cityDao.get(savedParis.getId());
	assertNotNull(retrievedParis);
	assertEquals(paris.getId(), retrievedParis.getId());
    }

    @Test
    public void testSaveShouldSaveTheAlternateNamesInCascade() {
	int nbalternateNames = 3;
	GisFeature gisFeature = GisgraphyTestHelper
		.createGisFeatureWithAlternateNames("paris", nbalternateNames);
	assertNotNull(gisFeature.getAlternateNames());
	assertEquals(3, gisFeature.getAlternateNames().size());
	GisFeature saved = this.gisFeatureDao.save(gisFeature);
	GisFeature retrieved = this.gisFeatureDao.get(saved.getId());
	assertNotNull(retrieved);
	assertEquals(gisFeature.getId(), retrieved.getId());
	assertNotNull(retrieved.getAlternateNames());
	assertEquals(nbalternateNames, retrieved.getAlternateNames().size());
    }

    // test get

    @Test
    public void testgetNearestAndDistanceFromGisFeatureShouldTakeTheSpecifiedClassIntoAccount() {
	City p1 = GisgraphyTestHelper.createCity("paris", 48.86667F, 2.3333F, 1L);
	City p2 = GisgraphyTestHelper.createCity("bordeaux", 44.83333F, -0.56667F,
		3L);
	City p3 = GisgraphyTestHelper.createCity("goussainville", 49.01667F,
		2.46667F, 2L);

	this.cityDao.save(p1);
	this.cityDao.save(p2);
	this.cityDao.save(p3);

	List<GisFeatureDistance> results = this.gisFeatureDao
		.getNearestAndDistanceFromGisFeature(p1, 1000000,true,
			GisFeature.class);
	assertEquals(2, results.size());

	results = this.gisFeatureDao.getNearestAndDistanceFromGisFeature(p1,
		1000000,true, City.class);
	assertEquals(2, results.size());

	GisFeature p4 = GisgraphyTestHelper.createGisFeature("test", 49.01668F,
		2.46667F, 4L);
	this.gisFeatureDao.save(p4);

	results = this.gisFeatureDao.getNearestAndDistanceFromGisFeature(p1,
		1000000, true,GisFeature.class);
	assertEquals(3, results.size());

	results = this.gisFeatureDao.getNearestAndDistanceFromGisFeature(p1,
		1000000,true, City.class);
	assertEquals(2, results.size());

    }

    @Test
    public void testgetNearestAndDistanceFromGisFeatureShouldThrowsIfGisFeatureIsNull() {

	try {
	    this.gisFeatureDao.getNearestAndDistanceFromGisFeature(null,
		    1000000,true, GisFeature.class);
	    fail("getNearestAndDistanceFromGisFeature should throws if gisFeature is null");
	} catch (IllegalArgumentException e) {

	}

    }

    @Test
    public void testgetNearestAndDistanceFromGisFeatureShouldfilterMunicipality() {
	City p1 = GisgraphyTestHelper.createCity("paris", 48.86667F, 2.3333F, 1L);
	City p2 = GisgraphyTestHelper.createCity("bordeaux", 44.83333F, -0.56667F,
		3L);
	p2.setMunicipality(true);
	City p3 = GisgraphyTestHelper.createCity("goussainville", 49.01667F,
		2.46667F, 2L);

	this.gisFeatureDao.save(p1);
	this.gisFeatureDao.save(p2);
	this.gisFeatureDao.save(p3);
	// for city dao
	List<GisFeatureDistance> results  = this.gisFeatureDao.getNearestAndDistanceFromGisFeature(p1,
		1000000, 0, 10, true,City.class, false);
	assertEquals(2, results.size());

	results = this.gisFeatureDao.getNearestAndDistanceFromGisFeature(p1,
		1000000, 0, 10, true,City.class, true);
	assertEquals(1, results.size());
	assertEquals(p2.getName(), results.get(0).getName());

    }
    
    @Test
    public void testgetNearestAndDistanceFromGisFeatureShouldPaginate() {
	City p1 = GisgraphyTestHelper.createCity("paris", 48.86667F, 2.3333F, 1L);
	City p2 = GisgraphyTestHelper.createCity("bordeaux", 44.83333F, -0.56667F,
		3L);
	City p3 = GisgraphyTestHelper.createCity("goussainville", 49.01667F,
		2.46667F, 2L);

	this.gisFeatureDao.save(p1);
	this.gisFeatureDao.save(p2);
	this.gisFeatureDao.save(p3);
	// for city dao
	List<GisFeatureDistance> results = this.gisFeatureDao
		.getNearestAndDistanceFromGisFeature(p1, 1000000, 1, 5,true);
	assertEquals(2, results.size());
	assertEquals(p3.getName(), results.get(0).getName());
	assertEquals(p2.getName(), results.get(1).getName());

	results = this.gisFeatureDao.getNearestAndDistanceFromGisFeature(p1,
		1000000, 2, 5, true,City.class, false);
	assertEquals(1, results.size());
	assertEquals(p2.getName(), results.get(0).getName());

	results = this.gisFeatureDao.getNearestAndDistanceFromGisFeature(p1,
		1000000, 1, 1, true,City.class, false);
	assertEquals(1, results.size());
	assertEquals(p3.getName(), results.get(0).getName());

	results = this.gisFeatureDao.getNearestAndDistanceFromGisFeature(p1,
		1000000, 0, 1,true, City.class, false);
	assertEquals(1, results.size());
	assertEquals(p3.getName(), results.get(0).getName());

	results = this.gisFeatureDao.getNearestAndDistanceFromGisFeature(p1,
		1000000, 1, 0,true, City.class, false);
	assertEquals(2, results.size());
	assertEquals(p3.getName(), results.get(0).getName());
	assertEquals(p2.getName(), results.get(1).getName());

    }
    
    @Test
    public void testgetNearestAndDistanceFromGisFeatureShouldNotCalculateTheDistanceIfincludeDistanceFieldIsFalse() {
	City p1 = GisgraphyTestHelper.createCity("paris", 48.86667F, 2.3333F, 1L);
	City p2 = GisgraphyTestHelper.createCity("bordeaux", 44.83333F, -0.56667F,
		3L);
	City p3 = GisgraphyTestHelper.createCity("goussainville", 49.01667F,
		2.46667F, 2L);

	this.gisFeatureDao.save(p1);
	this.gisFeatureDao.save(p2);
	this.gisFeatureDao.save(p3);
	// for city dao
	List<GisFeatureDistance> results = this.gisFeatureDao
		.getNearestAndDistanceFromGisFeature(p1, 1000000, 1, 5,true);
	assertEquals(2, results.size());
	assertEquals(p3.getName(), results.get(0).getName());
	assertEquals(p2.getName(), results.get(1).getName());


	results = this.gisFeatureDao.getNearestAndDistanceFromGisFeature(p1,
		1000000, 1, 0, false, City.class, false);
	assertEquals(2, results.size());
	
	assertNull("The distance should be null if includeDistanceField is false", results.get(0).getDistance());
	assertNull("The distance should be null if includeDistanceField is false", results.get(1).getDistance());

    }

    @Test
    public void testgetNearestAndDistanceFromShouldTakeTheSpecifiedClassIntoAccount() {
	City p1 = GisgraphyTestHelper.createCity("paris", 48.86667F, 2.3333F, 1L);
	City p2 = GisgraphyTestHelper.createCity("bordeaux", 44.83333F, -0.56667F,
		3L);
	City p3 = GisgraphyTestHelper.createCity("goussainville", 49.01667F,
		2.46667F, 2L);

	this.cityDao.save(p1);
	this.cityDao.save(p2);
	this.cityDao.save(p3);

	List<GisFeatureDistance> results = this.gisFeatureDao
		.getNearestAndDistanceFrom(p1.getLocation(), 1000000,true,
			GisFeature.class);
	assertEquals(3, results.size());

	results = this.gisFeatureDao.getNearestAndDistanceFrom(
		p1.getLocation(), 1000000, true,City.class);
	assertEquals(3, results.size());

	GisFeature p4 = GisgraphyTestHelper.createGisFeature("test", 49.01668F,
		2.46667F, 4L);
	this.gisFeatureDao.save(p4);

	results = this.gisFeatureDao.getNearestAndDistanceFrom(
		p1.getLocation(), 1000000, true,GisFeature.class);
	assertEquals(4, results.size());

	results = this.gisFeatureDao.getNearestAndDistanceFrom(
		p1.getLocation(), 1000000, true, City.class);
	assertEquals(3, results.size());

    }
    
    @Test
    public void testgetNearestAndDistanceFromShouldFilterIsMunicipality() {
    	City p1 = GisgraphyTestHelper.createCity("paris", 48.86667F, 2.3333F, 1L);
    	City p2 = GisgraphyTestHelper.createCity("bordeaux", 44.83333F, -0.56667F,
    		3L);
    	p2.setMunicipality(true);
    	City p3 = GisgraphyTestHelper.createCity("goussainville", 49.01667F,
    		2.46667F, 2L);

	this.gisFeatureDao.save(p1);
	this.gisFeatureDao.save(p2);
	this.gisFeatureDao.save(p3);
	// for city dao
	List<GisFeatureDistance> results = this.gisFeatureDao
		.getNearestAndDistanceFrom(p1.getLocation(), 1000000, 1, 5, true, false);
	assertEquals(3, results.size());
	assertEquals(p1.getName(), results.get(0).getName());
	assertEquals(p3.getName(), results.get(1).getName());
	assertEquals(p2.getName(), results.get(2).getName());

	results = this.gisFeatureDao
			.getNearestAndDistanceFrom(p1.getLocation(), 1000000, 1, 5, true, true);
	assertEquals(1, results.size());
	assertEquals(p2.getName(), results.get(0).getName());

    }

    @Test
    public void testgetNearestAndDistanceFromShouldPaginate() {
	City p1 = GisgraphyTestHelper.createCity("paris", 48.86667F, 2.3333F, 1L);
	City p2 = GisgraphyTestHelper.createCity("bordeaux", 44.83333F, -0.56667F,
		3L);
	City p3 = GisgraphyTestHelper.createCity("goussainville", 49.01667F,
		2.46667F, 2L);

	this.gisFeatureDao.save(p1);
	this.gisFeatureDao.save(p2);
	this.gisFeatureDao.save(p3);
	// for city dao
	List<GisFeatureDistance> results = this.gisFeatureDao
		.getNearestAndDistanceFrom(p1.getLocation(), 1000000, 1, 5, true, false);
	assertEquals(3, results.size());
	assertEquals(p1.getName(), results.get(0).getName());
	assertEquals(p3.getName(), results.get(1).getName());
	assertEquals(p2.getName(), results.get(2).getName());

	results = this.gisFeatureDao.getNearestAndDistanceFrom(
		p1.getLocation(), 1000000, 2, 5, true,City.class);
	assertEquals(2, results.size());
	assertEquals(p3.getName(), results.get(0).getName());
	assertEquals(p2.getName(), results.get(1).getName());

	results = this.gisFeatureDao.getNearestAndDistanceFrom(
		p1.getLocation(), 1000000, 1, 1, true,City.class);
	assertEquals(1, results.size());
	assertEquals(p1.getName(), results.get(0).getName());

	results = this.gisFeatureDao.getNearestAndDistanceFrom(
		p1.getLocation(), 1000000, 0, 1,true, City.class);
	assertEquals(1, results.size());
	assertEquals(p1.getName(), results.get(0).getName());

	results = this.gisFeatureDao.getNearestAndDistanceFrom(
		p1.getLocation(), 1000000, 1, 0, true,City.class);
	assertEquals(3, results.size());
	assertEquals(p1.getName(), results.get(0).getName());
	assertEquals(p3.getName(), results.get(1).getName());
	assertEquals(p2.getName(), results.get(2).getName());

    }
    
    
    @Test
    public void testgetNearestAndDistanceFromhouldNotCalculateTheDistanceIfincludeDistanceFieldIsFalse() {
	City p1 = GisgraphyTestHelper.createCity("paris", 48.86667F, 2.3333F, 1L);
	City p2 = GisgraphyTestHelper.createCity("bordeaux", 44.83333F, -0.56667F,
		3L);
	City p3 = GisgraphyTestHelper.createCity("goussainville", 49.01667F,
		2.46667F, 2L);

	this.gisFeatureDao.save(p1);
	this.gisFeatureDao.save(p2);
	this.gisFeatureDao.save(p3);
		

	List<GisFeatureDistance> results = this.gisFeatureDao.getNearestAndDistanceFrom(
		p1.getLocation(), 1000000, 0, 4, false,City.class);
	assertEquals(3, results.size());
	assertNull("The distance should be null if includeDistanceField is false", results.get(0).getDistance());
	assertNull("The distance should be null if includeDistanceField is false", results.get(1).getDistance());
	assertNull("The distance should be null if includeDistanceField is false", results.get(2).getDistance());

    }

    @Test
    public void testGetEagerShouldLoadAlternateNamesAndAdm() {
	Long featureId = 1001L;
	City gisFeature = GisgraphyTestHelper.createCity("Saint-André", 1.5F, 2F,
		featureId);
	AlternateName alternateName = new AlternateName();
	alternateName.setName("alteré");
	alternateName.setGisFeature(gisFeature);
	alternateName.setSource(AlternateNameSource.ALTERNATENAMES_FILE);
	gisFeature.addAlternateName(alternateName);
	City paris = new City(gisFeature);
	paris.addZipCode(new ZipCode("50263"));

	Adm admParent = GisgraphyTestHelper.createAdm("admparent", "FR", "A1",
		"B2", "C3", null, null, 3);

	Adm p = this.admDao.save(admParent);
	paris.setAdm(p);

	this.cityDao.save(paris);
	this.cityDao.flushAndClear();
	this.admDao.flushAndClear();
	// test in non eagerMode
	City retrieved = this.cityDao.get(paris.getId());
	this.cityDao.flushAndClear();
	this.admDao.flushAndClear();
	try {
	    assertEquals(1, retrieved.getAlternateNames().size());
	    fail("without eager mode, getAlternateNames should throw");
	} catch (RuntimeException e) {

	}
	try {
	    assertEquals("C3", retrieved.getAdm().getAdm3Code());
	    fail("without eager mode, getAdm should throw");
	} catch (RuntimeException e) {
	}
	// test In Eager Mode
	retrieved = this.cityDao.getEager(paris.getId());
	this.cityDao.flushAndClear();
	this.admDao.flushAndClear();
	try {
	    assertEquals(1, retrieved.getAlternateNames().size());
	} catch (RuntimeException e) {
	    fail("with eager mode, getAlternateNames should not throw");
	}
	try {
	    assertEquals("C3", retrieved.getAdm().getAdm3Code());
	} catch (RuntimeException e) {
	    fail("with eager mode, getAdm should not throw");
	}
    }

    @Test
    public void testlistAllFeaturesFromTextShouldReturnResults() {
	City city = GisgraphyTestHelper.createCityAtSpecificPoint("sèvres", 1.5F,
		1.6F);
	city.setFeatureId(3L);
	this.gisFeatureDao.save(city);
	this.solRSynchroniser.commit();
	this.solRSynchroniser.optimize();// to avoid too many open files
	// http://grep.codeconsult.ch/2006/07/18/lucene-too-many-open-files-explained/
	List<GisFeature> results = this.gisFeatureDao.listAllFeaturesFromText(
		"sèvres", true);
	assertNotNull("The fulltext search engine should not return null",
		results);
	assertTrue("size should be 1 but is " + results.size(),
		results.size() == 1);
	assertEquals(
		"The fulltext search engine does not return the expected result",
		new Long(3), results.get(0).getFeatureId());

    }

    @Test
    public void testGetShouldThrowsIfIdIsNull() {
	try {
	    this.gisFeatureDao.get(null);
	    fail();
	} catch (IllegalArgumentException e) {
	}
    }

    @Test
    public void testExistShouldThrowsIfIdIsNull() {
	try {
	    this.gisFeatureDao.exists(null);
	    fail();
	} catch (IllegalArgumentException e) {
	}
    }

    @Test
    public void testGetAllPaginateShouldNeverReturnNull() {
	assertNotNull(this.gisFeatureDao.getAllPaginate(1, 5));
    }

    @Test
    public void testGetByFeatureIdShouldRetrieveTheCorrectGisfeature() {
	Long featureId = 1001L;
	GisFeature gisFeature = GisgraphyTestHelper.createCity("gisfeatureName",
		null, null, featureId);
	GisFeature savedGisFeature = this.gisFeatureDao.save(gisFeature);
	GisFeature retrievedGisFeature = this.gisFeatureDao
		.getByFeatureId(featureId);
	assertNotNull(retrievedGisFeature);
	assertEquals(retrievedGisFeature.getId(), savedGisFeature.getId());
	assertEquals(retrievedGisFeature, savedGisFeature);
    }

    @Test
    public void testGetByFeatureIdShouldRetrieveACityIfTheGisfeatureIsAcity() {
	Long featureId = 1001L;
	GisFeature gisFeature = GisgraphyTestHelper.createCity("cityGisFeature",
		null, null, featureId);
	City paris = new City(gisFeature);
	// save city
	this.gisFeatureDao.save(paris);
	// chek city is well saved and getByFeatureID Return an object of City
	// Type
	GisFeature retrievedParis = this.gisFeatureDao
		.getByFeatureId(featureId);
	assertNotNull(retrievedParis);
	assertEquals(paris.getId(), retrievedParis.getId());
	assertEquals(City.class, retrievedParis.getClass());

    }

    @Test
    public void testgetDirtyShouldRetieveDirtyGisFeature() {
	// create two gis Feature
	GisFeature gisFeature1 = GisgraphyTestHelper
		.createGisFeatureWithAlternateNames("paris", 3);
	GisFeature gisFeature2 = GisgraphyTestHelper
		.createGisFeatureWithAlternateNames("paris2", 3);
	GisFeature gisFeature3 = GisgraphyTestHelper
		.createGisFeatureWithAlternateNames("paris3", 3);
	GisFeature gisFeature4 = GisgraphyTestHelper
		.createGisFeatureWithAlternateNames("paris4", 3);
	gisFeature2.setFeatureCode(ImporterConfig.DEFAULT_FEATURE_CODE);
	gisFeature4.setFeatureClass(ImporterConfig.DEFAULT_FEATURE_CLASS);
	gisFeature3.setLocation(GisgraphyTestHelper.createPoint(0F, 0F));

	// save
	this.gisFeatureDao.save(gisFeature1);
	this.gisFeatureDao.save(gisFeature2);
	this.gisFeatureDao.save(gisFeature3);
	this.gisFeatureDao.save(gisFeature4);

	// check it is well saved
	List<GisFeature> gisFeatures = this.gisFeatureDao.getAll();
	assertNotNull(gisFeatures);
	assertEquals(4, gisFeatures.size());

	List<GisFeature> dirties = this.gisFeatureDao.getDirties();
	assertNotNull(dirties);
	assertEquals(3, dirties.size());
	// assertEquals(gisFeature2, dirties.get(0));

    }

    @Test
    public void testgetDirtyShouldNeverReturnNull() {
	assertNotNull(this.gisFeatureDao.getDirties());
    }

    public void testGetByFeatureIdsShouldReturnTheGisFeature() {
	City city1 = GisgraphyTestHelper.createCity("cityGisFeature", null, null,
		100L);
	City city2 = GisgraphyTestHelper.createCity("cityGisFeature", null, null,
		200L);
	GisFeature gisFeature = GisgraphyTestHelper
		.createGisFeatureWithAlternateNames("gisfeature", 0);
	gisFeature.setFeatureId(300L);
	GisFeature gisFeature2 = GisgraphyTestHelper
		.createGisFeatureWithAlternateNames("gisfeature", 0);
	gisFeature2.setFeatureId(400L);

	this.gisFeatureDao.save(city1);
	this.gisFeatureDao.save(city2);
	this.gisFeatureDao.save(gisFeature);
	this.gisFeatureDao.save(gisFeature2);

	// check it is well saved
	List<GisFeature> gisFeatures = this.gisFeatureDao.getAll();
	assertNotNull(gisFeatures);
	assertEquals(4, gisFeatures.size());

	List<Long> ids = new ArrayList<Long>();
	ids.add(100L);
	ids.add(200L);
	ids.add(300L);
	List<GisFeature> gisByIds = this.gisFeatureDao.listByFeatureIds(ids);
	assertNotNull(gisByIds);
	assertEquals(3, gisByIds.size());

    }

    @Test
    public void testGetByFeatureIdsWithANullListOrEmptyListShouldReturnEmptylist() {
	// test with null
	List<GisFeature> results = this.gisFeatureDao.listByFeatureIds(null);
	assertNotNull(results);
	assertEquals(0, results.size());

	// test with an empty list
	results = this.gisFeatureDao.listByFeatureIds(new ArrayList<Long>());
	assertNotNull(results);
	assertEquals(0, results.size());

    }

    @Test
    public void testGetByFeatureIdWithANullFeatureIdShouldThrows() {
	try {
	    this.gisFeatureDao.getByFeatureId(null);
	    fail();
	} catch (IllegalArgumentException e) {
	}

    }

    @Test
    public void testSetFlushModeWithNullShouldThrows() {
	try {
	    this.gisFeatureDao.setFlushMode(null);
	    fail();
	} catch (RuntimeException e) {
	}
    }

    @Test
    public void testGetAllShouldNotReturnNull() {
	assertNotNull(gisFeatureDao.getAll());
    }

    @Test
    public void testListByNameShouldNotReturnNullButAnEmptyList() {
	assertNotNull(gisFeatureDao.listByName("ABC"));
    }

    @Test
    public void testListByNameWithNullNameShouldThrows() {
	try {
	    assertNotNull(gisFeatureDao.listByName(null));
	    fail();
	} catch (IllegalArgumentException e) {
	}
    }

    public void testListFromTextShouldOnlyReturnFeaturesOfTheSpecifiedClass() {
	// create one city
	Long featureId = 1001L;
	GisFeature gisFeature = GisgraphyTestHelper.createCity("Saint-André",
		1.5F, 2F, featureId);
	AlternateName alternateName = new AlternateName();
	alternateName.setName("alteré");
	alternateName.setGisFeature(gisFeature);
	alternateName.setSource(AlternateNameSource.ALTERNATENAMES_FILE);
	gisFeature.addAlternateName(alternateName);
	City paris = new City(gisFeature);
	paris.addZipCode(new ZipCode("50263"));

	// create ADM
	GisFeature gisAdm = GisgraphyTestHelper.createGisFeatureForAdm(
		"Saint-andré", 2.5F, 3.5F, 40L, 4);
	Adm adm = GisgraphyTestHelper.createAdm("Saint-André", "FR", "A1", "B2",
		"C3", "D4", gisAdm, 4);

	// create a second city
	Long featureId2 = 1002L;
	GisFeature gisFeature2 = GisgraphyTestHelper.createCity("mytown", 1.5F,
		2F, featureId2);
	City paris2 = new City(gisFeature2);
	paris2.addZipCode(new ZipCode("50264"));

	// save cities and check it is saved
	this.gisFeatureDao.save(paris);
	assertNotNull(this.gisFeatureDao.getByFeatureId(featureId));
	this.gisFeatureDao.save(paris2);
	assertNotNull(this.gisFeatureDao.getByFeatureId(featureId2));

	// save Adm and check it is saved
	this.admDao.save(adm);
	assertTrue(this.admDao.getAll().size() == 1);

	// check alternatename is saved
	assertEquals(1, alternateNameDao.getAll().size());

	// commit changes
	this.solRSynchroniser.commit();

	// exact name
	List<City> results = this.cityDao.listFromText("Saint-André", false);
	assertTrue(
		"There must only have one results and only one (the city one), the adm should not be retrieved an nor the second city",
		results.size() == 1);
	assertEquals("Saint-André", results.get(0).getName());
	assertTrue(results.get(0).getFeatureId() == featureId);

	// test synonyms
	results = this.cityDao.listFromText("st-André", false);
	assertTrue("The synonyms with st/saint/santa should be returned",
		results.size() == 1);
	assertEquals("Saint-André", results.get(0).getName());
	assertTrue(results.get(0).getFeatureId() == featureId);

	results = this.cityDao.listFromText("St-André", false);
	assertTrue("The synonyms must be case insensitive", results.size() == 1);
	assertEquals("Saint-André", results.get(0).getName());
	assertTrue(results.get(0).getFeatureId() == featureId);

	List<GisFeature> resultsGis = this.gisFeatureDao.listFromText(
		"Saint-André", false);
	assertTrue(
		"Even if gisFeature is a city, no gisFeature should be retrieved",
		resultsGis.size() == 0);

	// test ADM
	List<Adm> resultsAdm = this.admDao.listFromText("Saint-André", false);
	assertTrue("an Adm should be retrieved for Saint-André", resultsAdm
		.size() == 1);
	assertTrue(resultsAdm.get(0).getFeatureId() == 40L);

	// test other named
	List<Adm> noResults = this.admDao.listFromText("test", true);
	assertTrue("no Adm should be found for 'test'", noResults.size() == 0);
	List<GisFeature> noResults2 = this.gisFeatureDao.listFromText("test",
		true);
	assertTrue("no GisFeature should be found for 'test'", noResults2
		.size() == 0);
	List<City> noResults3 = this.cityDao.listFromText("test", true);
	assertTrue("no city should be found for 'test'", noResults3.size() == 0);

	// test zipcode
	List<Adm> zipResults = this.admDao.listFromText("50264", true);
	assertTrue("no Adm should be found for '50264'", zipResults.size() == 0);
	List<GisFeature> zipResults2 = this.gisFeatureDao.listFromText("50264",
		true);
	assertTrue("no GisFeature should be found for '50264'", zipResults2
		.size() == 0);
	List<City> zipResults3 = this.cityDao.listFromText("50264", true);
	assertTrue("a city should be found for '50264' in alternatenames",
		zipResults3.size() == 1);
	List<City> zipResults4 = this.cityDao.listFromText("50264", false);
	assertTrue("a city should be found for '50264' in zipcode", zipResults4
		.size() == 1);

	// test alternatenames should not be included
	results = this.cityDao.listFromText("alteré", false);
	assertTrue("alternateName should not be included", results.size() == 0);
	results = this.cityDao.listFromText("alteré", true);
	assertTrue("alternateName should be included", results.size() == 1);
	assertEquals("Saint-André", results.get(0).getName());
	assertTrue(results.get(0).getFeatureId() == featureId);

	// test fulltext engine
	results = this.cityDao.listFromText("Saint André", false);
	assertTrue(
		"the fulltext search engine should be iso, case and - insensitive",
		results.size() == 1);

	results = this.cityDao.listFromText("saInt andré", false);
	assertTrue(
		"the fulltext search engine should be iso, case and - insensitive",
		results.size() == 1);

	results = this.cityDao.listFromText("saInt andre", false);
	assertTrue(
		"the fulltext search engine should be iso, case and - insensitive",
		results.size() == 1);
    }
    
    @Test
    public void testGetMaxFeatureId(){
    long firstFeatureId = 10L;
	long featureId = 42L;
	GisFeature gisFeature = GisgraphyTestHelper.createGisFeature("asciiName", 1F, 2F, firstFeatureId);
	gisFeatureDao.save(gisFeature);
	long estimateCount = gisFeatureDao.getMaxFeatureId();
	Assert.assertEquals("getMaxFeatureId should return the max gid",firstFeatureId, estimateCount);
	
	City city = GisgraphyTestHelper.createCity("city", 3.5F, 4.2F, featureId);
	
	gisFeatureDao.save(city);
	estimateCount = gisFeatureDao.getMaxFeatureId();
	Assert.assertEquals("getMaxFeatureId should return the max gid of subtype",featureId, estimateCount);
	
	
    }
    
    public void testCreateGISTIndexForLocationColumnShouldNotThrow(){
	gisFeatureDao.createGISTIndexForLocationColumn();
    }

    @Required
    public void setGisFeatureDao(IGisFeatureDao gisFeatureDao) {
	this.gisFeatureDao = gisFeatureDao;
    }

    @Required
    public void setCityDao(ICityDao cityDao) {
	this.cityDao = cityDao;
    }

    @Required
    public void setAdmDao(IAdmDao admDao) {
	this.admDao = admDao;
    }

    @Required
    public void setAlternateNameDao(IAlternateNameDao alternateNameDao) {
	this.alternateNameDao = alternateNameDao;
    }

    /**
     * @param countryDao
     *                the countryDao to set
     */
    @Required
    public void setCountryDao(ICountryDao countryDao) {
	this.countryDao = countryDao;
    }

}
