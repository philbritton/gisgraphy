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
package com.gisgraphy.compound;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class DecompoudTest {
	@SuppressWarnings("serial")
	private static List<String> words = new ArrayList<String>(){ 
		{
		add("weg");
		add("strasse"); 
		add("wald");  
		}
	};

	@Test
	public void testDecompound2(){
		Decompounder d = new Decompounder(words);
		
		String[] stringsToTest = {"foo","fooweg","fOOWeg","wegfoo","wegwaldfoo","wegfoowald"};
		String[][] results= {{"foo"},{"fooweg","foo","weg"},{"fOOWeg","fOO","Weg"},{"wegfoo","weg","foo"},{"wegwaldfoo","weg","wald","foo"},{"wegfoowald","weg","foo","wald"}};
		for(int i=0;i<stringsToTest.length;i++){
			String str = stringsToTest[i];
			String[] actual = d.decompound(str);
			System.out.println(str+":"+Arrays.toString(actual));
			Assert.assertArrayEquals("expected "+Arrays.toString(results[i]) + "but was "+Arrays.toString(actual), results[i], actual);
		}
	}
	
	@Test(expected=RuntimeException.class)
	public void NullWordsList(){
		new Decompounder(null);
	}

}
