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

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.geoloc.entity.ZipCode;
import com.gisgraphy.domain.valueobject.GisFeatureDistance;
import com.gisgraphy.fulltext.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.helper.URLUtils;
import com.gisgraphy.test.GisgraphyTestHelper;

/**
 * test cityDao and GenericDao
 */
public class CityDaoTest extends AbstractIntegrationHttpSolrTestCase {

    private ICityDao cityDao;

    private IGisFeatureDao gisFeatureDao;

    @Resource
    private GisgraphyTestHelper geolocTestHelper;

    
    // it is the genericDaotest apply to a city

   @Test
    public void testGetAllShouldRetrieveAllTheCityInTheDataStore() {
	int nbToInsert = 2;
	for (int i = 0; i < nbToInsert; i++) {
	    City paris = GisgraphyTestHelper.createCityWithAlternateNames("paris"
		    + i, 3);
	    City savedParis = this.cityDao.save(paris);
	    City retrievedParis = this.cityDao.get(savedParis.getId());
	    assertNotNull(retrievedParis);
	    assertEquals(paris.getId(), retrievedParis.getId());
	}
	List<City> cities = this.cityDao.getAll();
	assertNotNull(cities);
	assertEquals(nbToInsert, cities.size());
    }

    @Test
    public void testCitiesShouldbeSavedInABatch() {
	int nbToInsert = 10;
	for (int i = 0; i < nbToInsert; i++) {
	    City paris = GisgraphyTestHelper.createCityWithAlternateNames("paris"
		    + i, 0);
	    City savedParis = this.cityDao.save(paris);
	    City retrievedParis = this.cityDao.get(savedParis.getId());
	    assertNotNull(retrievedParis);
	    assertEquals(paris.getId(), retrievedParis.getId());
	}
	List<City> cities = this.cityDao.getAll();
	assertNotNull(cities);
	assertEquals(nbToInsert, cities.size());
    }

    @Test
    public void testGetAllpaginateShouldPaginate() {
	int nbToInsert = 10;
	int from = 3;
	int max = 5;
	// save 0,1,2,3,4,5,6,7,8,9
	for (int i = 0; i < nbToInsert; i++) {
	    City paris = GisgraphyTestHelper.createCityWithAlternateNames("paris"
		    + i, 0);
	    City savedParis = this.cityDao.save(paris);
	    City retrievedParis = this.cityDao.get(savedParis.getId());
	    assertNotNull(retrievedParis);
	    assertEquals(paris.getId(), retrievedParis.getId());
	}
	List<City> cities = this.cityDao.getAllPaginate(from, max);
	// should be 2,3,4,5,6
	assertNotNull(cities);
	assertEquals(max, cities.size());
	// check values
	for (int i = 0; i < cities.size(); i++) {
	    assertEquals("paris" + (i + from - 1), cities.get(i).getName());
	}
    }

    @Test
    public void testGetAllpaginateShouldNotConsiderFromIfItIsLessThan1() {
	int nbToInsert = 10;
	int from = 0;
	int max = 5;
	// save 0,1,2,3,4,5,6,7,8,9
	for (int i = 0; i < nbToInsert; i++) {
	    City paris = GisgraphyTestHelper.createCityWithAlternateNames("paris"
		    + i, 0);
	    City savedParis = this.cityDao.save(paris);
	    City retrievedParis = this.cityDao.get(savedParis.getId());
	    assertNotNull(retrievedParis);
	    assertEquals(paris.getId(), retrievedParis.getId());
	}
	List<City> cities = this.cityDao.getAllPaginate(from, max);
	// should be 0,1,2,3,4
	assertNotNull(cities);
	assertEquals(max, cities.size());
	// check values
	for (int i = 0; i < cities.size(); i++) {
	    assertEquals("paris" + (i), cities.get(i).getName());
	}
    }

    @Test
    public void testGetAllpaginateShouldNotConsiderMaxIfItIsLessOrEqualsTo0() {
	int nbToInsert = 10;
	int from = 3;
	int max = 0;
	// save 0,1,2,3,4,5,6,7,8,9
	for (int i = 0; i < nbToInsert; i++) {
	    City paris = GisgraphyTestHelper.createCityWithAlternateNames("paris"
		    + i, 0);
	    City savedParis = this.cityDao.save(paris);
	    City retrievedParis = this.cityDao.get(savedParis.getId());
	    assertNotNull(retrievedParis);
	    assertEquals(paris.getId(), retrievedParis.getId());
	}
	List<City> cities = this.cityDao.getAllPaginate(from, max);
	// should be 2,3,4,5,6,7,8,9
	assertNotNull(cities);
	assertEquals(8, cities.size());
	// check values
	for (int i = 0; i < cities.size(); i++) {
	    assertEquals("paris" + (i + from - 1), cities.get(i).getName());
	}
    }

    @Test
    public void testSaveShouldSaveTheInheritedGisFeature() {
	City paris = GisgraphyTestHelper.createCityWithAlternateNames("paris", 0);
	City savedParis = this.cityDao.save(paris);
	City retrievedParis = this.cityDao.get(savedParis.getId());
	assertNotNull(retrievedParis);
	assertEquals(paris.getId(), retrievedParis.getId());
	GisFeature retrievedgisFeature = this.gisFeatureDao.get(retrievedParis
		.getId());
	assertNotNull(retrievedgisFeature);
    }

    @Test
    public void testsaveCityInABatchShouldCascadeAlternateNames() {
	int nbToInsert = 20;
	for (int i = 0; i < nbToInsert; i++) {
	    City paris = GisgraphyTestHelper.createCityWithAlternateNames("paris"
		    + i, 3);
	    City savedParis = this.cityDao.save(paris);
	    City retrievedParis = this.cityDao.get(savedParis.getId());
	    assertNotNull(retrievedParis);
	    assertEquals(paris.getId(), retrievedParis.getId());
	}
	List<City> cities = this.cityDao.getAll();
	assertNotNull(cities);
	assertEquals(nbToInsert, cities.size());
    }

    @Test
    public void testSaveCityShouldCascadeAlternateNames() {
	City paris = GisgraphyTestHelper.createCityWithAlternateNames("paris", 3);
	assertNotNull(paris.getAlternateNames());
	assertEquals(3, paris.getAlternateNames().size());
	City savedParis = this.cityDao.save(paris);
	City retrievedParis = this.cityDao.get(savedParis.getId());
	assertNotNull(retrievedParis);
	assertEquals(paris.getId(), retrievedParis.getId());
	assertNotNull(retrievedParis.getAlternateNames());
	assertEquals(3, retrievedParis.getAlternateNames().size());
    }

    @Test
    public void testGetShouldRetrieveNullIfTheSpecifiedIdDoesntExists() {
	City city = this.cityDao.get(100000L);
	assertEquals(null, city);
    }

    @Test
    public void testGetShouldRetrieveCorrectData() {
	City paris = GisgraphyTestHelper.createCityWithAlternateNames("paris", 0);
	City savedParis = this.cityDao.save(paris);
	City retrievedParis = this.cityDao.get(savedParis.getId());
	assertNotNull(retrievedParis);
	assertEquals(paris.getId(), retrievedParis.getId());
	GisFeature retrievedgisFeature = this.gisFeatureDao.get(retrievedParis
		.getId());
	assertNotNull(retrievedgisFeature);
    }

    @Test
    public void testGetAllShouldRetrieveanEmptyListIfNoCitiesInTheDatastore() {
	List<City> cities = this.cityDao.getAll();
	assertEquals(0, cities.size());
    }

    @Test
    public void testExistsShouldReturnFalseWhenNoCityWithTheSpecifiedIdExists() {
	assertFalse(this.cityDao.exists(-1L));
    }

    @Test
    public void testListByNameShouldRetrieveTheCorrectCity() {
	City paris = GisgraphyTestHelper.createCityWithAlternateNames("paris", 3);
	City savedParis = this.cityDao.save(paris);
	List<City> results = this.cityDao.listByName("paris");
	assertNotNull(results);
	assertEquals(1, results.size());
	assertEquals(savedParis, results.get(0));
    }

    @Test
    public void testListByNameShouldNotRetrieveNullIfNoCityExistsWithTheSpecifiedName() {
	List<City> results = this.cityDao.listByName("paris");
	assertNotNull(results);
    }

    @Test
    public void testRemoveShouldRemoveTheCity() {
	// create and save city
	City paris = GisgraphyTestHelper.createCityWithAlternateNames("paris", 3);
	City savedParis = this.cityDao.save(paris);

	// check it is saved
	Long id = savedParis.getId();
	City retrievedParis = this.cityDao.get(savedParis.getId());
	assertNotNull(retrievedParis);
	assertEquals(paris.getId(), retrievedParis.getId());
	// remove city
	this.cityDao.remove(savedParis);
	// check city is removed
	City retrievedParisafterRemove = this.cityDao.get(id);
	assertNull(retrievedParisafterRemove);
    }

    @Test
    public void testRemoveCityShouldRemoveTheCityAndTheInheritedGisFeatureInCascade() {
	City paris = GisgraphyTestHelper.createCityWithAlternateNames("paris", 3);
	// save city
	City savedParis = this.cityDao.save(paris);
	// chek city is well saved
	Long id = savedParis.getId();
	City retrievedParis = this.cityDao.get(savedParis.getId());
	assertNotNull(retrievedParis);
	assertEquals(paris.getId(), retrievedParis.getId());
	// check gisFeature is well saved
	Long savedGisFeatureId = retrievedParis.getId();
	GisFeature savedGisFeature = this.gisFeatureDao.get(savedGisFeatureId);
	assertNotNull(savedGisFeature);

	// remove city
	this.cityDao.remove(savedParis);

	// check city is removed
	City retrievedParisafterRemove = this.cityDao.get(id);
	assertEquals(null, retrievedParisafterRemove);

	// check gisFeature is remove
	GisFeature savedGisFeatureafterRemove = this.gisFeatureDao
		.get(savedGisFeatureId);
	assertNull(savedGisFeatureafterRemove);
    }

    // distance

    @Test
    public void testgetNearestAndDistanceFromShouldReturnCorrectDistance() {
	City p1 = GisgraphyTestHelper.createCity("paris", 48.86667F, 2.3333F, 1L);
	City p2 = GisgraphyTestHelper.createCity("bordeaux", 44.83333F, -0.56667F,
		3L);
	City p3 = GisgraphyTestHelper.createCity("goussainville", 49.01667F,
		2.46667F, 2L);

	this.cityDao.save(p1);
	this.cityDao.save(p2);
	this.cityDao.save(p3);
	List<GisFeatureDistance> results = this.cityDao
		.getNearestAndDistanceFrom(p1.getLocation(), 1000000);
	assertEquals(3, results.size());
	checkDistancePercentError(p1, results);

    }

    @Test
    public void testGetNearestAndDistanceFromShouldReturnAfullFilledDTO() {
	City p1 = geolocTestHelper
		.createAndSaveCityWithFullAdmTreeAndCountry(1L);
	p1.setAdm4Code("D4");
	p1.setAdm4Name("adm");


	this.cityDao.save(p1);
	List<GisFeatureDistance> results = this.cityDao
		.getNearestAndDistanceFrom(p1.getLocation(), 1000000);
	assertEquals(1, results.size());
	GisFeatureDistance gisFeatureDistance = results.get(0);
	assertEquals(p1.getFeatureId(), gisFeatureDistance.getFeatureId());
	assertEquals(p1.getName(), gisFeatureDistance.getName());
	assertEquals(p1.getAsciiName(), gisFeatureDistance.getAsciiName());
	assertEquals(p1.getLocation().toText(), gisFeatureDistance
		.getLocation().toText());
	assertEquals(p1.getLatitude(), gisFeatureDistance.getLat());
	assertEquals(p1.getLongitude(), gisFeatureDistance.getLng());
	assertEquals(p1.getAdm1Code(), gisFeatureDistance.getAdm1Code());
	assertEquals(p1.getAdm2Code(), gisFeatureDistance.getAdm2Code());
	assertEquals(p1.getAdm3Code(), gisFeatureDistance.getAdm3Code());
	assertEquals(p1.getAdm4Code(), gisFeatureDistance.getAdm4Code());
	assertEquals(p1.getAdm1Name(), gisFeatureDistance.getAdm1Name());
	assertEquals(p1.getAdm2Name(), gisFeatureDistance.getAdm2Name());
	assertEquals(p1.getAdm3Name(), gisFeatureDistance.getAdm3Name());
	assertEquals(p1.getAdm4Name(), gisFeatureDistance.getAdm4Name());
	assertEquals(p1.getFeatureClass(), gisFeatureDistance.getFeatureClass());
	assertEquals(p1.getFeatureCode(), gisFeatureDistance.getFeatureCode());
	assertEquals(p1.getCountryCode(), gisFeatureDistance.getCountryCode());
	assertEquals(p1.getPopulation(), gisFeatureDistance.getPopulation());
	assertEquals(p1.getElevation(), gisFeatureDistance.getElevation());
	assertEquals(p1.getGtopo30(), gisFeatureDistance.getGtopo30());
	assertEquals(p1.getTimezone(), gisFeatureDistance.getTimezone());
	assertEquals("gisfeatureDistance should have the same number of zipCodes as the original features",p1.getZipCodes().size(),
			gisFeatureDistance.getZipCodes().size());
	assertTrue(gisFeatureDistance.getZipCodes().contains(p1.getZipCodes().get(0).getCode()));
	assertTrue(gisFeatureDistance.getZipCodes().contains(p1.getZipCodes().get(1).getCode()));
	assertEquals("City", gisFeatureDistance.getPlaceType());
	// check transcient field
	assertEquals(URLUtils.createCountryFlagUrl(p1.getCountryCode()),
		gisFeatureDistance.getCountry_flag_url());
	assertEquals(URLUtils
		.createGoogleMapUrl(p1.getLocation(), p1.getName()),
		gisFeatureDistance.getGoogle_map_url());
	assertEquals(URLUtils.createYahooMapUrl(p1.getLocation()),
		gisFeatureDistance.getYahoo_map_url());
	assertNotNull(gisFeatureDistance.getDistance());

	checkDistancePercentError(p1, results);

    }
    
    @Test
    public void testGetNearestAndDistanceFromShouldPaginateWhenThereIsMoreThanOneZipCodes() {
	City p1 = GisgraphyTestHelper.createCity("paris", 48.86667F, 2.3333F, 1L);
	City p2 = GisgraphyTestHelper.createCity("vanves", 48.82F, 2.289F, 2L);
	City p3 = GisgraphyTestHelper.createCity("goussainville", 49.01667F,
		2.46667F, 3L);
	p2.addZipCode(new ZipCode("75000"));
	p2.addZipCode(new ZipCode("75001"));
	p2.addZipCode(new ZipCode("75002"));
	this.cityDao.save(p1);
	this.cityDao.save(p2);
	this.cityDao.save(p3);
	List<GisFeatureDistance> results = this.cityDao
		.getNearestAndDistanceFrom(p1.getLocation(), 1000000, 1, 2,true);
	assertEquals(2, results.size());
	// check values and sorted
	assertEquals(p1.getName(), results.get(0).getName());
	assertEquals(p2.getName(), results.get(1).getName());
    }

    @Test
    public void testgetNearestAndDistanceFromShouldPaginate() {
	City p1 = GisgraphyTestHelper.createCity("paris", 48.86667F, 2.3333F, 1L);
	City p2 = GisgraphyTestHelper.createCity("bordeaux", 44.83333F, -0.56667F,
		3L);
	City p3 = GisgraphyTestHelper.createCity("goussainville", 49.01667F,
		2.46667F, 2L);

	this.cityDao.save(p1);
	this.cityDao.save(p2);
	this.cityDao.save(p3);
	List<GisFeatureDistance> results = this.cityDao
		.getNearestAndDistanceFrom(p1.getLocation(), 1000000, 2, 5,true);
	assertEquals(2, results.size());
	// check values and sorted
	assertEquals(p3.getName(), results.get(0).getName());
	assertEquals(p2.getName(), results.get(1).getName());

    }

    @Test
    public void testGetNearestAndDistanceFromGisFeatureShouldReturnCorrectDistance() {
	City p1 = GisgraphyTestHelper.createCity("paris", 48.86667F, 2.3333F, 1L);
	City p2 = GisgraphyTestHelper.createCity("bordeaux", 44.83333F, -0.56667F,
		3L);
	City p3 = GisgraphyTestHelper.createCity("goussainville", 49.01667F,
		2.46667F, 2L);

	this.cityDao.save(p1);
	this.cityDao.save(p2);
	this.cityDao.save(p3);
	// for city dao
	List<GisFeatureDistance> results = this.cityDao
		.getNearestAndDistanceFromGisFeature(p1, 1000000, -1, -1,true);
	assertEquals(2, results.size());
	checkDistancePercentError(p1, results);

	results = this.cityDao.getNearestAndDistanceFromGisFeature(p1, 100,true);
	assertNotNull("getNearestAndDistanceFrom should never return null",
		results);
	assertTrue(results.isEmpty());

	results = this.cityDao.getNearestAndDistanceFromGisFeature(p1, 22000,true);
	assertTrue(results.isEmpty());

	results = this.cityDao.getNearestAndDistanceFromGisFeature(p1, 23000,true);
	assertEquals(1, results.size());
	checkDistancePercentError(p1, results);
	// will try with the gisFeature implementation
	this.cityDao.remove(p1);
	this.cityDao.remove(p2);
	this.cityDao.remove(p3);
	assertEquals(0, this.cityDao.getAll().size());

	GisFeature p4 = GisgraphyTestHelper.createGisFeature("_paris", 48.86667F,
		2.3333F, 1L);
	GisFeature p5 = GisgraphyTestHelper.createGisFeature("_bordeaux",
		44.83333F, -0.56667F, 3L);
	GisFeature p6 = GisgraphyTestHelper.createGisFeature("_goussainville",
		49.01667F, 2.46667F, 2L);

	this.gisFeatureDao.save(p4);
	this.gisFeatureDao.save(p5);
	this.gisFeatureDao.save(p6);
	assertEquals(3, this.gisFeatureDao.getAll().size());

	// for gisfeatureDao because there is two implementation

	results = this.gisFeatureDao.getNearestAndDistanceFromGisFeature(p4,
		1000000,true);
	assertEquals(2, results.size());
	checkDistancePercentError(p4, results);

	results = this.gisFeatureDao.getNearestAndDistanceFromGisFeature(p4,
		100,true);
	assertNotNull("getNearestAndDistanceFrom should never return null",
		results);
	assertTrue(results.isEmpty());

	results = this.gisFeatureDao.getNearestAndDistanceFromGisFeature(p4,
		22000,true);
	assertTrue(results.isEmpty());

	results = this.gisFeatureDao.getNearestAndDistanceFromGisFeature(p4,
		23000,true);
	assertEquals(1, results.size());
	checkDistancePercentError(p4, results);

    }

    @Test
    public void testgetNearestAndDistanceFromGisFeatureShouldPaginate() {
	City p1 = GisgraphyTestHelper.createCity("paris", 48.86667F, 2.3333F, 1L);// N
	// 48°
	// 52'
	// 0''
	// 2°
	// 20'
	// 0''
	// E
	City p2 = GisgraphyTestHelper.createCity("bordeaux", 44.83333F, -0.56667F,
		3L);// 44
	// 50 0
	// N; 0
	// 34 0
	// W
	City p3 = GisgraphyTestHelper.createCity("goussainville", 49.01667F,
		2.46667F, 2L);// N
	// 49°
	// 1'
	// 0''
	// E 2°
	// 28'
	// 0''

	this.gisFeatureDao.save(p1);
	this.gisFeatureDao.save(p2);
	this.gisFeatureDao.save(p3);
	// for city dao
	List<GisFeatureDistance> results = this.cityDao
		.getNearestAndDistanceFromGisFeature(p1, 1000000, 1, 5,true);
	assertEquals(2, results.size());
	assertEquals(p3.getName(), results.get(0).getName());
	assertEquals(p2.getName(), results.get(1).getName());

	results = this.cityDao.getNearestAndDistanceFromGisFeature(p1, 1000000,
		2, 5,true);
	assertEquals(1, results.size());
	assertEquals(p2.getName(), results.get(0).getName());

	results = this.cityDao.getNearestAndDistanceFromGisFeature(p1, 1000000,
		1, 1,true);
	assertEquals(1, results.size());
	assertEquals(p3.getName(), results.get(0).getName());

	results = this.cityDao.getNearestAndDistanceFromGisFeature(p1, 1000000,
		0, 1,true);
	assertEquals(1, results.size());
	assertEquals(p3.getName(), results.get(0).getName());

	results = this.cityDao.getNearestAndDistanceFromGisFeature(p1, 1000000,
		1, 0,true);
	assertEquals(2, results.size());
	assertEquals(p3.getName(), results.get(0).getName());
	assertEquals(p2.getName(), results.get(1).getName());
	// remove city and replace with gisFeature
	this.cityDao.remove(p1);
	this.cityDao.remove(p2);
	this.cityDao.remove(p3);
	assertEquals(0, this.cityDao.getAll().size());

	GisFeature p4 = GisgraphyTestHelper.createCity("paris", 48.86667F,
		2.3333F, 1L);// N
	// 48°
	// 52'
	// 0''
	// 2°
	// 20'
	// 0''
	// E
	GisFeature p5 = GisgraphyTestHelper.createCity("bordeaux", 44.83333F,
		-0.56667F, 3L);// N 44
	// 50 0
	// ; 0
	// 34 0
	// W
	GisFeature p6 = GisgraphyTestHelper.createCity("goussainville", 49.01667F,
		2.46667F, 2L);// N
	// 49°
	// 1'
	// 0''
	// E 2°
	// 28'
	// 0''

	this.gisFeatureDao.save(p4);
	this.gisFeatureDao.save(p5);
	this.gisFeatureDao.save(p6);
	assertEquals(3, this.gisFeatureDao.getAll().size());

	results = this.gisFeatureDao.getNearestAndDistanceFromGisFeature(p4,
		1000000, 2, 5,true);
	assertEquals(1, results.size());
	assertEquals(p5.getName(), results.get(0).getName());

	results = this.gisFeatureDao.getNearestAndDistanceFromGisFeature(p4,
		1000000, 1, 1,true);
	assertEquals(1, results.size());
	assertEquals(p6.getName(), results.get(0).getName());

	results = this.gisFeatureDao.getNearestAndDistanceFromGisFeature(p4,
		1000000, 0, 1,true);
	assertEquals(1, results.size());
	assertEquals(p6.getName(), results.get(0).getName());

	results = this.gisFeatureDao.getNearestAndDistanceFromGisFeature(p4,
		1000000, 1, 0,true);
	assertEquals(2, results.size());
	assertEquals(p6.getName(), results.get(0).getName());
	assertEquals(p5.getName(), results.get(1).getName());

    }

    private void checkDistancePercentError(GisFeature p1,
	    List<GisFeatureDistance> results) {
	Double lastOne = null;
	for (GisFeatureDistance gisFeatureDistance : results) {
	    double calculatedDist = p1.distanceTo(gisFeatureDistance
		    .getLocation());
	    double retrieveDistance = gisFeatureDistance.getDistance();
	    if (lastOne != null) {
		assertNotNull(retrieveDistance);
		assertNotNull(lastOne);
		if (!(lastOne.doubleValue() <= retrieveDistance)) {
		    fail("The results are not sorted");
		}
	    }
	    double percent = (Math.abs(calculatedDist - retrieveDistance) * 100)
		    / Math.min(retrieveDistance, calculatedDist);
	    log.info("Distance difference beetween " + p1.getName() + " and "
		    + gisFeatureDistance.getName() + " is " + percent + "%");
	    if (calculatedDist > 0.001) {
		assertTrue(
			"The results are not at the expected distance : should be "
				+ calculatedDist + " but was "
				+ retrieveDistance + " (purcent error="
				+ percent + ")",
			(percent < GisgraphyTestHelper.DISTANCE_PURCENT_ERROR_ACCEPTED)
				|| (calculatedDist == retrieveDistance));// tolerence
	    }
	    lastOne = retrieveDistance;
	}

    }

    @Test
    public void testIsCityShouldReturnTrueIfItIsACity() {
	City city = GisgraphyTestHelper.createCityWithAlternateNames("paris", 0);
	assertTrue(city.isCity());
    }

    @Test
    public void testIsCityShouldReturnFalseIfItIsNotACity() {
	GisFeature gisFeature = GisgraphyTestHelper.createGisFeatureForAdm("",
		20F, 30F, null, 2);
	assertFalse(gisFeature.isCity());
    }

    public void testGetByFeatureIdsShouldOnlyReturnCities() {
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
	List<City> gisByIds = this.cityDao.listByFeatureIds(ids);
	assertNotNull(gisByIds);
	assertEquals(2, gisByIds.size());

    }

    @Test
    public void testListByZipCodeShouldReturnCorrectValues() {
	City city1 = GisgraphyTestHelper.createCity("paris", 48.86667F, 2.3333F,
		1L);
	city1.addZipCode(new ZipCode("75003"));
	city1.setCountryCode("FR");
	City city2 = GisgraphyTestHelper.createCity("paris2", 48.86667F, 2.3333F,
		2L);
	city2.addZipCode(new ZipCode("75003"));
	city2.setCountryCode("EN");
	this.cityDao.save(city1);
	this.cityDao.save(city2);
	List<City> results = this.cityDao.listByZipCode("75003", null);
	assertEquals(2, results.size());
	results = this.cityDao.listByZipCode("75004", null);
	assertNotNull(results);
	assertEquals(0, results.size());
	results = this.cityDao.listByZipCode("75003", "fr");
	assertEquals(
		"ListByZipCode should be case insensitive for countrycode", 1,
		results.size());

    }
    @Test
    public void testCreateGISTIndexForLocationColumnShouldNotThrow(){
	cityDao.createGISTIndexForLocationColumn();
    }

    @Required
    public void setCityDao(ICityDao cityDao) {
	this.cityDao = cityDao;
    }

    @Required
    public void setGisFeatureDao(IGisFeatureDao gisFeatureDao) {
	this.gisFeatureDao = gisFeatureDao;
    }

}
