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
package com.gisgraphy.rest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.test.GisgraphyUtilsTestHelper;


public class BeanToRestParameterTest {
    
    @Test(expected=RestClientException.class)
    public void toQueryStringWithNull(){
    	BeanToRestParameter.toQueryString(null);
    }
    
    @Test(expected=RestClientException.class)
    public void toMapWithNull(){
    	BeanToRestParameter.ToMap(null);
    }
    
    @Test
    public void toQueryStringWithSomeNullValues(){
	SampleBean query = new SampleBean("foo", "bar");
	String queryString = BeanToRestParameter.toQueryString(query);
	Map<String, String> parameters = GisgraphyUtilsTestHelper.splitURLParams(queryString,"&");
	Assert.assertTrue(queryString.startsWith("?"));
	Assert.assertEquals(query.getParam1(),parameters.get("param1"));
	Assert.assertEquals(2,parameters.size());
	Assert.assertEquals(query.getParam2(),parameters.get("param2"));
	Assert.assertNull(parameters.get("param3"));
    }
    
    @Test
    public void toMapWithSomeNullValues(){
	SampleBean query = new SampleBean("foo", "bar");
	Map<String,String> map = BeanToRestParameter.ToMap(query);
	Assert.assertEquals(2,map.size());
	Assert.assertEquals(query.getParam1(),map.get("param1"));
	Assert.assertEquals(query.getParam2(),map.get("param2"));
    }
    
    


}
