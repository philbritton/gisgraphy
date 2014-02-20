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

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.testing.ServletTester;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.gisgraphy.addressparser.AddressQuery;
import com.gisgraphy.addressparser.IAddressParserService;
import com.gisgraphy.addressparser.exception.AddressParserException;
import com.gisgraphy.addressparser.web.AddressQueryHttpBuilder;
import com.gisgraphy.domain.valueobject.GisgraphyServiceType;
import com.gisgraphy.helper.OutputFormatHelper;
import com.gisgraphy.serializer.common.OutputFormat;

public class AbstractAddressServletTest {

    private boolean customErrorSended = false;
    private boolean processRequest = false;
    private IAddressParserService mockAddressParserService;
    private AddressQueryHttpBuilder mockAddressQueryHttpBuilder;

    @Before
    public void setup() {
	customErrorSended = false;
	processRequest = false;
	mockAddressParserService = EasyMock.createNiceMock(IAddressParserService.class);
	mockAddressParserService.executeAndSerialize((AddressQuery) EasyMock.anyObject(), (OutputStream) EasyMock.anyObject());
	EasyMock.replay(mockAddressParserService);

	mockAddressQueryHttpBuilder = EasyMock.createNiceMock(AddressQueryHttpBuilder.class);
	EasyMock.expect(mockAddressQueryHttpBuilder.buildFromRequest((HttpServletRequest) EasyMock.anyObject())).andReturn(new AddressQuery("address", "us"));
	EasyMock.replay(mockAddressQueryHttpBuilder);

    }

    @Test
    public void initShouldTakeTheDebugParameterWhenTrue() throws Exception {

	ServletTester servletTester = new ServletTester();
	ServletHolder sh = servletTester.addServlet(ConcreteAddressParserServlet.class, "/*");
	sh.setInitParameter(GisgraphyServlet.DEBUG_MODE_PARAMETER_NAME, "true");
	servletTester.start();
	ConcreteAddressParserServlet servlet = (ConcreteAddressParserServlet) sh.getServlet();
	Assert.assertTrue(servlet.isDebugMode());
	servletTester.stop();

    }

    @Test
    public void initShouldTakeTheDebugParameterWhenIncorrect() throws Exception {

	ServletTester servletTester = new ServletTester();
	ServletHolder sh = servletTester.addServlet(ConcreteAddressParserServlet.class, "/*");
	sh.setInitParameter(GisgraphyServlet.DEBUG_MODE_PARAMETER_NAME, "foo");
	servletTester.start();
	ConcreteAddressParserServlet servlet = (ConcreteAddressParserServlet) sh.getServlet();
	Assert.assertFalse(servlet.isDebugMode());
	servletTester.stop();

    }

    @Test
    public void debugParameterShouldBeFalse() throws Exception {

	ServletTester servletTester = new ServletTester();
	ServletHolder sh = servletTester.addServlet(ConcreteAddressParserServlet.class, "/*");
	servletTester.start();
	ConcreteAddressParserServlet servlet = (ConcreteAddressParserServlet) sh.getServlet();
	Assert.assertFalse(servlet.isDebugMode());
    }

    private AbstractAddressServlet servlet = new AbstractAddressServlet() {
	public com.gisgraphy.addressparser.IAddressParserService getAddressParserService() {
	    return mockAddressParserService;
	};
	@Override
	public boolean checkparameter() {
			return false;
	}

	@Override
	public void sendCustomError(String arg0, OutputFormat arg1, HttpServletResponse arg2, HttpServletRequest arg3) {
	    customErrorSended = true;
	}

	@Override
	protected AddressQueryHttpBuilder getAddressQueryHttpBuilder() {
	    return mockAddressQueryHttpBuilder;
	}

	@Override
	public void processRequest(AddressQuery query, HttpServletResponse resp) throws IOException {
	    processRequest=true;
	}

	@Override
	public GisgraphyServiceType getGisgraphyServiceType() {
	    return GisgraphyServiceType.ADDRESS_PARSER;
	};
	
	
    };

    @Test
    public void doGetWithoutParameter() throws AddressParserException, IOException {
	MockHttpServletResponse response = new MockHttpServletResponse();

	MockHttpServletRequest request = new MockHttpServletRequest();
	servlet.doGet(request, response);
	Assert.assertTrue(customErrorSended);
    }
    
    
    @Test
    public void doGetWithoutParameter_without_checkParameter() throws AddressParserException, IOException {
    AbstractAddressServlet servlet = new AbstractAddressServlet() {
    			public com.gisgraphy.addressparser.IAddressParserService getAddressParserService() {
    			    return mockAddressParserService;
    			};
    			@Override
    			public boolean checkparameter() {
    					return false;
    			}

    			@Override
    			public void sendCustomError(String arg0, OutputFormat arg1, HttpServletResponse arg2, HttpServletRequest arg3) {
    			    customErrorSended = false;
    			}

    			@Override
    			protected AddressQueryHttpBuilder getAddressQueryHttpBuilder() {
    			    return mockAddressQueryHttpBuilder;
    			}

    			@Override
    			public void processRequest(AddressQuery query, HttpServletResponse resp) throws IOException {
    			    processRequest=true;
    			}

    			@Override
    			public GisgraphyServiceType getGisgraphyServiceType() {
    			    return GisgraphyServiceType.ADDRESS_PARSER;
    			};
    			
    			
    		    };
    	
	MockHttpServletResponse response = new MockHttpServletResponse();

	MockHttpServletRequest request = new MockHttpServletRequest();
	servlet.doGet(request, response);
	Assert.assertFalse(customErrorSended);

    }
    
    @Test
    public void doGetWithoutParameter_with_checkParameter() throws AddressParserException, IOException {
    AbstractAddressServlet servlet = new AbstractAddressServlet() {
    			public com.gisgraphy.addressparser.IAddressParserService getAddressParserService() {
    			    return mockAddressParserService;
    			};
    			@Override
    			public boolean checkparameter() {
    					return true;
    			}

    			@Override
    			public void sendCustomError(String arg0, OutputFormat arg1, HttpServletResponse arg2, HttpServletRequest arg3) {
    			    customErrorSended = false;
    			}

    			@Override
    			protected AddressQueryHttpBuilder getAddressQueryHttpBuilder() {
    			    return mockAddressQueryHttpBuilder;
    			}

    			@Override
    			public void processRequest(AddressQuery query, HttpServletResponse resp) throws IOException {
    			    processRequest=true;
    			}

    			@Override
    			public GisgraphyServiceType getGisgraphyServiceType() {
    			    return GisgraphyServiceType.ADDRESS_PARSER;
    			};
    			
    			
    		    };
    	
	MockHttpServletResponse response = new MockHttpServletResponse();

	MockHttpServletRequest request = new MockHttpServletRequest();
	servlet.doGet(request, response);
	Assert.assertFalse(customErrorSended);

    }

    @Test
    public void doGetWithoutAddressParameter() throws AddressParserException, IOException {
	MockHttpServletResponse response = new MockHttpServletResponse();

	MockHttpServletRequest request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	servlet.doGet(request, response);
	Assert.assertTrue(customErrorSended);

    }

    @Test
    public void doGetWithoutCountryParameter() throws AddressParserException, IOException {
	MockHttpServletResponse response = new MockHttpServletResponse();

	MockHttpServletRequest request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	servlet.doGet(request, response);
	Assert.assertTrue(customErrorSended);

    }

    @Test
    public void doGetWithRequiredParameter() throws AddressParserException, IOException {
	MockHttpServletResponse response = new MockHttpServletResponse();

	MockHttpServletRequest request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
	servlet.doGet(request, response);
	Assert.assertFalse(customErrorSended);
	Assert.assertTrue(processRequest);
	EasyMock.verify(mockAddressQueryHttpBuilder);

    }

    @Test
    public void testServletShouldReturnCorrectContentType() {

	for (OutputFormat format : OutputFormat.values()) {
	    try {
		MockHttpServletResponse response = new MockHttpServletResponse();

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter(AbstractAddressServlet.ADDRESS_PARAMETER, "address");
		request.setParameter(AbstractAddressServlet.COUNTRY_PARAMETER, "us");
		request.setParameter(AbstractAddressServlet.FORMAT_PARAMETER, format.name());
		servlet.doGet(request, response);

		String contentType = (String) response.getHeader("Content-Type");
		if (OutputFormatHelper.isFormatSupported(format, servlet.getGisgraphyServiceType())) {
		    Assert.assertEquals(format.getContentType(), contentType);
		}
	    } catch (IOException e) {
		Assert.fail("An exception has occured " + e.getMessage());
	    }
	}

    }

    @Test
    public void testServletShouldReturnCorrectErrorVisitor() {

    }

}
