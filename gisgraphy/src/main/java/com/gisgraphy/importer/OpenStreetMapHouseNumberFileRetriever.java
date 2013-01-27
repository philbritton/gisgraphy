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
package com.gisgraphy.importer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.gisgraphy.helper.Untar;


/**
 * Retrieve The Geonames files from a server
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class OpenStreetMapHouseNumberFileRetriever extends AbstractFileRetriever {

    private Untar untar;
    
    public String getcurrentExtractedFileNameIntoArchive(){
	return untar == null? null : untar.getCurrentFileNameIntoArchiveExtracted();
    }
   
    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractFileRetriever#getDownloadDirectory()
     */
    public String getDownloadDirectory() {
	return importerConfig.getOpenStreetMapHouseNumberDir();
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractFileRetriever#getDownloadBaseUrl()
     */
    public String getDownloadBaseUrl() {
	return importerConfig
	    .getOpenstreetMaphouseNumberDownloadURL();
    }
    
    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractFileRetriever#decompressFiles()
     */
    public void decompressFiles() throws IOException {
	File[] filesToUntar = getFilesToDecompress();
	File destDirectory = new File(getDownloadDirectory());
	for (int i = 0; i < filesToUntar.length; i++) {
	    try {
			untar = new Untar(filesToUntar[i].getAbsolutePath(),destDirectory);
			untar.untar();
		} catch (Exception e) {
			logger.error(filesToUntar[i].getAbsolutePath()+" is not a valid tar file");
		}
	}

	// for log purpose
	File[] filesToImport = ImporterHelper
		.listCountryFilesToImport(getDownloadDirectory());

	for (int i = 0; i < filesToImport.length; i++) {
	    logger.info("the files " + filesToImport[i].getName()
		    + " will be imported for openstreetMap house number");
	}
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractFileRetriever#shouldBeSkipped()
     */
    @Override
    public boolean shouldBeSkipped() {
	return !importerConfig.isOpenstreetmapHouseNumberImporterEnabled();
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractFileRetriever#getFilesToDownload()
     */
    @Override
    List<String> getFilesToDownload() {
	return importerConfig.getOpenStreetMapHouseNumberDownloadFilesListFromOption();
    }

    @Override
    public File[] getFilesToDecompress() throws IOException {
	return ImporterHelper.listTarFiles(getDownloadDirectory());
    }

    @Override
    public boolean isFileNotFoundTolerant() {
	return false;
    }

}
