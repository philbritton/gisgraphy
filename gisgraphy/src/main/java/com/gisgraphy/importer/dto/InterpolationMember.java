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

import com.vividsolutions.jts.geom.Point;


/**
 * Represents a house number with an associated street in the Karlsruhe schema.
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class InterpolationMember {
	/**
	 * id of the node
	 */
	private String id;
	/**
	 * the ordered sequence of nodes
	 */
	private int sequenceId;
	/**
	 * the gis location of the member (middle point or location )
	 */
	private Point location;

	/**
	 * the number of the house. It is a string because of latin that is can have bis ter or a letter (3c)
	 */
	private String houseNumber;
	
	/**
	 * the name of the street 
	 */
	private String streetname;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the sequenceId
	 */
	public int getSequenceId() {
		return sequenceId;
	}

	/**
	 * @param sequenceId the sequenceId to set
	 */
	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}

	/**
	 * @return the location
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(Point location) {
		this.location = location;
	}

	/**
	 * @return the streetname
	 */
	public String getStreetname() {
		return streetname;
	}

	/**
	 * @param streetname the streetname to set
	 */
	public void setStreetname(String streetname) {
		this.streetname = streetname;
	}
	
	

}
