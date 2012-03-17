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

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import com.gisgraphy.domain.Constants;


public class BeanToRestParameter {
    
    public static String toQueryString(Object object){
	if (object == null){
	    throw new RestClientException("Can not get queryString for null object");
	}
	StringBuilder sb = new StringBuilder(128);
	try {
	    boolean first=true;
	    String andValue ="&";
	    for (PropertyDescriptor thisPropertyDescriptor : Introspector.getBeanInfo(object.getClass(), Object.class).getPropertyDescriptors()) {
		Object property = PropertyUtils.getProperty(object, thisPropertyDescriptor.getName());
		if (property!=null){
		 sb.append( first?"?":andValue );
		 sb.append(thisPropertyDescriptor.getName());
		 sb.append("=");
		 sb.append(URLEncoder.encode(property.toString(),Constants.CHARSET));
		 first=false;
		}
	    }
	} catch (Exception e) {
	    throw new RestClientException("can not generate url for bean: "+e.getMessage(),e);
	}
	return sb.toString();
    }
    
    public static Map<String,String> ToMap(Object object){
    	if (object == null){
    	    throw new RestClientException("Can not get queryString for null object");
    	}
    	Map<String,String> result= new HashMap<String, String>();
    	try {
    	    for (PropertyDescriptor thisPropertyDescriptor : Introspector.getBeanInfo(object.getClass(), Object.class).getPropertyDescriptors()) {
    		Object property = PropertyUtils.getProperty(object, thisPropertyDescriptor.getName());
    		if (property!=null){
    			result.put(thisPropertyDescriptor.getName(), property.toString());
    		}
    	    }
    	} catch (Exception e) {
    	    throw new RestClientException("can not generate Map for bean : "+e.getMessage(),e);
    	}
    	return result;
        }

}
