package com.gisgraphy.importer;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.HouseNumber;
import com.gisgraphy.helper.GeolocHelper;

public class HouseNumberListSerializerTest {

	@Test
	public void serialize() {
		HouseNumberListSerializer serializer = new HouseNumberListSerializer();
		HouseNumber houseNumber = new HouseNumber();
		houseNumber.setLocation(GeolocHelper.createPoint(10.3D, 9.6D));
		houseNumber.setNumber("1:");//with 
		
		HouseNumber houseNumber2 = new HouseNumber();
		houseNumber2.setLocation(GeolocHelper.createPoint(10.4D, 9.7D));
		houseNumber2.setNumber("2");
		
		List<HouseNumber> houseNumberList = new ArrayList<HouseNumber>();
		houseNumberList.add(houseNumber);
		houseNumberList.add(houseNumber2);
		String actual = serializer.serialize(houseNumberList);
		assertEquals("1:9.6,10.3  2:9.7,10.4", actual);
	}
	
	@Test
	public void deserialize(){
		HouseNumberListSerializer serializer = new HouseNumberListSerializer();
		List<HouseNumber> housenumbers = serializer.deserialize("1:9.6,10.3  2:9.7,10.4");
		Assert.assertEquals(2, housenumbers.size());
		HouseNumber houseNumber1 = housenumbers.get(0);
		Assert.assertEquals("1", houseNumber1.getName());
		Assert.assertTrue(houseNumber1.getLatitude().toString(),houseNumber1.getLatitude().toString().startsWith("9.6"));
		Assert.assertTrue(houseNumber1.getLongitude().toString(), houseNumber1.getLongitude().toString().startsWith("10.3"));
		HouseNumber houseNumber2 = housenumbers.get(1);
		Assert.assertEquals("2", houseNumber2.getName());
		Assert.assertTrue(houseNumber2.getLatitude().toString(),houseNumber2.getLatitude().toString().startsWith("9.7"));
		Assert.assertTrue(houseNumber2.getLongitude().toString(), houseNumber2.getLongitude().toString().startsWith("10.4"));
	}
	
	@Test
	public void deserializeWithNullOrEmpty(){
		HouseNumberListSerializer serializer = new HouseNumberListSerializer();
		Assert.assertNotNull(serializer.deserialize(""));
		Assert.assertEquals(0,serializer.deserialize("").size());
		
		Assert.assertNotNull(serializer.deserialize(null));
		Assert.assertEquals(0,serializer.deserialize(null).size());
	}

}
