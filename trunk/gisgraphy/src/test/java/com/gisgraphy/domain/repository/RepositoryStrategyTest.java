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
package com.gisgraphy.domain.repository;

import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.valueobject.FeatureCode;


public class RepositoryStrategyTest extends AbstractTransactionalTestCase {

    @Resource
    IRepositoryStrategy repositoryStrategy;

    @Resource
    IGisFeatureDao gisFeatureDao;

    @Resource
    ICityDao cityDao;

    /**
     * Test method for
     * {@link com.gisgraphy.domain.repository.RepositoryStrategy#initMap()}.
     */
    @Test
    public void testThatAllPlaceTypeHaveTheirOwnDao() {
	Collection<Class<? extends GisFeature>> placetypes = FeatureCode.entityClass
		.values();
	Collection<IGisDao<? extends GisFeature>> daos = repositoryStrategy
		.getAvailablesDaos();
	for (Class<? extends GisFeature> placeType : placetypes) {
	    assertEquals("placeType " + placeType + " have no dao", placeType,
		    repositoryStrategy.getDao(placeType).getPersistenceClass());
	}
	for (IGisDao<? extends GisFeature> dao : daos) {
	    assertEquals("dao " + dao.getClass().getSimpleName()
		    + " have no placetype", dao.getPersistenceClass(),
		    FeatureCode.entityClass.get(dao.getPersistenceClass()
			    .getSimpleName().toLowerCase()));
	}

	assertEquals(placetypes.size(), daos.size());

    }

    /**
     * Test method for
     * {@link com.gisgraphy.domain.repository.RepositoryStrategy#GetDao(java.lang.Class)}.
     */
    @Test
    public void testGetDaoWithNullReturnGisFeatureDao() {
	assertEquals(gisFeatureDao, repositoryStrategy.getDao(null));
    }

    /**
     * Test method for
     * {@link com.gisgraphy.domain.repository.RepositoryStrategy#GetDao(java.lang.Class)}.
     */
    @Test
    public void testGetDaoreturnCorrectDao() {
	assertEquals(gisFeatureDao, repositoryStrategy.getDao(GisFeature.class));
	assertEquals(cityDao, repositoryStrategy.getDao(City.class));
    }

}
