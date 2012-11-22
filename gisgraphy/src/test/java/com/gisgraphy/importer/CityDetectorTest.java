package com.gisgraphy.importer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.importer.CityDetector.cityDetectionStrategy;

public class CityDetectorTest {

	@Test
	public void testIsMunicipality() {
		IcityDetector d = new CityDetector();
		GisFeature gisFeature = new GisFeature();
		gisFeature.setAdm1Code("a");
		gisFeature.setAdm2Code("a");
		gisFeature.setAdm3Code("a");
		gisFeature.setAdm4Code("a");
		gisFeature.setAdm5Code("a");
		gisFeature.setPopulation(1);
		assertFalse(d.isMunicipality("",gisFeature));
		assertFalse(d.isMunicipality(null, gisFeature));
		assertFalse(d.isMunicipality("NOTEXISTING",gisFeature));
		assertFalse(d.isMunicipality("IT", null));

		assertTrue(d.isMunicipality("it",  gisFeature));
		assertTrue(d.isMunicipality("IT", gisFeature));
		
	}
	
	@Test
	public void testIsMunicipality_internal() {
		CityDetector d = new CityDetector();
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.POPULATION, 1, "", "", "", "", ""));
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.POPULATION, 0, "", "", "", "", ""));
		
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.ADM1CODE, 0, "a", "", "", "", ""));
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.ADM1CODE, 1, "", "", "", "", ""));
		
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.ADM2CODE, 0, "", "a", "", "", ""));
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.ADM2CODE, 0, "", "", "", "", ""));
		
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.ADM3CODE, 0, "", "", "a", "", ""));
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.ADM3CODE, 0, "", "", "", "", ""));

		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.ADM4CODE, 0, "", "", "", "a", ""));
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.ADM4CODE, 0, "", "", "", "", ""));
		
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.ADM5CODE, 0, "", "", "", "", "a"));
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.ADM5CODE, 0, "", "", "", "", ""));
		
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_OR_ADM1CODE, 0, "a", "", "", "", ""));
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_OR_ADM1CODE, 1, "a", "", "", "", ""));
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_OR_ADM1CODE, 1, "", "", "", "", ""));
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_OR_ADM1CODE, 0, "", "", "", "", ""));
		
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_OR_ADM2CODE, 0, "", "a", "", "", ""));
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_OR_ADM2CODE, 1, "", "a", "", "", ""));
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_OR_ADM2CODE, 1, "", "", "", "", ""));
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_OR_ADM2CODE, 0, "", "", "", "", ""));
	
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_OR_ADM3CODE, 0, "a", "", "a", "", ""));
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_OR_ADM3CODE, 1, "a", "", "a", "", ""));
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_OR_ADM3CODE, 1, "", "", "", "", ""));
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_OR_ADM3CODE, 0, "", "", "", "", ""));
		
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_OR_ADM4CODE, 0, "a", "", "", "a", ""));
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_OR_ADM4CODE, 1, "a", "", "", "a", ""));
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_OR_ADM4CODE, 1, "", "", "", "", ""));
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_OR_ADM4CODE, 0, "", "", "", "", ""));

		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_OR_ADM5CODE, 0, "a", "", "", "", "a"));
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_OR_ADM5CODE, 1, "a", "", "", "", "a"));
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_OR_ADM5CODE, 1, "", "", "", "", ""));
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_OR_ADM5CODE, 0, "", "", "", "", ""));
		
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_AND_ADM1CODE, 0, "a", "", "", "", ""));
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_AND_ADM1CODE, 1, "", "", "", "", ""));
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_AND_ADM1CODE, 0, "", "", "", "", ""));
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_AND_ADM1CODE, 1, "a", "", "", "", ""));

		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_AND_ADM2CODE, 0, "", "a", "", "", ""));
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_AND_ADM2CODE, 1, "", "", "", "", ""));
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_AND_ADM2CODE, 0, "", "", "", "", ""));
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_AND_ADM2CODE, 1, "", "a", "", "", ""));
		
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_AND_ADM3CODE, 0, "", "", "a", "", ""));
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_AND_ADM3CODE, 1, "", "", "", "", ""));
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_AND_ADM3CODE, 0, "", "", "", "", ""));
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_AND_ADM3CODE, 1, "", "", "a", "", ""));
		
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_AND_ADM4CODE, 0, "", "", "", "a", ""));
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_AND_ADM4CODE, 1, "", "", "", "", ""));
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_AND_ADM4CODE, 0, "", "", "", "", ""));
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_AND_ADM4CODE, 1, "", "", "", "a", ""));
		
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_AND_ADM5CODE, 0, "", "", "", "", "a"));
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_AND_ADM5CODE, 1, "", "", "", "", ""));
		assertFalse(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_AND_ADM5CODE, 0, "", "", "", "", ""));
		assertTrue(d.isMunicipality_internal(cityDetectionStrategy.POPULATION_AND_ADM5CODE, 1, "", "", "", "", "a"));
		
	}

}
