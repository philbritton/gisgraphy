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

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gisgraphy.domain.Constants;

/**
 * Encoding helper
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class EncodingHelper {

    protected static final Logger logger = LoggerFactory.getLogger(EncodingHelper.class);

    /**
     * useful for windows only, this method is a workaround for encoding
     * problems on Windows.<br>
     * any suggestion are welcomed
     * 
     * @return the string in utf-8
     */
    public static String toUTF8(String string) {
	String utf8 = "";
	if (string == null){
	    return null;
	}
	try {
	    utf8 = new String(string.getBytes(Constants.CHARSET));
	} catch (UnsupportedEncodingException e1) {
	    throw new RuntimeException("can not change String Encoding");
	}
	return utf8;
    }

    /**
     * Set the file.encoding and sun.jnu.encoding to UTF-8
     */
    public static void setJVMEncodingToUTF8() {
	setSystemProperty("file.encoding", Constants.CHARSET);
	setSystemProperty("sun.jnu.encoding", Constants.CHARSET);
    }

    private static void setSystemProperty(String name, String value) {
	if (System.getProperty(name) == null || !System.getProperty(name).equals(value)) {
	    logger.info("change system property from " + System.getProperty(name) + " to " + value);
	    System.setProperty(name, value);

	    logger.info("System property" + name + " is now : " + System.getProperty(name));
	} else {
	    logger.info(name + "=" + System.getProperty("file.encoding"));
	}
    }

    /**
     * To replace accented characters in a String by unaccented equivalents.
     * source code from lucene ISOLatin1AccentFilter
     */
    public final static String removeAccents(String input) {
	final StringBuffer output = new StringBuffer();
	for (int i = 0; i < input.length(); i++) {
	    switch (input.charAt(i)) {
	    case '\u00C0': // À
	    case '\u00C1': // �?
	    case '\u00C2': // Â
	    case '\u00C3': // Ã
	    case '\u00C4': // Ä
	    case '\u00C5': // Å
		output.append("A");
		break;
	    case '\u00C6': // Æ
		output.append("AE");
		break;
	    case '\u00C7': // Ç
		output.append("C");
		break;
	    case '\u00C8': // È
	    case '\u00C9': // É
	    case '\u00CA': // Ê
	    case '\u00CB': // Ë
		output.append("E");
		break;
	    case '\u00CC': // Ì
	    case '\u00CD': // �?
	    case '\u00CE': // Î
	    case '\u00CF': // �?
		output.append("I");
		break;
	    case '\u00D0': // �?
		output.append("D");
		break;
	    case '\u00D1': // Ñ
		output.append("N");
		break;
	    case '\u00D2': // Ò
	    case '\u00D3': // Ó
	    case '\u00D4': // Ô
	    case '\u00D5': // Õ
	    case '\u00D6': // Ö
	    case '\u00D8': // Ø
		output.append("O");
		break;
	    case '\u0152': // Œ
		output.append("OE");
		break;
	    case '\u00DE': // Þ
		output.append("TH");
		break;
	    case '\u00D9': // Ù
	    case '\u00DA': // Ú
	    case '\u00DB': // Û
	    case '\u00DC': // Ü
		output.append("U");
		break;
	    case '\u00DD': // �?
	    case '\u0178': // Ÿ
		output.append("Y");
		break;
	    case '\u00E0': // à
	    case '\u00E1': // á
	    case '\u00E2': // â
	    case '\u00E3': // ã
	    case '\u00E4': // ä
	    case '\u00E5': // å
		output.append("a");
		break;
	    case '\u00E6': // æ
		output.append("ae");
		break;
	    case '\u00E7': // ç
		output.append("c");
		break;
	    case '\u00E8': // è
	    case '\u00E9': // é
	    case '\u00EA': // ê
	    case '\u00EB': // ë
		output.append("e");
		break;
	    case '\u00EC': // ì
	    case '\u00ED': // í
	    case '\u00EE': // î
	    case '\u00EF': // ï
		output.append("i");
		break;
	    case '\u00F0': // ð
		output.append("d");
		break;
	    case '\u00F1': // ñ
		output.append("n");
		break;
	    case '\u00F2': // ò
	    case '\u00F3': // ó
	    case '\u00F4': // ô
	    case '\u00F5': // õ
	    case '\u00F6': // ö
	    case '\u00F8': // ø
		output.append("o");
		break;
	    case '\u0153': // œ
		output.append("oe");
		break;
	    case '\u00DF': // ß
		output.append("ss");
		break;
	    case '\u00FE': // þ
		output.append("th");
		break;
	    case '\u00F9': // ù
	    case '\u00FA': // ú
	    case '\u00FB': // û
	    case '\u00FC': // ü
		output.append("u");
		break;
	    case '\u00FD': // ý
	    case '\u00FF': // ÿ
		output.append("y");
		break;
	    default:
		output.append(input.charAt(i));
		break;
	    }
	}
	return output.toString();
    }

}
