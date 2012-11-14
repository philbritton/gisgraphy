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

import java.util.HashMap;
import java.util.Map;

/**
 * @author david masclet
 * geonames classify features with some code. populated place. there is no real definition of City. city are populated place
 * without any distinction. this class try to detect if the place is a city. to do this we search for population administrative division code (one or the other or both
 * and apply a strategy depends on country. for each country we have search for the number of city and search what is the best fields to look to see if it is a city.
 * e.g : for France there is about 36000 city. in geonames there is about 58000 place. if we look at the adm4 code and the population (both) we identify 36875 city.
 * <br/>
 * it is not deterministic but probabilistic approach.if you got better idea to identify city, please let us know. This can seems tricky but this works well and improve geocoding 
 * when we fill the is_in fields,we have a city and not a quater)
 *
 */
public class CityDetector {
	
	public enum cityDetectionStrategy{
		//0
		NO_STRATEGY,
		//1
		POPULATION,
		//2
		ADM1CODE,
		ADM2CODE,
		ADM3CODE,
		ADM4CODE,
		ADM5CODE,
		//3
		POPULATION_OR_ADM1CODE,
		POPULATION_OR_ADM2CODE,
		POPULATION_OR_ADM3CODE,
		POPULATION_OR_ADM4CODE,
		POPULATION_OR_ADM5CODE,
		//4
		POPULATION_AND_ADM1CODE,
		POPULATION_AND_ADM2CODE,
		POPULATION_AND_ADM3CODE,
		POPULATION_AND_ADM4CODE,
		POPULATION_AND_ADM5CODE,
		
	}
	public final static Map<String, cityDetectionStrategy> countrycodeToCityDetectionStrategy = new HashMap<String, CityDetector.cityDetectionStrategy>(){
		{
			put("IT",cityDetectionStrategy.POPULATION);
			put("DE",cityDetectionStrategy.ADM2CODE);
			put("FR",cityDetectionStrategy.POPULATION_OR_ADM4CODE);
			put("US",cityDetectionStrategy.POPULATION);
			put("AR",cityDetectionStrategy.POPULATION);
			put("BE",cityDetectionStrategy.POPULATION);
			put("VN",cityDetectionStrategy.POPULATION_OR_ADM1CODE);
			put("UK",cityDetectionStrategy.POPULATION);
			put("TW",cityDetectionStrategy.POPULATION_OR_ADM2CODE);//maybe we should take all
			put("RO",cityDetectionStrategy.POPULATION);
			put("AU",cityDetectionStrategy.POPULATION);
			/*put("",cityDetectionStrategy.POPULATION);
			put("",cityDetectionStrategy.POPULATION);
			put("",cityDetectionStrategy.POPULATION);
			put("",cityDetectionStrategy.POPULATION);
			put("",cityDetectionStrategy.POPULATION);
			put("",cityDetectionStrategy.POPULATION);*/
			
		}
		
	};

}
