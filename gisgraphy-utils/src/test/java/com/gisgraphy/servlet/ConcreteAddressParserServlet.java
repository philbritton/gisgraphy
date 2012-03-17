/**
 * 
 */
package com.gisgraphy.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.gisgraphy.addressparser.AddressQuery;
import com.gisgraphy.domain.valueobject.GisgraphyServiceType;
import com.gisgraphy.serializer.common.IoutputFormatVisitor;

public class ConcreteAddressParserServlet extends AbstractAddressServlet{
	
    @Override
   public  void processRequest(AddressQuery query,HttpServletResponse resp) throws IOException {
    };
    
    /**
     * 
     */
    private static final long serialVersionUID = -2124269033751536102L;



	public ConcreteAddressParserServlet() {
		super();
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