package com.gisgraphy.addressparser;

import org.junit.Assert;
import org.junit.Test;

public class StructuredAddressQueryTest {

	@Test
	public void constructor() {
		Address address = new Address();
		StructuredAddressQuery addressQuery = new StructuredAddressQuery(address, "FR");
		Assert.assertEquals(address, addressQuery.getStructuredAddress());
		Assert.assertEquals("FR", addressQuery.getCountry());
	}
	
	@Test(expected=RuntimeException.class)
	public void constructorWithNullAddres() {
		new StructuredAddressQuery(null, "FR");
	}
	
	@Test(expected=RuntimeException.class)
	public void constructorWithNullCountry() {
		Address address = new Address();
		new StructuredAddressQuery(address, null);
	}
	
	@Test(expected=RuntimeException.class)
	public void constructorWithEmptyCountry() {
		Address address = new Address();
		new StructuredAddressQuery(address, "");
	}
	
	@Test(expected=RuntimeException.class)
	public void setAddress() {
		Address address = new Address();
		StructuredAddressQuery addressQuery = new StructuredAddressQuery(address, "FR");
		addressQuery.setAddress("foo");
	}
	
	@Test(expected=RuntimeException.class)
	public void getAddress() {
		Address address = new Address();
		StructuredAddressQuery addressQuery = new StructuredAddressQuery(address, "FR");
		addressQuery.getAddress();
	}
	
	@Test
	public void toStringTest() {
		Address address = new Address();
		address.setStreetName("streetName");
		address.setCity("city");
		StructuredAddressQuery addressQuery = new StructuredAddressQuery(address, "FR");
		String queryToString = addressQuery.toString();
		Assert.assertTrue(queryToString.contains(addressQuery.getClass().getSimpleName()));
	}

}
