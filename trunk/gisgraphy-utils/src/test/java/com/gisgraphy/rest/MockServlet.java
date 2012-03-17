/**
 * 
 */
package com.gisgraphy.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gisgraphy.serializer.UniversalSerializer;
import com.gisgraphy.serializer.common.OutputFormat;
import com.gisgraphy.serializer.testdata.TestUtils;

public class MockServlet extends HttpServlet{
	
	public MockServlet() {
		super();
	}

	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 UniversalSerializer.getInstance().write(resp.getOutputStream(), TestUtils.createBeanForTest(), OutputFormat.JSON);
	}
	
}