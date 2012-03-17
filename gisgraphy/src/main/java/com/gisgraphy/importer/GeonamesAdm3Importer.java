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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.gisgraphy.domain.valueobject.NameValueDTO;

/**
 * Import the Adm of level 3 file. It is the first step of the adm3 import
 * process, the import will be complete when all the datastore object will be
 * updated by the {@link GeonamesFeatureSimpleImporter}
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class GeonamesAdm3Importer extends GeonamesFeatureSimpleImporter {


    
	@Override
	protected boolean isAdmMode() {
		return true;
	}

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#getFiles()
     */
    @Override
    protected File[] getFiles() {
	File[] files = new File[1];
	files[0] = new File(importerConfig.getGeonamesDir()
		+ importerConfig.getAdm3FileName());
	return files;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#getMaxInsertsBeforeFlush()
     */
    @Override
    protected int getMaxInsertsBeforeFlush() {
	// we commit each times because we don't want duplicate adm and the
	// cache is NONSTRICT_READ_WRITE (assynchronous)
	return 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IGeonamesProcessor#rollback()
     */
    public List<NameValueDTO<Integer>> rollback() {
	List<NameValueDTO<Integer>> deletedObjectInfo = new ArrayList<NameValueDTO<Integer>>();
	logger.info("deleting adm3...");
	int deletedadm = admDao.deleteAllByLevel(3);
	if (deletedadm != 0) {
	    deletedObjectInfo
		    .add(new NameValueDTO<Integer>("ADM3", deletedadm));
	}
	logger.info(deletedadm + " adm3s have been deleted");
	resetStatus();
	return deletedObjectInfo;
    }

}
