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
/**
 *
 */
package com.gisgraphy.addressparser.web;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.gisgraphy.addressparser.Address;
import com.gisgraphy.addressparser.AddressQuery;
import com.gisgraphy.addressparser.StructuredAddressQuery;
import com.gisgraphy.addressparser.exception.AddressParserException;
import com.gisgraphy.domain.valueobject.GisgraphyServiceType;
import com.gisgraphy.helper.IntrospectionHelper;
import com.gisgraphy.helper.OutputFormatHelper;
import com.gisgraphy.serializer.common.OutputFormat;
import com.gisgraphy.servlet.AbstractAddressServlet;
import com.gisgraphy.servlet.GisgraphyServlet;

/**
 * An address Query builder. it build Address query from HTTP Request
 * 
 * @see AddressParser
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class AddressQueryHttpBuilder {

	private static AddressQueryHttpBuilder instance = new AddressQueryHttpBuilder();

	public static AddressQueryHttpBuilder getInstance() {
		return instance;

	}

	/** This map allows to associate a parameter name that can be in any case (sensitive) to an address fields
	 *  
	 */
	private static Map<String,String> parameterNameToAddressFields = new HashMap<String, String>(){
		{
			for (String fieldName : IntrospectionHelper.getFieldsAsList(Address.class)){
				put(fieldName.toLowerCase(),fieldName);
			}
		}
	};
	
	/**
	 * @param req
	 *            an HttpServletRequest to construct a {@link AddressQuery}
	 */
	public AddressQuery buildFromRequest(HttpServletRequest req) {
		AddressQuery query = null;
		if (req==null){
			throw new AddressParserException("could not build address query from a null HTTP request");
		}
		// country parameter
		String countryParameter = req.getParameter(AbstractAddressServlet.COUNTRY_PARAMETER);
		if (countryParameter == null || countryParameter.trim().length() == 0) {
			//throw new AddressParserException("country parameter is not specified or empty");
			countryParameter=null;
		}
		// address Parameter
		String adressParameter = req.getParameter(AbstractAddressServlet.ADDRESS_PARAMETER);
		if (adressParameter == null) {
			Address address = buildAddressFromRequest(req);
			if (address == null){
				//no setter has been callled
				throw new AddressParserException("no structured address, nor address parameter is specified");
			}
			query =  new StructuredAddressQuery(address, countryParameter);
		}
		else {
			if ("".equals(adressParameter.trim())){
				throw new AddressParserException("'AbstractAddressServlet.ADDRESS_PARAMETER' could not be empty, please specify a valid parameter or some valid address fields");
			}
			if (adressParameter.length() > AbstractAddressServlet.QUERY_MAX_LENGTH) {
				throw new AddressParserException("address is limited to " + AbstractAddressServlet.QUERY_MAX_LENGTH + "characters");
			}
			query = new AddressQuery(adressParameter, countryParameter);
		}

		// outputformat
		OutputFormat outputFormat = OutputFormat.getFromString(req.getParameter(AbstractAddressServlet.FORMAT_PARAMETER));
		outputFormat = OutputFormatHelper.getDefaultForServiceIfNotSupported(outputFormat, GisgraphyServiceType.ADDRESS_PARSER);
		query.setFormat(outputFormat);

		String callbackParameter = req.getParameter(AbstractAddressServlet.CALLBACK_PARAMETER);
		if (callbackParameter != null) {
			query.setCallback(callbackParameter);
		}
		// indent
		if ("true".equalsIgnoreCase(req.getParameter(AbstractAddressServlet.INDENT_PARAMETER)) || "on".equalsIgnoreCase(req.getParameter(AbstractAddressServlet.INDENT_PARAMETER))) {
			query.setIndent(true);
		}

		// Postal
		if ("true".equalsIgnoreCase(req.getParameter(AbstractAddressServlet.POSTAL_PARAMETER)) || "on".equalsIgnoreCase(req.getParameter(AbstractAddressServlet.POSTAL_PARAMETER))) {
			query.setPostal(true);
		}
		
		// standardize
		if ("true".equalsIgnoreCase(req.getParameter(AbstractAddressServlet.STANDARDIZE_PARAMETER)) || "on".equalsIgnoreCase(req.getParameter(AbstractAddressServlet.STANDARDIZE_PARAMETER))) {
			query.setStandardize(true);
		}
		
		// apiKey
		String apiKey = req.getParameter(GisgraphyServlet.APIKEY_PARAMETER);
		query.setApikey(apiKey);

		return query;
	}

	/**
	 * build an Address from the request parameter names, parameter names are case sensitive.
	 * Extra parameter names are ignored
	 * @param req
	 * @return
	 */
	public Address buildAddressFromRequest(HttpServletRequest req) {
		if (req==null){
			throw new AddressParserException("could not build an address from a null HTTP request");
		}
		Address address = new Address();
		Map<String,String[]> parameterNames = req.getParameterMap();
		boolean atLeastOneSetterFound = false;
		for(Entry<String, String[]> parameters :parameterNames.entrySet()){
			String parameterNameFromQuery =(String) parameters.getKey();
			String parameterName = getFieldNameFromParameter(parameterNameFromQuery);
			if (parameters.getValue().length==1){
				if (AbstractAddressServlet.COUNTRY_PARAMETER.equalsIgnoreCase(parameterName)){
					//the country parameter should not be set, it is only a populated field and should not be confused with countrycode.
					//that's why we consider an unsuccess execution, if we return true and only the country is specified, the returned address won't be null
					//even if only the country is specified
					continue;
				}
				boolean success =setAddressField(address, parameterName, parameters.getValue()[0]);
				if (success){
					atLeastOneSetterFound=true;
				}
			}
		}
		if (atLeastOneSetterFound){
		return address;
		} else {
			return null;
		}
		
	}

	String getFieldNameFromParameter(String parameterNameFromQuery) {
		if (parameterNameFromQuery==null){
			return null;
		}
		String fieldName =  parameterNameToAddressFields.get(parameterNameFromQuery.toLowerCase());
		return fieldName==null? parameterNameFromQuery:fieldName;
	}

	protected static boolean setAddressField(Address address, String fieldName, String value) {
		try {
			if (fieldName==null){
				return false;
			}
			String setter = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			Method method;
			try {
				method = Address.class.getMethod(setter, new Class[] { String.class });
				method.invoke(address, new Object[] { value });
			} catch (NoSuchMethodException e) {
				return false;
			}
			return true;
		} catch (Exception e) {
			throw new RuntimeException("exception in setAddressField : " + e.getMessage(), e);
		}
}
	
}
