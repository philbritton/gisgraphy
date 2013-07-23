package com.gisgraphy.addressparser;

/**
 * Represent an address where each fields is explicitely defined.
 * @see AddressQuery;
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 * 
 */
public class StructuredAddressQuery extends AddressQuery {

	private Address structuredAddress;
	public StructuredAddressQuery(Address address, String country) {
		if (address == null) {
		    throw new IllegalArgumentException("structured address can not be nul");
		}
		if (country == null || country.trim().equals("")) {
		    throw new IllegalArgumentException("country can not be nul or empty");
		}
		this.structuredAddress=address;
		setCountry(country);
	}
	
	@Override
	public void setAddress(String address) {
		throw new RuntimeException("could not call setAddress on Structured AddressQuery, use AddressQuery object or call setStructuredAddress()");
	}
	
	@Override
	public String getAddress() {
		throw new RuntimeException("could not call getAddress on Structured AddressQuery, use AddressQuery object or call getStructuredAddress()");
	}
	
	public Address getStructuredAddress() {
		return structuredAddress;
	}
	public void setStructuredAddress(Address structuredAddress) {
		this.structuredAddress = structuredAddress;
	}

}
