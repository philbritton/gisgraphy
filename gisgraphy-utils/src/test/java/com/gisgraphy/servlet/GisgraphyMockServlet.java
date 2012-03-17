/**
 * 
 */
package com.gisgraphy.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gisgraphy.domain.valueobject.GisgraphyServiceType;
import com.gisgraphy.serializer.common.IoutputFormatVisitor;

public class GisgraphyMockServlet extends GisgraphyServlet{
	
	public GisgraphyMockServlet() {
		super();
	}

	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}



	@Override
	public IoutputFormatVisitor getErrorVisitor(String errorMessage) {
	    return null;
	}



	@Override
	public GisgraphyServiceType getGisgraphyServiceType() {
	    return null;
	}
	
}