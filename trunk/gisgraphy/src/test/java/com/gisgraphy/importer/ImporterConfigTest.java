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

import static com.gisgraphy.importer.ImporterConfig.ACCEPT_ALL_REGEX_OPTION;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gisgraphy.test.GisgraphyTestHelper;

public class ImporterConfigTest {

	public static String accessiblePath;
	public static String pathNotAccessible;
	public static String filePath;
	public static File tempDir;

	@BeforeClass
	public static void setUpClass() throws IOException {
		accessiblePath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + ImporterConfig.class.getSimpleName() + "-" + System.currentTimeMillis();
		tempDir = new File(accessiblePath);
		tempDir.mkdir();

		pathNotAccessible = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + ImporterConfig.class.getSimpleName() + "-" + new Random().nextLong();

		filePath = tempDir.getAbsolutePath() + System.getProperty("file.separator") + "test.txt";
		File file = new File(filePath);
		file.createNewFile();
	}

	@AfterClass
	public static void TearDownClass() throws IOException {
		// delete temp dir
		assertTrue("the tempDir has not been deleted", GisgraphyTestHelper.DeleteNonEmptyDirectory(tempDir));
	}

	@Test
	public void isGeonamesDownloadDirectoryAccessible() {
		ImporterConfig importerConfig = new ImporterConfig();
		importerConfig.setGeonamesDir(accessiblePath);
		assertTrue(importerConfig.isGeonamesDownloadDirectoryAccessible());

		importerConfig.setGeonamesDir(pathNotAccessible);
		assertFalse(importerConfig.isGeonamesDownloadDirectoryAccessible());
		importerConfig.setGeonamesDir(filePath);
		assertFalse(importerConfig.isGeonamesDownloadDirectoryAccessible());
	}

	@Test
	public void isOpenStreetMapDownloadDirectoryAccessible() {
		ImporterConfig importerConfig = new ImporterConfig();
		importerConfig.setOpenStreetMapDir(accessiblePath);
		assertTrue(importerConfig.isOpenStreetMapDownloadDirectoryAccessible());

		importerConfig.setOpenStreetMapDir(pathNotAccessible);
		assertFalse(importerConfig.isOpenStreetMapDownloadDirectoryAccessible());
		importerConfig.setOpenStreetMapDir(filePath);
		assertFalse(importerConfig.isOpenStreetMapDownloadDirectoryAccessible());
	}
	
	@Test
	public void isOpenStreetMapHouseNumberDownloadDirectoryAccessible() {
		ImporterConfig importerConfig = new ImporterConfig();
		importerConfig.setOpenStreetMapHouseNumberDir(accessiblePath);
		assertTrue(importerConfig.isOpenStreetMapHouseNumberDownloadDirectoryAccessible());

		importerConfig.setOpenStreetMapHouseNumberDir(pathNotAccessible);
		assertFalse(importerConfig.isOpenStreetMapHouseNumberDownloadDirectoryAccessible());
		importerConfig.setOpenStreetMapHouseNumberDir(filePath);
		assertFalse(importerConfig.isOpenStreetMapHouseNumberDownloadDirectoryAccessible());
	}
	
	@Test
	public void isOpenStreetMapCitiesDirectoryAccessible() {
		ImporterConfig importerConfig = new ImporterConfig();
		importerConfig.setOpenStreetMapCitiesDir(accessiblePath);
		assertTrue(importerConfig.isOpenStreetMapcitiesDirectoryAccessible());

		importerConfig.setOpenStreetMapCitiesDir(pathNotAccessible);
		assertFalse(importerConfig.isOpenStreetMapcitiesDirectoryAccessible());
		importerConfig.setOpenStreetMapCitiesDir(filePath);
		assertFalse(importerConfig.isOpenStreetMapcitiesDirectoryAccessible());
	}

	@Test
	public void setOpenStreetMapDirShouldAddFileSeparatorIfItDoesnTEndsWithFileSeparator() {
		String OpenStreetMapDir = "Test";
		ImporterConfig importerConfig = new ImporterConfig();
		importerConfig.setOpenStreetMapDir(OpenStreetMapDir);
		Assert.assertTrue("setOpenStreetMapDir should add File separator", importerConfig.getOpenStreetMapDir().endsWith(File.separator));
		Assert.assertEquals(OpenStreetMapDir + File.separator, importerConfig.getOpenStreetMapDir());
	}
	
	@Test
	public void setOpenStreetMapHouseNumberDirShouldAddFileSeparatorIfItDoesnTEndsWithFileSeparator() {
		String OpenStreetMapHouseNumberDir = "Test";
		ImporterConfig importerConfig = new ImporterConfig();
		importerConfig.setOpenStreetMapHouseNumberDir(OpenStreetMapHouseNumberDir);
		Assert.assertTrue("setOpenStreetMapHouseNumberDir should add File separator", importerConfig.getOpenStreetMapHouseNumberDir().endsWith(File.separator));
		Assert.assertEquals(OpenStreetMapHouseNumberDir + File.separator, importerConfig.getOpenStreetMapHouseNumberDir());
	}
	
	@Test
	public void setOpenStreetMapCitiesDirShouldAddFileSeparatorIfItDoesnTEndsWithFileSeparator() {
		String OpenStreetMapCitiesDir = "Test";
		ImporterConfig importerConfig = new ImporterConfig();
		importerConfig.setOpenStreetMapCitiesDir(OpenStreetMapCitiesDir);
		Assert.assertTrue("setOpenStreetMapCitiesDir should add File separator", importerConfig.getOpenStreetMapCitiesDir().endsWith(File.separator));
		Assert.assertEquals(OpenStreetMapCitiesDir + File.separator, importerConfig.getOpenStreetMapCitiesDir());
	}

	@Test
	public void setGeonamesDirShouldAddFileSeparatorIfItDoesnTEndsWithFileSeparator() {
		String geonamesDir = "Test";
		ImporterConfig importerConfig = new ImporterConfig();
		importerConfig.setGeonamesDir(geonamesDir);
		Assert.assertTrue("setGeonamesDir should add File separator", importerConfig.getGeonamesDir().endsWith(File.separator));
		Assert.assertEquals(geonamesDir + File.separator, importerConfig.getGeonamesDir());
	}

	@Test
	public void isGeonamesImporterShouldBeTrueByDefault() {
		ImporterConfig importerConfig = new ImporterConfig();
		Assert.assertTrue("Geonames importer should be enabled by default ", importerConfig.isGeonamesImporterEnabled());
	}

	@Test
	public void isOpenstreetmapImporterShouldBeTrueByDefault() {
		ImporterConfig importerConfig = new ImporterConfig();
		Assert.assertTrue("OpenStreetMap importer should be enabled by default ", importerConfig.isOpenstreetmapImporterEnabled());
	}
	
	@Test
	public void isConfigCorrectForImport(){
		ImporterConfig importerConfig = new ImporterConfig();
		//set Correct values
		importerConfig.setGeonamesDir(accessiblePath);
		importerConfig.setOpenStreetMapDir(accessiblePath);
		importerConfig.setAcceptRegExString(ImporterConfig.BASE_ACCEPT_REGEX);
		//test with bad geonamesDir
		importerConfig.setGeonamesDir(pathNotAccessible);
		Assert.assertFalse("when geonames dir is not ok the function should return false", importerConfig.isConfigCorrectForImport());
		importerConfig.setGeonamesDir(accessiblePath);
		//test with bad Openstreetmap dir
		importerConfig.setOpenStreetMapDir(pathNotAccessible);
		Assert.assertFalse("when openstreetmap dir is not ok the function should return false", importerConfig.isConfigCorrectForImport());
		importerConfig.setOpenStreetMapDir(accessiblePath);
		//test with bad Openstreetmap house number dir
		importerConfig.setOpenStreetMapHouseNumberDir(pathNotAccessible);
		Assert.assertFalse("when openstreetmap house number dir is not ok the function should return false", importerConfig.isConfigCorrectForImport());
		importerConfig.setOpenStreetMapHouseNumberDir(accessiblePath);
		//test with bad regexp
		importerConfig.setAcceptRegExString("k[;l");
		Assert.assertFalse("when regexp string is not ok the function should return false", importerConfig.isConfigCorrectForImport());
		importerConfig.setAcceptRegExString(ImporterConfig.BASE_ACCEPT_REGEX);
		//test when everything is correct
		assertTrue("when all the condition are ok the function should return true", importerConfig.isConfigCorrectForImport());
		
	}
	
	@Test
	public void testAcceptRegexpString(){
	    ImporterConfig importerConfig = new ImporterConfig();
	    importerConfig.setAcceptRegExString(" ");
	    Assert.assertEquals(ImporterConfig.ACCEPT_ALL_REGEX_OPTION, importerConfig.getAcceptRegExString());
	    importerConfig.setAcceptRegExString("UNK.UNK");
	    Assert.assertEquals("base regexp should be added",ImporterConfig.BASE_ACCEPT_REGEX+"UNK.UNK", importerConfig.getAcceptRegExString());
	    importerConfig.setAcceptRegExString(null);
	    Assert.assertEquals(ImporterConfig.ACCEPT_ALL_REGEX_OPTION, importerConfig.getAcceptRegExString());
	    
	    importerConfig.setAcceptRegExString(ACCEPT_ALL_REGEX_OPTION);
	    Assert.assertEquals("When option is "+ACCEPT_ALL_REGEX_OPTION+" no base regexp should be added",ACCEPT_ALL_REGEX_OPTION, importerConfig.getAcceptRegExString());
	}
	
	@Test
	public void testSetOpenStreetMapFilesToDownload(){
	    ImporterConfig importerConfig = new ImporterConfig();
	    importerConfig.setOpenStreetMapFilesToDownload(" ");
	    Assert.assertEquals(ImporterConfig.OPENSTREETMAP_DEFAULT_FILES_TO_DOWNLOAD, importerConfig.getOpenStreetMapFilesToDownload());
	    importerConfig.setOpenStreetMapFilesToDownload("FR.bz2");
	    Assert.assertEquals("FR.bz2", importerConfig.getOpenStreetMapFilesToDownload());
	    importerConfig.setOpenStreetMapFilesToDownload(null);
	    Assert.assertEquals(ImporterConfig.OPENSTREETMAP_DEFAULT_FILES_TO_DOWNLOAD, importerConfig.getOpenStreetMapFilesToDownload());
	}
	
	@Test
	public void testSetOpenStreetMapHouseNumberFilesToDownload(){
	    ImporterConfig importerConfig = new ImporterConfig();
	    importerConfig.setOpenStreetMapHouseNumberFilesToDownload(" ");
	    Assert.assertEquals(ImporterConfig.OPENSTREETMAP_HOUSENUMBER_DEFAULT_FILES_TO_DOWNLOAD, importerConfig.getOpenStreetMapHouseNumberFilesToDownload());
	    importerConfig.setOpenStreetMapHouseNumberFilesToDownload("FR.bz2");
	    Assert.assertEquals("FR.bz2", importerConfig.getOpenStreetMapHouseNumberFilesToDownload());
	    importerConfig.setOpenStreetMapHouseNumberFilesToDownload(null);
	    Assert.assertEquals(ImporterConfig.OPENSTREETMAP_HOUSENUMBER_DEFAULT_FILES_TO_DOWNLOAD, importerConfig.getOpenStreetMapHouseNumberFilesToDownload());
	}
	
	@Test
	public void testSetGeonamesFileToDownload(){
	    ImporterConfig importerConfig = new ImporterConfig();
	    importerConfig.setGeonamesFilesToDownload(" ");
	    Assert.assertEquals(ImporterConfig.GEONAMES_DEFAULT_FILES_TO_DOWNLOAD, importerConfig.getGeonamesFilesToDownload());
	    importerConfig.setGeonamesFilesToDownload("FR.zip");
	    Assert.assertEquals("FR.zip", importerConfig.getGeonamesFilesToDownload());
	    importerConfig.setGeonamesFilesToDownload(null);
	    Assert.assertEquals(ImporterConfig.GEONAMES_DEFAULT_FILES_TO_DOWNLOAD, importerConfig.getGeonamesFilesToDownload());
	}
	
	 @Test
	    public void testCreateImporterMetadataDirIfItDoesnTExistShouldCreateTheGeonamesDirIfItDoesnTExist(){
	    	ImporterConfig fakeImporterConfig = new ImporterConfig();
	    	String geonameDirPathThatDoesnTExist = System.getProperty("java.io.tmpdir")+File.separator+Math.abs(new Random().nextInt());
	    	fakeImporterConfig.setGeonamesDir(geonameDirPathThatDoesnTExist);
	    	fakeImporterConfig.createImporterMetadataDirIfItDoesnTExist();
	    	assertTrue("if the geonames directory doen't exists it should be created when the getAlreadyDoneFilePath method is called", new File(geonameDirPathThatDoesnTExist).exists());
	    }
	 
    @Test
    public void testSetOpenstreetmapFillIsInShouldPersistThePropertiesFile() throws URISyntaxException, FileNotFoundException, IOException {
	ImporterConfig importerConfig = new ImporterConfig();

	boolean openStreetMapFillIsInInitial = importerConfig.isOpenStreetMapFillIsIn();
	importerConfig.setOpenStreetMapFillIsIn(!openStreetMapFillIsInInitial);
	URL resourceUrl = this.getClass().getResource(ImporterConfig.PROPERTIES_CONFIG_FILE_CLASSPATH);
	File file = new File(resourceUrl.toURI());
	Properties properties = new Properties();
	properties.load(new FileInputStream(file));
	String actual = properties.getProperty(ImporterConfig.OPENSTREETMAP_FILL_ISIN_FIELD_NAME);
	Assert.assertEquals("the property has not been persist",String.valueOf(!openStreetMapFillIsInInitial), actual);
	Assert.assertEquals("the property has not been set",!openStreetMapFillIsInInitial, importerConfig.isOpenStreetMapFillIsIn());
	//restore
	importerConfig.setOpenStreetMapFillIsIn(openStreetMapFillIsInInitial);
	Assert.assertEquals("the property has not been persist",openStreetMapFillIsInInitial, importerConfig.isOpenStreetMapFillIsIn());
	Assert.assertEquals("the property has not been set",openStreetMapFillIsInInitial, importerConfig.isOpenStreetMapFillIsIn());
    }
    
 
	    

}
