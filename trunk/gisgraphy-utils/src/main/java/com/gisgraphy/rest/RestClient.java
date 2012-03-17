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
package com.gisgraphy.rest;

import static java.lang.String.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gisgraphy.client.ClientConfig;
import com.gisgraphy.serializer.UniversalSerializer;
import com.gisgraphy.serializer.common.OutputFormat;
import com.gisgraphy.serializer.common.SerializerException;

/**
 * Default (threadsafe) implementation of {@link IRestClient}
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class RestClient implements IRestClient {

	private HttpClient httpClient;
	
	private static Logger logger = LoggerFactory.getLogger(RestClient.class);

	/**
	 * Default constructor 
	 */
	public RestClient() {
		this(new MultiThreadedHttpConnectionManager());
	}

	/**
	
	
	/**
	 * @param multiThreadedHttpConnectionManager
	 *                The
	 * @link {@link MultiThreadedHttpConnectionManager} that the fulltext search
	 *       engine will use
	 * @throws RestClientException
	 *                 If an error occured
	 */
	public RestClient(MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager) throws RestClientException {
		if (multiThreadedHttpConnectionManager == null) {
			throw new RestClientException("multiThreadedHttpConnectionManager can not be null");
		}
		this.httpClient = new HttpClient(multiThreadedHttpConnectionManager);
		HttpClientParams httpClientParams = new HttpClientParams();
		httpClientParams.setHttpElementCharset("utf8");
		httpClientParams.setConnectionManagerTimeout(15000);
		httpClientParams.setParameter(HttpMethodParams.USER_AGENT, ClientConfig.CLIENT_USER_AGENT);
		httpClient.setParams(httpClientParams);

		if (this.httpClient == null) {
			throw new RestClientException("Can not instanciate http client with multiThreadedHttpConnectionManager : " + multiThreadedHttpConnectionManager);
		}
	}

	/* (non-Javadoc)
	 * @see com.gisgraphy.rest.IRestClient#get(java.lang.String, java.lang.Class, com.gisgraphy.serializer.OutputFormat)
	 */
	public <T> T get(String url, Class<T> classToBeBound, OutputFormat format) throws RestClientException {
		if (url == null) {
			throw new RestClientException("Can not call a null url");
		}
		GetMethod getMethod = null;
		try {
			if (classToBeBound == null) {
				throw new RestClientException("Can not bound a null class");
			}
			logger.info("will call url : "+url);
			getMethod = new GetMethod(url);
			InputStream inputStream = executeMethod(getMethod);
			T obj = UniversalSerializer.getInstance().read(inputStream, classToBeBound, format);
			return obj;
		} catch (SerializerException e) {
			throw new RestClientException("an error occured during de-serialization of the http stream " + e.getMessage(), e);
		} finally {
			if (getMethod != null) {
				getMethod.releaseConnection();
			}
		}

	}
	
	public void get(String url, OutputStream outputStream, OutputFormat format) throws RestClientException {
		if (url == null) {
			throw new RestClientException("Can not call a null url");
		}
		GetMethod getMethod = null;
		try {
			if (outputStream == null) {
				throw new RestClientException("Can not serialize in a null outputStream");
			}
			logger.info("will call url : "+url);
			getMethod = new GetMethod(url);
			InputStream inputStream = executeMethod(getMethod);
			 int numRead;
			 byte[] buf =new byte[256];
		      while ( (numRead = inputStream.read(buf) ) >= 0) {
		          outputStream.write(buf, 0, numRead);
		      }
		} catch (IOException e) {
			throw new RestClientException("An error occured during writing of the stream" + e.getMessage(), e);
		} finally {
			if (getMethod != null) {
				getMethod.releaseConnection();
			}
			try {
				outputStream.flush();
				outputStream.close();
			} catch (IOException e) {
			}
		}

	}

	private InputStream executeMethod(HttpMethod httpMethod) throws RestClientException {
		int statusCode = -1;
		try {
			statusCode = executeAndCheckStatusCode(httpMethod);
			logger.info("URL "+httpMethod.getURI()+" returns http status :"+statusCode);
			return httpMethod.getResponseBodyAsStream();
		} catch (HttpException e) {
			throw new RestClientException(statusCode, e.getMessage());
		} catch (IOException e) {
			throw new RestClientException(statusCode, e.getMessage());
		}
	}

	private int executeAndCheckStatusCode(HttpMethod httpMethod) throws IOException, HttpException, RestClientException {
		int statusCode = httpClient.executeMethod(httpMethod);
		if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_CREATED && statusCode != HttpStatus.SC_NO_CONTENT) {
			throw new RestClientException(statusCode, "restclient exception for "+httpMethod.getURI()+" : "+statusCode+", "+HttpStatus.getStatusText(statusCode));
		}
		return statusCode;
	}

	private HttpMethod createPostMethod(String url, Map<String, Object> map) throws RestClientException {
		PostMethod httpMethod = new PostMethod(url);

		if (map != null) {
			for (String key : map.keySet()) {
				if (map.get(key) != null) {
					httpMethod.addParameter(key, map.get(key).toString());
				}
			}
		}

		httpMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		return httpMethod;
	}

	private HttpMethod createPutMethod(String url, Map<String, Object> map) throws RestClientException {
		PutMethod httpMethod = new PutMethod(url);
		StringBuilder responseBody = new StringBuilder();
		if (map != null) {
			for (String key : map.keySet()) {
				if (map.get(key) != null) {
					responseBody.append(format("%s=%s\n", key, map.get(key).toString()));
				}
			}
		}
		String responseBodyAsString = responseBody.toString();

		httpMethod.setRequestEntity(new StringRequestEntity(responseBodyAsString.substring(0, responseBody.length())));
		httpMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		return httpMethod;
	}

	

}
