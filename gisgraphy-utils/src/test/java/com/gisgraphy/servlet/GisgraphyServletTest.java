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
package com.gisgraphy.servlet;

import java.io.UnsupportedEncodingException;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.testing.ServletTester;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.gisgraphy.serializer.common.IoutputFormatVisitor;
import com.gisgraphy.serializer.common.OutputFormat;



public class GisgraphyServletTest {
    
    @Test
    public void initShouldTakeTheDebugParameterWhenTrue() throws Exception {

	ServletTester servletTester = new ServletTester();
	ServletHolder sh = servletTester.addServlet(GisgraphyMockServlet.class, "/*");
	sh.setInitParameter(GisgraphyServlet.DEBUG_MODE_PARAMETER_NAME, "true");
	servletTester.start();
	GisgraphyMockServlet servlet = (GisgraphyMockServlet) sh.getServlet();
	Assert.assertTrue(servlet.isDebugMode());
	servletTester.stop();

    }

    @Test
    public void initShouldTakeTheDebugParameterWhenIncorrect() throws Exception {

	ServletTester servletTester = new ServletTester();
	ServletHolder sh = servletTester.addServlet(GisgraphyMockServlet.class, "/*");
	sh.setInitParameter(GisgraphyServlet.DEBUG_MODE_PARAMETER_NAME, "foo");
	servletTester.start();
	GisgraphyMockServlet servlet = (GisgraphyMockServlet) sh.getServlet();
	Assert.assertFalse(servlet.isDebugMode());
	servletTester.stop();

    }

    @Test
    public void debugParameterShouldBeFalseByDefault() throws Exception {

	ServletTester servletTester = new ServletTester();
	ServletHolder sh = servletTester.addServlet(GisgraphyMockServlet.class, "/*");
	servletTester.start();
	GisgraphyMockServlet servlet = (GisgraphyMockServlet) sh.getServlet();
	Assert.assertFalse(servlet.isDebugMode());
    }
    
    @Test
    public void sendCustomError() throws UnsupportedEncodingException{
	final IoutputFormatVisitor errorvisitor = EasyMock.createMock(IoutputFormatVisitor.class);
	String formatedErrorMessage = "formatedErrorMessage";
	String errorMessage="Basic error Message";
	EasyMock.expect(errorvisitor.visitJSON(OutputFormat.JSON)).andReturn(formatedErrorMessage);
	EasyMock.replay(errorvisitor);
	MockHttpServletRequest request = new MockHttpServletRequest();
	MockHttpServletResponse response = new MockHttpServletResponse();
	response.setCommitted(true);
	GisgraphyMockServlet mockServlet = new GisgraphyMockServlet(){
	    public IoutputFormatVisitor getErrorVisitor(String errorMessage) {
		return errorvisitor;
	    };
	};
	mockServlet.sendCustomError(errorMessage, OutputFormat.JSON, response, request);
	EasyMock.verify(errorvisitor);
	System.out.println(response.getContentAsString());
    }

}
