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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class Testregexpression {

    public static String accpetRegexp = ImporterConfig.DEFAULT_ACCEPT_REGEX_CITY;

    private static List<Pattern> acceptedPatterns;

    @Test
    public void defaulAcceptRegexpForCity() {
	compileRegex();
	assertEquals(
		"UNK[.].+;.[.]UNK;A[.]ADM.?;A[.]PCL.?;P[.]PPL[A-Z&&[^QW]];P[.]PPL$;P[.]STLMT$",
		ImporterConfig.DEFAULT_ACCEPT_REGEX_CITY);

	// must be false
	assertFalse(isFeatureClassCodeAccepted("PmPPLX"));
	assertFalse(isFeatureClassCodeAccepted("PmPPL"));
	assertFalse(isFeatureClassCodeAccepted("P.PPLQ"));
	assertFalse(isFeatureClassCodeAccepted("P.PPLW"));
	assertFalse(isFeatureClassCodeAccepted("L.PRK"));
	assertFalse(isFeatureClassCodeAccepted("PpSTLMT"));
	assertFalse(isFeatureClassCodeAccepted("V.FRS"));
	// must be true
	assertTrue(isFeatureClassCodeAccepted("P.PPL"));
	assertTrue(isFeatureClassCodeAccepted("P.PPLD"));
	assertTrue(isFeatureClassCodeAccepted("UNK.PRK"));
	assertTrue(isFeatureClassCodeAccepted("UNK.PPL"));
	assertTrue(isFeatureClassCodeAccepted("p.UNK"));
	assertTrue(isFeatureClassCodeAccepted("UNK.UNK"));
	assertTrue(isFeatureClassCodeAccepted("A.ADM1"));

	assertTrue(isFeatureClassCodeAccepted("A.ADM"));
	assertTrue(isFeatureClassCodeAccepted("A.PCL"));
	assertTrue(isFeatureClassCodeAccepted("A.PCLI"));

    }

    // this code is duplicated but it is important to keep the method static the
    // goal is to test the default regexp, not the function
    private static boolean isFeatureClassCodeAccepted(String classCode) {
	Matcher matcher = null;
	for (Pattern pattern : acceptedPatterns) {
	    matcher = pattern.matcher(classCode);
	    if (matcher.matches()) {
		return true;
	    }
	}
	return false;
    }

    private static void compileRegex() {
	List<Pattern> patternsList = new ArrayList<Pattern>();
	String[] acceptRegexpString = accpetRegexp.trim().split(";");
	for (String pattern : acceptRegexpString) {
	    if (pattern != null && !pattern.trim().equals("")) {
		patternsList.add(Pattern.compile(pattern));
	    }
	}
	acceptedPatterns = patternsList;
    }

}
