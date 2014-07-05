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
package com.gisgraphy.importer;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.repository.IGisFeatureDao;

/**
 *
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */

public class QuattroshapesSimpleImporterTest {

	@Test
	public void processData_GeonamesIdKO() {
		QuattroshapesSimpleImporter importer = new QuattroshapesSimpleImporter();
		String line ="wrongID\t ";
		importer.processData(line);
		
	}
	
	@Test
	public void processData_GeonamesIdok_featureFound_no_shape() {
		QuattroshapesSimpleImporter importer = new QuattroshapesSimpleImporter();
		IGisFeatureDao gisFeatureDao = EasyMock.createMock(IGisFeatureDao.class);
		String line ="5\t ";
		City city = new City();
		EasyMock.expect(gisFeatureDao.getByFeatureId(5L)).andReturn(city);
		EasyMock.replay(gisFeatureDao);
		importer.setGisFeatureDao(gisFeatureDao);
		importer.processData(line);
		
		Assert.assertNull(city.getShape());
		EasyMock.verify(gisFeatureDao);
	}
	
	@Test
	public void processData_GeonamesIdok_featureFound_shape() {
		QuattroshapesSimpleImporter importer = new QuattroshapesSimpleImporter();
		IGisFeatureDao gisFeatureDao = EasyMock.createMock(IGisFeatureDao.class);
		String line ="5\t010100000006C82291A0521E4054CC39B16BC64740";
		City city = new City();
		city.setFeatureId(1234L);
		EasyMock.expect(gisFeatureDao.getByFeatureId(5L)).andReturn(city);
		EasyMock.expect(gisFeatureDao.save(city)).andReturn(city);
		EasyMock.replay(gisFeatureDao);
		importer.setGisFeatureDao(gisFeatureDao);
		importer.processData(line);
		
		Assert.assertNotNull(city.getShape());
		EasyMock.verify(gisFeatureDao);
	}
	
	@Test
	public void processData_GeonamesIdok_NofeatureFound() {
		QuattroshapesSimpleImporter importer = new QuattroshapesSimpleImporter();
		IGisFeatureDao gisFeatureDao = EasyMock.createMock(IGisFeatureDao.class);
		String line ="5\t ";
		City city = new City();
		EasyMock.expect(gisFeatureDao.getByFeatureId(5L)).andReturn(null);
		EasyMock.replay(gisFeatureDao);
		importer.setGisFeatureDao(gisFeatureDao);
		importer.processData(line);
		
		Assert.assertNull(city.getShape());
		EasyMock.verify(gisFeatureDao);
	}
	
	@Test
	public void processData_GeonamesIdok_featureFound_wrong_shape() {
		QuattroshapesSimpleImporter importer = new QuattroshapesSimpleImporter();
		IGisFeatureDao gisFeatureDao = EasyMock.createMock(IGisFeatureDao.class);
		String line ="5\tfoo";
		City city = new City();
		EasyMock.expect(gisFeatureDao.getByFeatureId(5L)).andReturn(city);
		EasyMock.replay(gisFeatureDao);
		importer.setGisFeatureDao(gisFeatureDao);
		importer.processData(line);
		Assert.assertNull(city.getShape());
		EasyMock.verify(gisFeatureDao);
	}
	
	@Test
	public void shouldBeSkipped(){
		QuattroshapesSimpleImporter importer = new QuattroshapesSimpleImporter();
		ImporterConfig importerConfig = new ImporterConfig();
		importerConfig.setGeonamesImporterEnabled(true);
		importerConfig.setQuattroshapesImporterEnabled(true);
		importer.setImporterConfig(importerConfig);
		Assert.assertFalse(importer.shouldBeSkipped());
		
		importerConfig = new ImporterConfig();
		importerConfig.setGeonamesImporterEnabled(true);
		importerConfig.setQuattroshapesImporterEnabled(false);
		importer.setImporterConfig(importerConfig);
		Assert.assertTrue(importer.shouldBeSkipped());
		
		
		importerConfig = new ImporterConfig();
		importerConfig.setGeonamesImporterEnabled(false);
		importerConfig.setQuattroshapesImporterEnabled(true);
		importer.setImporterConfig(importerConfig);
		Assert.assertTrue(importer.shouldBeSkipped());
		
		importerConfig = new ImporterConfig();
		importerConfig.setGeonamesImporterEnabled(false);
		importerConfig.setQuattroshapesImporterEnabled(false);
		importer.setImporterConfig(importerConfig);
		Assert.assertTrue(importer.shouldBeSkipped());
	}

}
