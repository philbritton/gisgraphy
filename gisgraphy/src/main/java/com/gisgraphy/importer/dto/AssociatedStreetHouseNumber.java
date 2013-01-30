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

package com.gisgraphy.importer.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a house number with an associated street in the Karlsruhe schema.
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class AssociatedStreetHouseNumber {

	private String relationID;
	private List<AssociatedStreetMember> associatedStreetMember = new ArrayList<AssociatedStreetMember>();
	
	/**
	 * @return the associatedStreetMember
	 */
	public List<AssociatedStreetMember> getAssociatedStreetMember() {
		return associatedStreetMember;
	}
	/**
	 * @param associatedStreetMember the associatedStreetMember to set
	 */
	public void setAssociatedStreetMember(List<AssociatedStreetMember> associatedStreetMember) {
		this.associatedStreetMember = associatedStreetMember;
	}
	/**
	 * @return the relationID
	 */
	public String getRelationID() {
		return relationID;
	}
	/**
	 * @param relationID the relationID to set
	 */
	public void setRelationID(String relationID) {
		this.relationID = relationID;
	}
	
	public void addMember(AssociatedStreetMember member){
		associatedStreetMember.add(member);
	}
	
	public List<AssociatedStreetMember> getStreetMembers(){
		List<AssociatedStreetMember> streets = new ArrayList<AssociatedStreetMember>();
		for (AssociatedStreetMember member:associatedStreetMember){
			if (member.isStreet()){
				streets.add(member);
			}
		}
		return streets;
	}
	
	
	
	public List<AssociatedStreetMember> getHouseMembers(){
		List<AssociatedStreetMember> house = new ArrayList<AssociatedStreetMember>();
		for (AssociatedStreetMember member:associatedStreetMember){
			if (member.isHouse()){
				house.add(member);
			}
		}
		return house;
	}
}
