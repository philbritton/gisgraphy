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
package com.gisgraphy.domain.repository;

import org.springframework.stereotype.Repository;

import com.gisgraphy.domain.geoloc.entity.Shop;
import com.gisgraphy.domain.geoloc.entity.SwimmingPool;

/**
 * A data access object for {@link Shop} Object
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
@Repository
public class ShopDao extends GenericGisDao<Shop> implements
	IGisDao<Shop> {

    /**
     * Default constructor
     */
    public ShopDao() {
	super(Shop.class);
    }

}
