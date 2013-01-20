package com.gisgraphy.domain.geoloc.entity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import net.sf.jstester.util.Assert;

import org.junit.Test;

import com.gisgraphy.test.GisgraphyTestHelper;

public class OpenStreetMapTest {

	@Test
	public void testAddAlternateNameShouldAddAndNotReplace() {
		OpenStreetMap street = new OpenStreetMap();
		street.setId(1L);
		
		List<HouseNumber> houseNumbers = new ArrayList<HouseNumber>();
		HouseNumber n1 = GisgraphyTestHelper.createHouseNumber();
		
		houseNumbers.add(n1);
		street.setHouseNumbers(houseNumbers);
		
		Assert.assertEquals(1, street.getHouseNumbers().size());
		
		HouseNumber n2 = new HouseNumber();
		street.addHouseNumber(n2);
		
		Assert.assertEquals("add street should add street, not replace",2, street.getHouseNumbers().size());
		
		//check doubleset
		Assert.assertEquals("double set should be done",street, n2.getStreet());
		
	}
	
	@Test
	public void testAddAlternateNamewhenNoHouseNumberAlreadyAdded() {
		OpenStreetMap street = new OpenStreetMap();
		street.setId(1L);
		
		HouseNumber n1 = GisgraphyTestHelper.createHouseNumber();
		
		street.addHouseNumber(n1);
		
		Assert.assertEquals("housenumberlist should be initialize when no house number have been added",1, street.getHouseNumbers().size());
		
		//check doubleset
		Assert.assertEquals("double set should be done",street, n1.getStreet());
		
	}
	
	@Test
	public void testAddAlternateNames() {
		OpenStreetMap street = new OpenStreetMap();
		street.setId(1L);
		
		List<HouseNumber> houseNumbers = new ArrayList<HouseNumber>();
		HouseNumber n1 = GisgraphyTestHelper.createHouseNumber();
		
		houseNumbers.add(n1);
		street.addHouseNumbers(houseNumbers);
		
		Assert.assertEquals(1, street.getHouseNumbers().size());
		
		//check doubleset
		Assert.assertEquals("double set should be done",street, n1.getStreet());
		
	}

}
