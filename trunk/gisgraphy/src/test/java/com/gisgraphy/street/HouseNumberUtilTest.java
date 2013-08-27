package com.gisgraphy.street;

import static com.gisgraphy.street.HouseNumberUtil.normalizeNumber;
import net.sf.jstester.util.Assert;

import org.junit.Test;

public class HouseNumberUtilTest {
	
	@Test
	public void normalizeNumberTest(){
		Assert.assertEquals("1", normalizeNumber("-1"));
		Assert.assertEquals(null, normalizeNumber("?"));
		Assert.assertEquals("3", normalizeNumber("ev.3"));
		Assert.assertEquals("2", normalizeNumber("2/1"));
		Assert.assertEquals("1", normalizeNumber("1-3"));
		Assert.assertEquals(null, normalizeNumber("A"));
		Assert.assertEquals(null, normalizeNumber(""));
		Assert.assertEquals(null, normalizeNumber(null));
		
	}

}
