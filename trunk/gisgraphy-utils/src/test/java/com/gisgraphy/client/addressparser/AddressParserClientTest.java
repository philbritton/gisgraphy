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
package com.gisgraphy.client.addressparser;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.addressparser.AddressParserClient;
import com.gisgraphy.addressparser.AddressParserException;
import com.gisgraphy.addressparser.AddressQuery;
import com.gisgraphy.addressparser.AddressResultsDto;
import com.gisgraphy.rest.IRestClient;
import com.gisgraphy.serializer.common.OutputFormat;


public class AddressParserClientTest {
    
    @Test
    public void getBaseUrlShouldReturnTheConstructor(){
	String baseURL="http://www.url.com/addressparser";
	AddressParserClient service = new AddressParserClient(baseURL);
	Assert.assertEquals(baseURL, service.getBaseURL());
    }
    
    @Test
    public void defaultConstructor(){
	AddressParserClient service = new AddressParserClient();
	Assert.assertEquals(AddressParserClient.DEFAULT_ADDRESS_PARSER_BASE_URL, service.getBaseURL());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void ConstructorWithNullURL(){
	 new AddressParserClient(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void constructorWithMalformedURL(){
	 new AddressParserClient("malformedURL");
    }
    
    @Test
    public void execute(){
	 final String url = "URL";
	final IRestClient mockRestClient = EasyMock.createMock(IRestClient.class);
	EasyMock.expect(mockRestClient.get(url, AddressResultsDto.class, OutputFormat.JSON)).andReturn(null);
	EasyMock.replay(mockRestClient);
	AddressParserClient service = new AddressParserClient(){
	    @Override
	    protected IRestClient getRestClient() {
	        return mockRestClient;
	    }
	    
	    @Override
	    protected String getUrl(AddressQuery query) {
	    return url;
	    }
	};
	AddressQuery query =new AddressQuery("rawAddress", "US");
	service.execute(query);
	EasyMock.verify(mockRestClient);
    }
    
    @Test
    public void executeAndSerialize(){
	 final String url = "URL";
	 final OutputStream outputStream = new ByteArrayOutputStream();
	final IRestClient mockRestClient = EasyMock.createMock(IRestClient.class);
	mockRestClient.get(url, outputStream,OutputFormat.JSON);
	EasyMock.replay(mockRestClient);
	AddressParserClient service = new AddressParserClient(){
	    @Override
	    protected IRestClient getRestClient() {
	        return mockRestClient;
	    }
	    
	    @Override
	    protected String getUrl(AddressQuery query) {
	    return url;
	    }
	};
	AddressQuery query =new AddressQuery("rawAddress", "US");
	service.executeAndSerialize(query,outputStream);
	EasyMock.verify(mockRestClient);
    }
    
    @Test
    public void executeToString(){
	 final String url = "URL";
	final IRestClient mockRestClient = EasyMock.createMock(IRestClient.class);
	mockRestClient.get((String)EasyMock.anyObject(), (OutputStream)EasyMock.anyObject(),(OutputFormat)EasyMock.anyObject());
	EasyMock.replay(mockRestClient);
	AddressParserClient service = new AddressParserClient(){
	    @Override
	    protected IRestClient getRestClient() {
	        return mockRestClient;
	    }
	    
	    @Override
	    protected String getUrl(AddressQuery query) {
	    return url;
	    }
	};
	AddressQuery query =new AddressQuery("rawAddress", "US");
	service.executeToString(query);
	EasyMock.verify(mockRestClient);
    }
    
    
    
    
    @Test(expected=AddressParserException.class)
    public void executeShouldThrowsAddressParserException(){
	 final String url = "URL";
	final IRestClient mockRestClient = EasyMock.createMock(IRestClient.class);
	EasyMock.expect(mockRestClient.get(url, AddressResultsDto.class, OutputFormat.JSON)).andStubThrow(new RuntimeException());
	EasyMock.replay(mockRestClient);
	AddressParserClient service = new AddressParserClient(){
	    @Override
	    protected IRestClient getRestClient() {
	        return mockRestClient;
	    }
	    
	    @Override
	    protected String getUrl(AddressQuery query) {
	    return url;
	    }
	};
	AddressQuery query =new AddressQuery("rawAddress", "US");
	service.execute(query);
	EasyMock.verify(mockRestClient);
    }
    
    @Test(expected=AddressParserException.class)
    public void executeAndSerializeShouldThrowsAddressParserException(){
	 final String url = "URL";
	 final OutputStream outputStream = new ByteArrayOutputStream();
	final IRestClient mockRestClient = EasyMock.createMock(IRestClient.class);
	mockRestClient.get(url, outputStream,OutputFormat.JSON);
	EasyMock.expectLastCall().andStubThrow(new RuntimeException());
	EasyMock.replay(mockRestClient);
	AddressParserClient service = new AddressParserClient(){
	    @Override
	    protected IRestClient getRestClient() {
	        return mockRestClient;
	    }
	    
	    @Override
	    protected String getUrl(AddressQuery query) {
	    return url;
	    }
	};
	AddressQuery query =new AddressQuery("rawAddress", "US");
	service.executeAndSerialize(query,outputStream);
	EasyMock.verify(mockRestClient);
    }
    
    @Test(expected=AddressParserException.class)
    public void executeToStringShouldThrowsAddressParserException(){
	 final String url = "URL";
	final IRestClient mockRestClient = EasyMock.createMock(IRestClient.class);
	mockRestClient.get((String)EasyMock.anyObject(), (OutputStream)EasyMock.anyObject(),(OutputFormat)EasyMock.anyObject());
	EasyMock.expectLastCall().andStubThrow(new RuntimeException());
	EasyMock.replay(mockRestClient);
	AddressParserClient service = new AddressParserClient(){
	    @Override
	    protected IRestClient getRestClient() {
	        return mockRestClient;
	    }
	    
	    @Override
	    protected String getUrl(AddressQuery query) {
	    return url;
	    }
	};
	AddressQuery query =new AddressQuery("rawAddress", "US");
	service.executeToString(query);
	EasyMock.verify(mockRestClient);
    }
    
    
    @Test(expected=AddressParserException.class)
    public void executeWithNullQuery(){
	AddressParserClient service = new AddressParserClient();
	service.execute(null);
    }
    
    @Test(expected=AddressParserException.class)
    public void executeAndSerializeWithNullQuery(){
	AddressParserClient service = new AddressParserClient();
	service.executeAndSerialize(null,new ByteArrayOutputStream());
    }
    
    @Test(expected=AddressParserException.class)
    public void executeAndSerializeWithNullOutputStream(){
	AddressParserClient service = new AddressParserClient();
	service.executeAndSerialize(new AddressQuery("rawAddress", "us"),null);
    }
    
    @Test(expected=AddressParserException.class)
    public void executeToStringWithNullQuery(){
	AddressParserClient service = new AddressParserClient();
	service.executeToString(null);
    }
    
}
