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

import static com.gisgraphy.importer.MunicipalityDetector.MunicipalityDetectionStrategy.POPULATION_AND_ADM1CODE;
import static com.gisgraphy.importer.MunicipalityDetector.MunicipalityDetectionStrategy.POPULATION_AND_ADM2CODE;
import static com.gisgraphy.importer.MunicipalityDetector.MunicipalityDetectionStrategy.POPULATION_AND_ADM3CODE;
import static com.gisgraphy.importer.MunicipalityDetector.MunicipalityDetectionStrategy.POPULATION_AND_ADM4CODE;
import static com.gisgraphy.importer.MunicipalityDetector.MunicipalityDetectionStrategy.POPULATION_AND_ADM5CODE;
import static com.gisgraphy.importer.MunicipalityDetector.MunicipalityDetectionStrategy.POPULATION_OR_ADM1CODE;
import static com.gisgraphy.importer.MunicipalityDetector.MunicipalityDetectionStrategy.POPULATION_OR_ADM2CODE;
import static com.gisgraphy.importer.MunicipalityDetector.MunicipalityDetectionStrategy.POPULATION_OR_ADM3CODE;
import static com.gisgraphy.importer.MunicipalityDetector.MunicipalityDetectionStrategy.POPULATION_OR_ADM4CODE;
import static com.gisgraphy.importer.MunicipalityDetector.MunicipalityDetectionStrategy.POPULATION_OR_ADM5CODE;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gisgraphy.domain.geoloc.entity.GisFeature;

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
 *this code is for the moment replace by the osm cities importer , a municipality is a city that is present in both geonames and OSM, this seems to be more reliable, but we
 *keep this code for some country specificities based on user feedbacks
 */
@Service
public class MunicipalityDetector implements IMunicipalityDetector {
	
	public enum MunicipalityDetectionStrategy{
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
	public final static Map<String, MunicipalityDetectionStrategy> countrycodeToCityDetectionStrategy = new HashMap<String, MunicipalityDetector.MunicipalityDetectionStrategy>(){
		{
			put("IT",MunicipalityDetectionStrategy.POPULATION);
			put("DE",MunicipalityDetectionStrategy.ADM2CODE);
			put("FR",MunicipalityDetectionStrategy.POPULATION_OR_ADM4CODE);
			put("US",MunicipalityDetectionStrategy.POPULATION);
			put("AR",MunicipalityDetectionStrategy.POPULATION);
			put("BE",MunicipalityDetectionStrategy.POPULATION);
			put("VN",MunicipalityDetectionStrategy.POPULATION_OR_ADM1CODE);
			put("UK",MunicipalityDetectionStrategy.POPULATION);
			put("TW",MunicipalityDetectionStrategy.POPULATION_OR_ADM2CODE);//maybe we should take all
			put("RO",MunicipalityDetectionStrategy.POPULATION);
			put("AU",MunicipalityDetectionStrategy.POPULATION);
			/*put("",cityDetectionStrategy.POPULATION);
			put("",cityDetectionStrategy.POPULATION);
			put("",cityDetectionStrategy.POPULATION);
			put("",cityDetectionStrategy.POPULATION);
			put("",cityDetectionStrategy.POPULATION);
			put("",cityDetectionStrategy.POPULATION);*/
		}
	};

	
	/* (non-Javadoc)
	 * @see com.gisgraphy.importer.IMunicipalityDetector#isCountryHasMunicipality(java.lang.String)
	 */
	public boolean isCountryHasMunicipality(String countryCode){
		if (countryCode!=null && countrycodeToCityDetectionStrategy.get(countryCode.toUpperCase())!=null){
			return true;
		} return false;
	}
	
	
	/* (non-Javadoc)
	 * @see com.gisgraphy.importer.IMunicipalityDetector#isMunicipality(java.lang.String, com.gisgraphy.domain.geoloc.entity.GisFeature)
	 */
	public boolean isMunicipality(String countrycode,GisFeature gisFeature ){
		if (countrycode==null || "".equals(countrycode) || gisFeature ==null){
			return false;
		}
		MunicipalityDetectionStrategy strategy = countrycodeToCityDetectionStrategy.get(countrycode.toUpperCase());
		if (strategy == null){
			return false;
		}
		else {
			return isMunicipality_internal(strategy, gisFeature.getPopulation(), gisFeature.getAdm1Code(), gisFeature.getAdm2Code(), gisFeature.getAdm3Code(), gisFeature.getAdm4Code(), gisFeature.getAdm5Code());
		}
	}

	protected boolean isMunicipality_internal(MunicipalityDetectionStrategy strategy, Integer population, String adm1code, String adm2code, String adm3code, String adm4code, String adm5code) {
		if (population != null && population!=0){//population is not null
			if (strategy==MunicipalityDetectionStrategy.POPULATION || strategy ==POPULATION_OR_ADM1CODE ||  strategy == POPULATION_OR_ADM2CODE || strategy == POPULATION_OR_ADM3CODE || strategy == POPULATION_OR_ADM4CODE || strategy == POPULATION_OR_ADM5CODE){
				return true;
			} else if ((strategy== POPULATION_AND_ADM1CODE || strategy== MunicipalityDetectionStrategy.ADM1CODE ) && !isNullOrEmpty(adm1code)){
				return true;
			}
			else if ((strategy== POPULATION_AND_ADM2CODE || strategy== MunicipalityDetectionStrategy.ADM2CODE ) && !isNullOrEmpty(adm2code)){
				return true;
			}
			else if ((strategy== POPULATION_AND_ADM3CODE || strategy== MunicipalityDetectionStrategy.ADM3CODE ) && !isNullOrEmpty(adm3code)){
				return true;
			}
			else if ((strategy== POPULATION_AND_ADM4CODE || strategy== MunicipalityDetectionStrategy.ADM4CODE ) && !isNullOrEmpty(adm4code)){
				return true;
			}
			else if ((strategy== POPULATION_AND_ADM5CODE || strategy== MunicipalityDetectionStrategy.ADM5CODE ) && !isNullOrEmpty(adm5code)){
				return true;
			}
		} else {//population is null 
			if (strategy==POPULATION_AND_ADM1CODE || strategy ==POPULATION_AND_ADM2CODE || strategy== POPULATION_AND_ADM3CODE ||strategy==POPULATION_AND_ADM4CODE || strategy ==POPULATION_AND_ADM5CODE){
				return false;
			} else if ((strategy== POPULATION_OR_ADM1CODE || strategy== MunicipalityDetectionStrategy.ADM1CODE )&& !isNullOrEmpty(adm1code)){
				return true;
			}
			else if ((strategy== POPULATION_OR_ADM2CODE || strategy== MunicipalityDetectionStrategy.ADM2CODE ) && !isNullOrEmpty(adm2code)){
				return true;
			}
			else if ((strategy== POPULATION_OR_ADM3CODE || strategy== MunicipalityDetectionStrategy.ADM3CODE ) && !isNullOrEmpty(adm3code)){
				return true;
			}
			else if ((strategy== POPULATION_OR_ADM4CODE || strategy== MunicipalityDetectionStrategy.ADM4CODE )&& !isNullOrEmpty(adm4code)){
				return true;
			}
			else if ((strategy== POPULATION_OR_ADM5CODE|| strategy== MunicipalityDetectionStrategy.ADM5CODE ) && !isNullOrEmpty(adm5code)){
				return true;
			}
		}
		return false;
	}
	
	private boolean isNullOrEmpty(String str){
		return str==null || "".equals(str);
	}
	
}
