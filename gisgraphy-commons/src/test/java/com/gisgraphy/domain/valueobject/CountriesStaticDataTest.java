package com.gisgraphy.domain.valueobject;

import org.junit.Assert;
import org.junit.Test;

public class CountriesStaticDataTest {

	@Test
	public void getCountryCodeFromCountryName() {
		Assert.assertEquals("FR", CountriesStaticData.getCountryCodeFromCountryName("France"));
	}
	
	@Test
	public void getCountryCodeFromCountryName_nullCountryName() {
		Assert.assertNull(CountriesStaticData.getCountryCodeFromCountryName(null));
	}
	
	
	@Test
	public void getCountryCodeFromPosition() {
		int position = 10;
		Assert.assertEquals(CountriesStaticData.getCountryCodeFromCountryName(CountriesStaticData.sortedCountriesName.get(position)), CountriesStaticData.getCountryCodeFromPosition(position));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getCountryCodeFromPosition_negativePosition() {
	 CountriesStaticData.getCountryCodeFromPosition(-1);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void getCountryCodeFromPosition_TooHighPosition() {
		int position = 1000;
		Assert.assertEquals(CountriesStaticData.getCountryCodeFromCountryName(CountriesStaticData.sortedCountriesName.get(position)), CountriesStaticData.getCountryCodeFromPosition(position));
	}
	
	@Test
	public void getCountryNameFromPosition() {
		int position = 10;
		Assert.assertEquals(CountriesStaticData.sortedCountriesName.get(position), CountriesStaticData.getCountryNameFromPosition(position));
	}
	@Test(expected=IndexOutOfBoundsException.class)
	public void getCountryNameFromPosition_TooHighPosition() {
		int position = 1000;
		Assert.assertEquals(CountriesStaticData.sortedCountriesName.get(position), CountriesStaticData.getCountryNameFromPosition(position));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getCountryNameFromPosition_negativePosition() {
	 CountriesStaticData.getCountryNameFromPosition(-1);
	}
	
	@Test
	public void getCountryNameFromCountryCode() {
		Assert.assertEquals("France", CountriesStaticData.getCountryNameFromCountryCode("FR"));
	}
	
	@Test
	public void getPositionFromCountryCode() {
		Assert.assertEquals(0, CountriesStaticData.getPositionFromCountryCode("AF"));
	}
	
	@Test
	public void getNumberOfCountries(){
		Assert.assertEquals(248, CountriesStaticData.getNumberOfCountries());
	}
	
	
}
