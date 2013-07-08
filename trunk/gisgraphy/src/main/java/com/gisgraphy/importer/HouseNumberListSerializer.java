package com.gisgraphy.importer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gisgraphy.domain.geoloc.entity.HouseNumber;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.importer.dto.AssociatedStreetMember;
import com.vividsolutions.jts.geom.Point;

public class HouseNumberListSerializer {
	
	 /**
     * The logger
     */
    protected static final Logger logger = LoggerFactory
	    .getLogger(HouseNumberListSerializer.class);
	
	private static final String LAT_LON_SEPARATOR = ",";
	public static final String HOUSE_NUMBERS_SEPARATOR = "  ";
	public static final String HOUSENUMBER_AND_LOCATION_SEPARATOR = ":";
	Pattern clean_pattern = Pattern.compile(HOUSENUMBER_AND_LOCATION_SEPARATOR);
	private static final Pattern ASSOCIATED_HOUSE_NUMBER_REGEXP = Pattern.compile("(\\w+):([0-9\\.]+)\\,([0-9\\.]+)(?:\\s\\s)?");

	public String serialize(List<HouseNumber> houseNumberList){
		if (houseNumberList==null || houseNumberList.size()==0){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (HouseNumber houseNumber:houseNumberList){
			if (houseNumber.getNumber()==null || houseNumber.getLocation()==null){
				continue;
			}
			String cleaned = clean_pattern.matcher(houseNumber.getNumber()).replaceAll("");
			sb.append(cleaned)
			.append(HOUSENUMBER_AND_LOCATION_SEPARATOR)
			.append(String.format(Locale.US, "%s", houseNumber.getLatitude().doubleValue()))
			.append(LAT_LON_SEPARATOR)
			.append(String.format(Locale.US, "%s", houseNumber.getLongitude().doubleValue()))
			.append(HOUSE_NUMBERS_SEPARATOR);
		}
		return sb.toString().trim();
	}
	
	public List<HouseNumber> deserialize(String serializedString){
		List<HouseNumber> houseNumbers = new ArrayList<HouseNumber>();
		if (serializedString==null || "".equals(serializedString)){
			return houseNumbers;
		}
		Matcher matcher = ASSOCIATED_HOUSE_NUMBER_REGEXP.matcher(serializedString);
		int i = 0;
		while (matcher.find()) {
			HouseNumber number = new HouseNumber();
			if (matcher.groupCount() != 3) {
				logger.warn("wrong number of fields for housenumber");
				continue;
			}
			String name = matcher.group(1);
			if (name==null|| "".equals(name)){
				continue;
			}
			try {
				number.setName(name);
				String lat = matcher.group(2);
				if (lat==null|| "".equals(lat)){
					continue;
				}
				String lng = matcher.group(3);
				if (lng==null|| "".equals(lng)){
					continue;
				}
				Double latAsDouble = Double.parseDouble(lat);
				Double lngAsDouble = Double.parseDouble(lng);
				Point point = GeolocHelper.createPoint(lngAsDouble, latAsDouble);
				number.setLocation(point);
			} catch (Exception e) {
				logger.error("can not parse housenumbers for "+serializedString,e.getMessage());
			}
			houseNumbers.add(number);
			
		}
		return houseNumbers;
	}

}
