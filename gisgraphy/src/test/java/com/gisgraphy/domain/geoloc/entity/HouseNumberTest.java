package com.gisgraphy.domain.geoloc.entity;

import static org.junit.Assert.*;

import net.sf.jstester.util.Assert;

import org.junit.Test;

public class HouseNumberTest {

	@Test
	public void testEquals() {
		HouseNumber hn1 = new HouseNumber();
		OpenStreetMap street = new OpenStreetMap();
		street.setId(1L);
		hn1.setNumber("1");
		hn1.setStreet(street);
		
		HouseNumber hn2 = new HouseNumber();
		hn2.setStreet(street);
		hn2.setNumber("1");
		
		HouseNumber hnNotSameNumber = new HouseNumber();
		hn2.setStreet(street);
		hn2.setNumber("2");
		
		Assert.assertFalse("not same number=>not equals",hn1.equals(hnNotSameNumber));

		HouseNumber hnNotSameStreet = new HouseNumber();
		hn2.setNumber("1");
		
		Assert.assertFalse("not same street=>not equals",hn1.equals(hnNotSameStreet));
		
		//test when name is not the same and number is not null
		HouseNumber hn3 = new HouseNumber();
		hn3.setNumber("1");
		hn3.setName("name");
		hn3.setStreet(street);
		
		HouseNumber hn4 = new HouseNumber();
		hn4.setNumber("1");
		hn4.setStreet(street);
		
		Assert.assertEquals("name should not impact equals when number is not null",hn4, hn3);
		
		
		//if number is null, they should have the same street and name
		HouseNumber hnWithoutNumber = new HouseNumber();
		hnWithoutNumber.setName("name");
		hnWithoutNumber.setStreet(street);
		
		HouseNumber hnWithoutNumberButSameName = new HouseNumber();
		hnWithoutNumberButSameName.setName("name");
		hnWithoutNumberButSameName.setStreet(street);
		
		Assert.assertTrue("number is null, name should be checked",hnWithoutNumber.equals(hnWithoutNumberButSameName));
		
		HouseNumber hnWithoutNumberDifferentName = new HouseNumber();
		hnWithoutNumberDifferentName.setName("differentName");
		hnWithoutNumberDifferentName.setStreet(street);
		
		Assert.assertFalse("number is null, name should be checked",hnWithoutNumber.equals(hnWithoutNumberDifferentName));
	}

}
