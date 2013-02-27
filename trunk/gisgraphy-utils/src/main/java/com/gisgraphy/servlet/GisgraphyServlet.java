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
package com.gisgraphy.servlet;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gisgraphy.domain.valueobject.GisgraphyServiceType;
import com.gisgraphy.helper.EncodingHelper;
import com.gisgraphy.helper.OutputFormatHelper;
import com.gisgraphy.serializer.common.IoutputFormatVisitor;
import com.gisgraphy.serializer.common.OutputFormat;

/**
 * provides utility function for Gisgraphy Service
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 * @see GeolocServlet
 * @see FulltextServlet
 */
public abstract class  GisgraphyServlet extends HttpServlet {
    
    public static final String DEBUG_MODE_PARAMETER_NAME = "debugMode";
    
    @Override
    public void init() throws ServletException {
        super.init();
	    this.debugMode = Boolean.valueOf(getInitParameter(DEBUG_MODE_PARAMETER_NAME));
	    logger.info(this.getClass().getSimpleName()+" debugmode = " + this.debugMode);
	    EncodingHelper.setJVMEncodingToUTF8();
    }

   
    protected boolean debugMode = false;


    public static final String INDENT_PARAMETER = "indent";
    public static final String TO_PARAMETER = "to";
    public static final String FROM_PARAMETER = "from";
    public static final String FORMAT_PARAMETER = "format";
    public static final String APIKEY_PARAMETER = "apikey";
    
    /**
     * @return the {@link GisgraphyServiceType} that the servlet handle
     */
    public abstract GisgraphyServiceType getGisgraphyServiceType();
    
    /**
     * @param errorMessage the error message to be treated by the visitor
     * @return an instance of the outputFormatVisitor for the servlet
     */
    public abstract IoutputFormatVisitor getErrorVisitor(String errorMessage);
   

    /**
     * Default serialVersionUID
     */
    private static final long serialVersionUID = -90545482454895743L;


    /**
     * The logger
     */
    protected static final Logger logger = LoggerFactory
	    .getLogger(GisgraphyServlet.class);
    
    protected OutputFormat setResponseContentType(HttpServletRequest req,
	    HttpServletResponse resp) {
	OutputFormat format;
	String formatParam = req.getParameter(GisgraphyServlet.FORMAT_PARAMETER);
	format = OutputFormat.getFromString(formatParam);
	format = OutputFormatHelper.getDefaultForServiceIfNotSupported(format, getGisgraphyServiceType());
	resp.setHeader("content-type", format.getContentType());
	return format;
    }

    public void sendCustomError(String errorMessage, OutputFormat format,
	    HttpServletResponse resp,HttpServletRequest req) {
	IoutputFormatVisitor visitor = getErrorVisitor(errorMessage);
	String response = format.accept(visitor);
	Writer writer = null;
	try {
	   if (!resp.isCommitted()){
	    resp.reset();
	    resp.setStatus(500);
	    setResponseContentType(req, resp);
	    writer = resp.getWriter();
	    if (writer != null ){
	    writer.append(response);
	    writer.flush();
	}
	    
	    }
	   
	} catch (IOException e) {
	    logger.warn("error when sending error : "+e.getMessage());
	}
	catch (IllegalStateException e) {
	    logger.warn("Can not send error because the response has already been send : "+e.getMessage());
	}
	finally {
	    if (writer != null) {
		try {
		    writer.close();
		} catch (Exception e) {
		    logger
			    .warn("Error when closing writer after sending error : "+e.getMessage());
		}
	    }
	}
    }
    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doHead(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doOptions(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doTrace(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    
    /**
     * @return the debugMode
     */
    public boolean isDebugMode() {
	return debugMode;
    }

    /**
     * @param debugMode
     *                the debugMode to set
     */
    public void setDebugMode(boolean debugMode) {
	this.debugMode = debugMode;
    }
    

}
