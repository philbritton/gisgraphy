package com.gisgraphy.street;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 
 *  * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 * 
 */
public class HouseNumberUtil {
	
	private static Pattern pattern = Pattern.compile("[\\-\\–\\一]?(\\d+)[^\\d]*",Pattern.CASE_INSENSITIVE);
	public static String normalizeNumber(String numberAsString){
		if (numberAsString ==null){
			return null;
		}
		Matcher matcher = pattern.matcher(numberAsString);
		if (matcher.find()){
			return matcher.group(1);
		}
		return null;
	}

}
