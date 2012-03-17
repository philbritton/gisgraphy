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

import static com.gisgraphy.helper.OutputFormatHelper.isFormatSupported;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.valueobject.GisgraphyServiceType;
import com.gisgraphy.serializer.common.OutputFormat;


public class OutputFormatHelperTest {
	 @Test
	    public void getListFormatByServiceShouldImplementsAllGisgraphyService() {
		for (GisgraphyServiceType serviceType : GisgraphyServiceType.values()) {
		    try {
			OutputFormatHelper.listFormatByService(serviceType);
		    } catch (Exception e) {
			Assert.fail(e.getMessage());
		    }
		}
	    }

	    @Test
	    public void getListFormatByServiceShouldReturnCorrectValues() {
		OutputFormat[] expectedFulltext = { OutputFormat.XML, OutputFormat.JSON,
			OutputFormat.ATOM, OutputFormat.GEORSS,OutputFormat.PHP,OutputFormat.PYTHON,OutputFormat.RUBY };
		OutputFormat[] expectedForStreetAndGeoloc = { OutputFormat.XML, OutputFormat.JSON,
			OutputFormat.ATOM, OutputFormat.GEORSS,OutputFormat.PHP,OutputFormat.RUBY,OutputFormat.PYTHON , OutputFormat.YAML };
		OutputFormat[] expectedForAddress = { OutputFormat.XML, OutputFormat.JSON,
				OutputFormat.PHP,OutputFormat.RUBY,OutputFormat.PYTHON , OutputFormat.YAML };
		
		Assert.assertEquals(Arrays.asList(expectedFulltext), Arrays.asList(OutputFormatHelper
			.listFormatByService(GisgraphyServiceType.FULLTEXT)));
		Assert.assertEquals(Arrays.asList(expectedForStreetAndGeoloc), Arrays.asList(OutputFormatHelper
			.listFormatByService(GisgraphyServiceType.GEOLOC)));
		Assert.assertEquals(Arrays.asList(expectedForStreetAndGeoloc), Arrays.asList(OutputFormatHelper
			.listFormatByService(GisgraphyServiceType.STREET)));
		Assert.assertEquals(Arrays.asList(expectedForAddress), Arrays.asList(OutputFormatHelper
				.listFormatByService(GisgraphyServiceType.ADDRESS_PARSER)));

	    }
	    
	    @Test
	    public void getDefaultForServiceIfNotSupportedShouldImplementsAllGisgraphyService() {
		for (GisgraphyServiceType serviceType : GisgraphyServiceType.values()) {
		    try {
			OutputFormatHelper.getDefaultForServiceIfNotSupported(
				OutputFormat.XML, serviceType);
		    } catch (RuntimeException e) {
			Assert.fail(e.getMessage());
		    }
		}
	    }

	   

	    @Test
	    public void getDefaultForServiceIfNotSupportedShouldReturnsCorrectValues() {
		// fulltext service allows all formats
		for (OutputFormat format : OutputFormat.values()) {
			if (format ==OutputFormat.UNSUPPORTED || format == OutputFormat.YAML){
				 Assert.assertEquals(OutputFormat.getDefault(), OutputFormatHelper
						    .getDefaultForServiceIfNotSupported(format,
							    GisgraphyServiceType.FULLTEXT));
			} else{
		    Assert.assertEquals(format, OutputFormatHelper
			    .getDefaultForServiceIfNotSupported(format,
				    GisgraphyServiceType.FULLTEXT));
			}
		}
		//street
		for (GisgraphyServiceType serviceType : GisgraphyServiceType.values()){
		for (OutputFormat format : OutputFormat.values()) {
		   if (isFormatSupported(format,serviceType)){
		    Assert.assertEquals(format, OutputFormatHelper
			    .getDefaultForServiceIfNotSupported(format,
				    serviceType));
		   }
		   else {
		       Assert.assertEquals(OutputFormat.getDefault(), OutputFormatHelper.getDefaultForServiceIfNotSupported(format, serviceType));
		   }
		}
		}
	    }
	    

	
}
