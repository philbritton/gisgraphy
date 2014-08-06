/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.gisgraphy.compound.solr;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenFilterFactory;
import org.apache.solr.common.ResourceLoader;
import org.apache.solr.common.SolrException;
import org.apache.solr.util.plugin.ResourceLoaderAware;

import com.gisgraphy.compound.Decompounder;

public class DecompoundTokenFilterFactory extends BaseTokenFilterFactory implements ResourceLoaderAware {

    private Decompounder decompounder;
    private String dictFile;
    List<String> wlist;
    
    public void init(Map<String, String> args) {
    	dictFile = args.get("dictionary");
        if (null == dictFile) {
          throw new SolrException( SolrException.ErrorCode.SERVER_ERROR, 
                                   "Missing required parameter: dictionary");
        }
        }

    @Override
    public TokenStream create(TokenStream tokenStream) {
        return new DecompoundTokenFilter(tokenStream, decompounder);
    }
    
    public void inform(ResourceLoader loader) {
        try {
          wlist = loader.getLines(dictFile);
          this.decompounder = createDecompounder();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }

    private Decompounder createDecompounder() {
            return new Decompounder(wlist);
    }
}
