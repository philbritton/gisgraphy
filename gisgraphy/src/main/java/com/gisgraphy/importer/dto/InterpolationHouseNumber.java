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

package com.gisgraphy.importer.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a house number as an interpolation in the Karlsruhe schema.
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class InterpolationHouseNumber {

	/**
	 * the id ow the way
	 */
	private String wayID;
	
	/**
	 * the nodes of the way
	 */
	private List<InterpolationMember> members = new ArrayList<InterpolationMember>();

	
	/**
	 * The type of interpolation (even,odd,...)
	 */
	private String interpolationType;
	
	/**
	 * @return the wayID
	 */
	public String getWayID() {
		return wayID;
	}

	/**
	 * @param wayID the wayID to set
	 */
	public void setWayID(String wayID) {
		this.wayID = wayID;
	}

	/**
	 * @return the members
	 */
	public List<InterpolationMember> getMembers() {
		return members;
	}

	/**
	 * @param members the members to set
	 */
	public void setMembers(List<InterpolationMember> members) {
		this.members = members;
	}
	/**
	 * Add a member
	 * @param member
	 */
	public void addMember(InterpolationMember member){
		members.add(member);
	}

	/**
	 * @return the interpolationType
	 */
	public String getInterpolationType() {
		return interpolationType;
	}

	/**
	 * @param interpolationType the interpolationType to set
	 */
	public void setInterpolationType(String interpolationType) {
		this.interpolationType = interpolationType;
	}
}
