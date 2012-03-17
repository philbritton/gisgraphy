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
package com.gisgraphy.geocoding;

import java.io.OutputStream;

import com.gisgraphy.addressparser.Address;
import com.gisgraphy.addressparser.AddressQuery;
import com.gisgraphy.addressparser.AddressResultsDto;

/**
 * 
 * Geocode internationnal address via gisgraphy services
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 *
 */
public interface IGeocodingService {
	
	/**
	 * 
	 * parsed and geocode a raw address
	 * @param query a query that contains an address as string
	 * @param countryCode the countryCode (two letters) of the address
	 * @return A list of geocoded address in an {@link AddressResultsDto} or null if the address can not be parsed
	 * @throws GeocodingException when error occurs
	 */
	public AddressResultsDto geocode(AddressQuery query) throws GeocodingException;
	
	/**
	 * 
	 * parsed and geocode a raw address and serialize in several format (according {@link AddressQuery#getFormat()}
	 * @param query a query that contains an address as string
	 * @param countryCode the countryCode (two letters) of the address
	 * @throws GeocodingException when error occurs
	 */
	public void geocodeAndSerialize(AddressQuery query,OutputStream outputStream) throws GeocodingException;
	
	/**
	 * 
	 * parsed and geocode a raw address and serialize in several format (according {@link AddressQuery#getFormat()}
	 * @param query a query that contains an address as string
	 * @return A string that represents a list of geocoded address in an {@link AddressResultsDto} or null if the address can not be parsed
	 * @throws GeocodingException when error occurs
	 */
	public String geocodeToString(AddressQuery query) throws GeocodingException;
	
	/**
	 * @param address the address to geocode
	 * @param countryCode the countryCode (two letters) of the address
	 * @return  A list of geocoded address in an {@link AddressResultsDto} with the lat and long field
         * @throws GeocodingException when error occurs
	 */
	public AddressResultsDto geocode(Address address, String countryCode) throws GeocodingException;
	

}
