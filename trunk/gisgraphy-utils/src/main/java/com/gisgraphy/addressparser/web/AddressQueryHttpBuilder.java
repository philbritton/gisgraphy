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

import javax.servlet.http.HttpServletRequest;

import com.gisgraphy.addressparser.AddressQuery;
import com.gisgraphy.addressparser.exception.AddressParserException;
import com.gisgraphy.domain.valueobject.GisgraphyServiceType;
import com.gisgraphy.helper.OutputFormatHelper;
import com.gisgraphy.serializer.common.OutputFormat;
import com.gisgraphy.servlet.AbstractAddressServlet;

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

	/**
	 * @param req
	 *            an HttpServletRequest to construct a {@link AddressQuery}
	 */
	public AddressQuery buildFromRequest(HttpServletRequest req) {

		// address Parameter
		String adressParameter = req.getParameter(AbstractAddressServlet.ADDRESS_PARAMETER);
		if (adressParameter == null || "".equals(adressParameter.trim())) {
			throw new AddressParserException("address is not specified or empty");
		}
		if (adressParameter.length() > AbstractAddressServlet.QUERY_MAX_LENGTH) {
			throw new AddressParserException("address is limited to " + AbstractAddressServlet.QUERY_MAX_LENGTH + "characters");
		}
		// country parameter
		String countryParameter = req.getParameter(AbstractAddressServlet.COUNTRY_PARAMETER);
		if (countryParameter == null || countryParameter.trim().length() == 0) {
			throw new AddressParserException("country parameter is not specified or empty");
		}

		AddressQuery query = new AddressQuery(adressParameter, countryParameter);
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

		return query;
	}

}
