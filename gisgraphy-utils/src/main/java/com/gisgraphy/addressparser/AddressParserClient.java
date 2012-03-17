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
package com.gisgraphy.addressparser;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gisgraphy.rest.BeanToRestParameter;
import com.gisgraphy.rest.IRestClient;
import com.gisgraphy.rest.RestClient;
import com.gisgraphy.serializer.common.OutputFormat;

public class AddressParserClient implements IAddressParserService {

	protected OutputFormat PREFERED_FORMAT = OutputFormat.JSON;

	public static Logger logger = LoggerFactory.getLogger(AddressParserClient.class);

	public static final String DEFAULT_ADDRESS_PARSER_BASE_URL = "http://services.gisgraphy.com/addressparser/parse";
	private String baseURL = DEFAULT_ADDRESS_PARSER_BASE_URL;

	public AddressParserClient() {
	}

	public AddressParserClient(String baseURL) {

		if (baseURL == null) {
			throw new IllegalArgumentException("the address parser client needs an url");
		}
		try {
			new URL(baseURL);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("the address parser client needs a well formed url");
		}
		this.baseURL = baseURL;
	}

	private RestClient restClient = new RestClient();

	protected IRestClient getRestClient() {
		return restClient;
	}

	public AddressResultsDto execute(AddressQuery addressQuery) throws AddressParserException {
		if (addressQuery == null) {
			throw new AddressParserException("Can not execute a null Adsress query");
		}
		try {
			addressQuery.setFormat(PREFERED_FORMAT);
			addressQuery.setCallback(null);
			String url = getUrl(addressQuery);
			AddressResultsDto results = getRestClient().get(url, AddressResultsDto.class, PREFERED_FORMAT);
			Address address = null;
			Long qTime = -1L;
			if (results!= null && results.getResult()!=null && results.getResult().size() >=1){
					address =results.getResult().get(0) ;
					qTime = results.getQTime();
			}							
			logger.info(addressQuery + " took " + qTime
					    + " ms and returns " + address );
			return results;
		} catch (Exception e) {
			throw new AddressParserException(e);
		}
	}

	public void executeAndSerialize(AddressQuery addressQuery, OutputStream outputStream) throws AddressParserException {
		if (addressQuery == null) {
			throw new AddressParserException("Can not stream a null Adsress query");
		}
		if (outputStream == null) {
			throw new AddressParserException("Can not serialize in a null stream");
		}
		try {
			String url = getUrl(addressQuery);
			getRestClient().get(url, outputStream, OutputFormat.JSON);
		} catch (Exception e) {
			throw new AddressParserException(e);
		}

	}

	public String executeToString(AddressQuery addressQuery) throws AddressParserException {
		if (addressQuery == null) {
			throw new AddressParserException("Can not stream a null Adsress query to string");
		}
		try {
			String url = getUrl(addressQuery);
			OutputStream outputStream = new ByteArrayOutputStream();
			getRestClient().get(url, outputStream, OutputFormat.JSON);
			return outputStream.toString();
		} catch (Exception e) {
			throw new AddressParserException(e);
		}
	}

	protected String getUrl(AddressQuery query) {
		return baseURL + BeanToRestParameter.toQueryString(query);
	}

	public String getBaseURL() {
		return baseURL;
	}

}
