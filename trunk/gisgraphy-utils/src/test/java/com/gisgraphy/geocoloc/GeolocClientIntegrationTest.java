package com.gisgraphy.geocoloc;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.placetype.City;
import com.gisgraphy.domain.valueobject.GeolocResultsDto;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.geoloc.GeolocQuery;
import com.gisgraphy.rest.IRestClient;
import com.gisgraphy.rest.RestClient;
import com.gisgraphy.serializer.common.OutputFormat;
import com.gisgraphy.test.GisgraphyUtilsTestHelper;

public class GeolocClientIntegrationTest {

	private static final String BASE_URL = "http://services.gisgraphy.com/geoloc";
	
	
	@Test
	public void executeQuery(){
		GeolocClient client = new GeolocClient(BASE_URL);
		GeolocQuery query = createQuery();
		Assert.assertEquals(3, client.executeQuery(query).getResult().size());
	}

	private GeolocQuery createQuery() {
		Float lng = 2.349F;
		Float lat = 48.853F;
		GeolocQuery query = new GeolocQuery(GisgraphyUtilsTestHelper.createPoint(lng, lat));
		query.setApikey("apikey");
		query.withRadius(2000D);
		query.withCallback("callback");//test that callback will be set to null
		query.withOutput(Output.withFormat(OutputFormat.JSON).withIndentation());
		query.withPagination(Pagination.paginate().from(2).to(4));
		query.withPlaceType(City.class);
		query.withDistanceField(true);
		return query;
	}
	

}
