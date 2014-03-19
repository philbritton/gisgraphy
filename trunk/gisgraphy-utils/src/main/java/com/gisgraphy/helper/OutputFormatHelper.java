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

import com.gisgraphy.domain.valueobject.GisgraphyServiceType;
import com.gisgraphy.serializer.common.OutputFormat;
import com.gisgraphy.serializer.exception.UnsupportedFormatException;

public class OutputFormatHelper {

    private final static OutputFormat[] FULLTEXTSEARCH_SUPPORTED_FORMAT =  { OutputFormat.XML, OutputFormat.JSON,
		OutputFormat.ATOM, OutputFormat.GEORSS,OutputFormat.PHP,OutputFormat.PYTHON,OutputFormat.RUBY };
    
    private final static OutputFormat[] GEOLOCSEARCH_SUPPORTED_FORMAT = { OutputFormat.XML, OutputFormat.JSON, OutputFormat.ATOM, OutputFormat.GEORSS,OutputFormat.PHP,OutputFormat.RUBY,OutputFormat.PYTHON,OutputFormat.YAML};
    
    private final static OutputFormat[] STREETSEARCH_SUPPORTED_FORMAT = { OutputFormat.XML, OutputFormat.JSON, OutputFormat.ATOM, OutputFormat.GEORSS,OutputFormat.PHP,OutputFormat.RUBY,OutputFormat.PYTHON ,OutputFormat.YAML};
    
    private final static OutputFormat[] ADDRESS_PARSER_SUPPORTED_FORMAT = { OutputFormat.XML, OutputFormat.JSON, OutputFormat.PHP,OutputFormat.RUBY,OutputFormat.PYTHON,OutputFormat.YAML };
    
    private final static OutputFormat[] REVERSEGEOCODING_SUPPORTED_FORMAT = ADDRESS_PARSER_SUPPORTED_FORMAT;
    
    private final static OutputFormat[] GEOCODING_SUPPORTED_FORMAT = { OutputFormat.XML, OutputFormat.JSON, OutputFormat.PHP,OutputFormat.RUBY,OutputFormat.PYTHON,OutputFormat.YAML };
    
	/**
	 * @param serviceType
	 *                the service type we'd like to know all the formats
	 * @return the formats for the specified service
	 * @throws RuntimeException
	 *                 if the service is not implemented by the algorithm
	 */
	public static OutputFormat[] listFormatByService(
		GisgraphyServiceType serviceType) {
	    switch (serviceType) {
	    case FULLTEXT:
		return FULLTEXTSEARCH_SUPPORTED_FORMAT;
	    case GEOLOC:
		return GEOLOCSEARCH_SUPPORTED_FORMAT;
	    case STREET:
		return STREETSEARCH_SUPPORTED_FORMAT;
	    case ADDRESS_PARSER:
		return ADDRESS_PARSER_SUPPORTED_FORMAT;
	    case GEOCODING:
		return GEOCODING_SUPPORTED_FORMAT;
	    case REVERSEGEOCODING:
			return REVERSEGEOCODING_SUPPORTED_FORMAT;
	    default:
		throw new RuntimeException("The service type "
			+ serviceType + " is not implemented");
	    }
	
	}

	/**
	 * @param format
	 *                the format to check
	 * @param serviceType
	 *                the service type we'd like to know if the format is
	 *                applicable
	 * @return the format if the format is applicable for the service or the
	 *         default one.
	 * @throws UnsupportedFormatException
	 *                 if the service is not implemented by the algorithm
	 */
	public static OutputFormat getDefaultForServiceIfNotSupported(OutputFormat format,
		GisgraphyServiceType serviceType) {
	    switch (serviceType) {
	    case FULLTEXT:
		// fulltext accept all formats
		return isFormatSupported(format,serviceType)==true?format:OutputFormat.getDefault();
	    case GEOLOC:
		return isFormatSupported(format,serviceType)==true?format:OutputFormat.getDefault();
	    case STREET:
		return isFormatSupported(format,serviceType)==true?format:OutputFormat.getDefault();
	    case ADDRESS_PARSER:
		return isFormatSupported(format,serviceType)==true?format:OutputFormat.getDefault();
	    case GEOCODING:
		return isFormatSupported(format,serviceType)==true?format:OutputFormat.getDefault();
	    case REVERSEGEOCODING:
			return isFormatSupported(format,serviceType)==true?format:OutputFormat.getDefault();
	    default:
		throw new UnsupportedFormatException("The service type "
			+ serviceType + " is not implemented");
	    }
	}
	
	/**
	 * @param serviceType the type of service we'd like to know if the format is supported
	 * @param outputFormat the output format
	 * @return true if the format is supported by the specified {@link GisgraphyServiceType}
	 */
	public static boolean isFormatSupported(OutputFormat outputFormat,GisgraphyServiceType serviceType){
		for (OutputFormat format : listFormatByService(serviceType)){
		    if (format == outputFormat){
			return true;
		    }
		} return false;
	}

}
