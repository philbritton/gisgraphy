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
package com.gisgraphy.helper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class HTMLHelperTest  {

    @Test
    public void isParametesrEmptyShouldReturnValidResult() {
	String parameterName1 = "param1";
	String parameterName2 = "param2";
	MockHttpServletRequest req = new MockHttpServletRequest();
	assertTrue(HTMLHelper.isParametersEmpty(req, parameterName1));
	assertTrue(HTMLHelper.isParametersEmpty(req, parameterName1,parameterName2));
	
	//one param empty string
	req.setParameter(parameterName1, "");
	assertTrue(HTMLHelper.isParametersEmpty(req, parameterName1));
	assertTrue(HTMLHelper.isParametersEmpty(req, parameterName1,parameterName2));
	req.removeAllParameters();
	
	//one empty, one not
	req.setParameter(parameterName1, "value1");
	assertFalse(HTMLHelper.isParametersEmpty(req, parameterName1));
	assertTrue(HTMLHelper.isParametersEmpty(req, parameterName1,parameterName2));
	req.removeAllParameters();
	
	//all not empty
	req.setParameter(parameterName1, "value1");
	req.setParameter(parameterName2, "value2");
	assertFalse(HTMLHelper.isParametersEmpty(req, parameterName1));
	assertFalse(HTMLHelper.isParametersEmpty(req, parameterName1,parameterName2));
	
	
	
	
	
    }

}
