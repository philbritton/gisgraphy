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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.Test;
import org.mortbay.jetty.testing.ServletTester;

import com.gisgraphy.serializer.UniversalSerializer;
import com.gisgraphy.serializer.common.OutputFormat;
import com.gisgraphy.serializer.testdata.BeanForTest;
import com.gisgraphy.serializer.testdata.TestUtils;

public class RestClientTest {

	BeanForTest beanForTest = TestUtils.createBeanForTest();

	@Test
	public void getToObjectWithStatusOK() throws Exception {
		ServletTester servletTester = new ServletTester();
		String url = servletTester.createSocketConnector(true);
		System.out.println(url);
		servletTester.addServlet(MockServlet.class, "/*");
		servletTester.start();
		RestClient restClient = new RestClient();
		BeanForTest beanForTestActual =  restClient.get(url+"/test",BeanForTest.class, OutputFormat.JSON);
		Assert.assertEquals(beanForTest, beanForTestActual);

	}
	@Test
	public void getToObjectWithNullURL() throws Exception {
		RestClient restClient = new RestClient();
		 try {
			restClient.get(null,BeanForTest.class, OutputFormat.JSON);
			Assert.fail();
		} catch (RestClientException e) {
		}

	}
	
	@Test
	public void getToOutputStreamWithNullURL() throws Exception {
		RestClient restClient = new RestClient();
		 try {
			restClient.get(null,new ByteArrayOutputStream(), OutputFormat.JSON);
			Assert.fail();
		} catch (RestClientException e) {
		}

	}
	
	
	@Test
	public void getInOutputStreamWithStatusOK() throws Exception {
		ServletTester servletTester = new ServletTester();
		String url = servletTester.createSocketConnector(true);
		System.out.println(url);
		servletTester.addServlet(MockServlet.class, "/*");
		servletTester.start();
		RestClient restClient = new RestClient();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		restClient.get(url+"/test",byteArrayOutputStream, OutputFormat.JSON);
		String resultString = byteArrayOutputStream.toString();
		Assert.assertEquals(beanForTest,UniversalSerializer.getInstance().read(new ByteArrayInputStream(resultString.getBytes()), BeanForTest.class, OutputFormat.JSON));

	}
	@Test(expected=RuntimeException.class)
	public void constructorDoesNotAcceptNullMultithreadedConnectionManager(){
		new RestClient(null);
		
	}

}
