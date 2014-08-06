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
package com.gisgraphy.compound;

import static com.gisgraphy.compound.Trie.CONDENSE;
import static com.gisgraphy.compound.Trie.trie;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author david Masclet
 * 
 * decompounder are often based on wordlist and doesn't return unknow words (as lucene one).
 * It is not very useful when you got street names (e.g : fooStrasse).<br/><br>
 * This decompounder aim is to split a word based on words list but keep the unknow words: 
 * e.g : if words are {weg,wald} then foowegwald will return [foowegwald foo weg wald].
 * lucene one would have returned [weg wald].
 * 
 */
public class Decompounder {
	private Pattern p;

	public Decompounder(List<String> words) {
		if (words==null){
			throw new RuntimeException("words list is mandatory for a decompounder");
		}
		String re = trie(words, CONDENSE);
		p = Pattern
				.compile(re, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	}

	public String[] decompound(String str) {
		//Simple but probably not optimized
		Matcher m = p.matcher(str);
		StringBuffer s = new StringBuffer(str).append(" ");
		boolean found=false;
		while (m.find()) {
			found=true;
			m.appendReplacement(s, " " + m.group(0) + " ");
		}
		m.appendTail(s);
		if(found){
		return s.toString().replaceAll("\\s+", " ").trim().split(" ");
		} else {
			return new String[]{str};
		}
	}

}
