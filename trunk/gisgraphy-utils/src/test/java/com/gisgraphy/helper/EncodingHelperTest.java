/*******************************************************************************
 * Gisgraphy Project 
 *  
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *  
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *    Lesser General Public License for more details.
 *  
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA
 *  
 *   Copyright 2008  Gisgraphy project 
 * 
 *   David Masclet <davidmasclet@gisgraphy.com>
 ******************************************************************************/
package com.gisgraphy.helper;

import junit.framework.Assert;

import org.junit.Test;

public class EncodingHelperTest {
    
    
    	@Test
	public void testToUTF8() {
    	    Assert.assertNotNull(EncodingHelper.toUTF8("é"));
    	    Assert.assertNull(EncodingHelper.toUTF8(null));
	
    }

	@Test
	public void testRemoveAccents() {
		Assert.assertEquals("A",EncodingHelper.removeAccents("\u00C0"));
		Assert.assertEquals("A",EncodingHelper.removeAccents("\u00C1"));
		Assert.assertEquals("A",EncodingHelper.removeAccents("\u00C2"));
		Assert.assertEquals("A",EncodingHelper.removeAccents("\u00C3"));
		Assert.assertEquals("A",EncodingHelper.removeAccents("\u00C4"));
		Assert.assertEquals("A",EncodingHelper.removeAccents("\u00C5"));
		
		Assert.assertEquals("AE",EncodingHelper.removeAccents("\u00C6"));
		
		Assert.assertEquals("C",EncodingHelper.removeAccents("\u00C7"));
		
		Assert.assertEquals("E",EncodingHelper.removeAccents("\u00C8"));
		Assert.assertEquals("E",EncodingHelper.removeAccents("\u00C9"));
		Assert.assertEquals("E",EncodingHelper.removeAccents("\u00CA"));
		Assert.assertEquals("E",EncodingHelper.removeAccents("\u00CB"));
		
		Assert.assertEquals("I",EncodingHelper.removeAccents("\u00CC"));
		Assert.assertEquals("I",EncodingHelper.removeAccents("\u00CD"));
		Assert.assertEquals("I",EncodingHelper.removeAccents("\u00CE"));
		Assert.assertEquals("I",EncodingHelper.removeAccents("\u00CF"));
		
		Assert.assertEquals("D",EncodingHelper.removeAccents("\u00D0"));
		
		Assert.assertEquals("N",EncodingHelper.removeAccents("\u00D1"));
		
		Assert.assertEquals("O",EncodingHelper.removeAccents("\u00D2"));
		Assert.assertEquals("O",EncodingHelper.removeAccents("\u00D3"));
		Assert.assertEquals("O",EncodingHelper.removeAccents("\u00D4"));
		Assert.assertEquals("O",EncodingHelper.removeAccents("\u00D5"));
		Assert.assertEquals("O",EncodingHelper.removeAccents("\u00D6"));
		Assert.assertEquals("O",EncodingHelper.removeAccents("\u00D8"));
		
		Assert.assertEquals("OE",EncodingHelper.removeAccents("\u0152"));
		
		Assert.assertEquals("TH",EncodingHelper.removeAccents("\u00DE"));
		
		Assert.assertEquals("U",EncodingHelper.removeAccents("\u00D9"));
		Assert.assertEquals("U",EncodingHelper.removeAccents("\u00DA"));
		Assert.assertEquals("U",EncodingHelper.removeAccents("\u00DB"));
		Assert.assertEquals("U",EncodingHelper.removeAccents("\u00DC"));
		
		Assert.assertEquals("Y",EncodingHelper.removeAccents("\u00DD"));
		Assert.assertEquals("Y",EncodingHelper.removeAccents("\u0178"));
		
		Assert.assertEquals("a",EncodingHelper.removeAccents("\u00E0"));
		Assert.assertEquals("a",EncodingHelper.removeAccents("\u00E1"));
		Assert.assertEquals("a",EncodingHelper.removeAccents("\u00E2"));
		Assert.assertEquals("a",EncodingHelper.removeAccents("\u00E3"));
		Assert.assertEquals("a",EncodingHelper.removeAccents("\u00E4"));
		Assert.assertEquals("a",EncodingHelper.removeAccents("\u00E5"));
		
		Assert.assertEquals("ae",EncodingHelper.removeAccents("\u00E6"));
		
		Assert.assertEquals("c",EncodingHelper.removeAccents("\u00E7"));
		

		Assert.assertEquals("e",EncodingHelper.removeAccents("\u00E8"));
		Assert.assertEquals("e",EncodingHelper.removeAccents("\u00E9"));
		Assert.assertEquals("e",EncodingHelper.removeAccents("\u00EA"));
		Assert.assertEquals("e",EncodingHelper.removeAccents("\u00EB"));

		Assert.assertEquals("i",EncodingHelper.removeAccents("\u00EC"));
		Assert.assertEquals("i",EncodingHelper.removeAccents("\u00ED"));
		Assert.assertEquals("i",EncodingHelper.removeAccents("\u00EE"));
		Assert.assertEquals("i",EncodingHelper.removeAccents("\u00EF"));
		
		Assert.assertEquals("d",EncodingHelper.removeAccents("\u00F0"));
		
		Assert.assertEquals("n",EncodingHelper.removeAccents("\u00F1"));

		Assert.assertEquals("o",EncodingHelper.removeAccents("\u00F2"));
		Assert.assertEquals("o",EncodingHelper.removeAccents("\u00F3"));
		Assert.assertEquals("o",EncodingHelper.removeAccents("\u00F4"));
		Assert.assertEquals("o",EncodingHelper.removeAccents("\u00F5"));
		Assert.assertEquals("o",EncodingHelper.removeAccents("\u00F6"));
		Assert.assertEquals("o",EncodingHelper.removeAccents("\u00F8"));
		
		Assert.assertEquals("oe",EncodingHelper.removeAccents("\u0153"));
		
		Assert.assertEquals("ss",EncodingHelper.removeAccents("\u00DF"));
		
		Assert.assertEquals("th",EncodingHelper.removeAccents("\u00FE"));
		
		Assert.assertEquals("u",EncodingHelper.removeAccents("\u00F9"));
		Assert.assertEquals("u",EncodingHelper.removeAccents("\u00FA"));
		Assert.assertEquals("u",EncodingHelper.removeAccents("\u00FB"));
		Assert.assertEquals("u",EncodingHelper.removeAccents("\u00FC"));
		
		Assert.assertEquals("y",EncodingHelper.removeAccents("\u00FD"));
		Assert.assertEquals("y",EncodingHelper.removeAccents("\u00FF"));
		
		Assert.assertEquals("letter without accent should not be modified","- Je me souviens de ce zouave qui jouait du xylophone en buvant du whisky.;",
				EncodingHelper.removeAccents("- Je me souviens de ce zouave qui jouait du xylophone en buvant du whisky.;"));
		
	}

}
