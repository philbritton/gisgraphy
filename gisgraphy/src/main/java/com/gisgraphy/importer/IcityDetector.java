package com.gisgraphy.importer;

import com.gisgraphy.domain.geoloc.entity.GisFeature;

public interface IcityDetector {

	public boolean isCountryHasMunicipality(String countryCode);

	public boolean isMunicipality(String countrycode, GisFeature gisFeature);

}