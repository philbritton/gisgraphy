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
package com.gisgraphy.fulltext;

import java.util.List;

import com.gisgraphy.service.IQueryProcessor;
import com.gisgraphy.service.ServiceException;

/**
 * Execute a {@linkplain FulltextQuery} and returns the results in a specific
 * format
 * 
 * @see FulltextQuery
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public interface IFullTextSearchEngine extends IQueryProcessor<FulltextQuery> {

    /**
     * Execute the query and returns the Java Objects.
     * The results are nor sorted by relevance
     * 
     * @param query
     *                The FulltextQuery to execute
     * @return The proxied hibernate Java Objects. Never return null but an
     *         empty list
     * @throws ServiceException
     *                 If an error occurred
     */
    public List<?> executeQueryToDatabaseObjects(
	    FulltextQuery query) throws ServiceException;

    /**
     * Execute the query and returns a list of DTO
     * 
     * @param query
     *                The FulltextQuery to execute
     * @return a DTO for the results. note : The list of results will never be
     *         null but an empty list
     * @throws ServiceException
     *                 If an error occurred
     */
    public FulltextResultsDto executeQuery(FulltextQuery query)
	    throws ServiceException;

    /**
     * @return true if the fulltextsearchengine is alive, otherwise return
     *         false;
     */
    public boolean isAlive();

    /**
     * @return the url of the fulltextsearch engine
     */
    public String getURL();

}
