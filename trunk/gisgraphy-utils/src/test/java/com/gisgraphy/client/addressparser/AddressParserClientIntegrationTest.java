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

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.addressparser.Address;
import com.gisgraphy.addressparser.AddressParserClient;
import com.gisgraphy.addressparser.AddressQuery;
import com.gisgraphy.addressparser.AddressResultsDto;
import com.gisgraphy.helper.BeanHelper;


public class AddressParserClientIntegrationTest {
    
  
    @Test
    public void integrationTest(){
    	AddressParserClient addressParserClient =new AddressParserClient();
    	AddressQuery addressQuery = new AddressQuery("Wacholderweg 52a 26133 Oldenburg","de");
    	addressQuery.setCallback("callback");//test that callback will be set to null
    	AddressResultsDto results = addressParserClient.execute(addressQuery);
    	Assert.assertNotNull(results);
    	Assert.assertEquals(1, results.getResult().size());
    	Address address= results.getResult().get(0);
    	Assert.assertEquals("Wacholderweg", address.getStreetName() );
    	Assert.assertEquals("52a", address.getHouseNumber() );
    	Assert.assertEquals("26133", address.getZipCode() );
    	Assert.assertEquals("Oldenburg", address.getCity() );
    	System.out.println(BeanHelper.toString(address));
    }

}
