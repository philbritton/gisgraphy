package com.gisgraphy.street;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.serializer.common.OutputFormat;
import com.gisgraphy.test.GisgraphyUtilsTestHelper;

public class StreetSearchClientIntegrationTest {

	private static final String BASE_URL = "	http://services.gisgraphy.com/street/streetsearch";


	@Test
	public void executeQuery() {
		StreetSearchClient client = new StreetSearchClient(BASE_URL);
		StreetSearchQuery query = createQuery();
		Assert.assertNotNull(client.executeQuery(query));
	}


	private StreetSearchQuery createQuery() {
		Float lng = 2F;
		Float lat = 3F;
		StreetSearchQuery query = new StreetSearchQuery(GisgraphyUtilsTestHelper.createPoint(lng, lat));
		query.setApikey("apikey");
		query.withRadius(20D);
		query.withOutput(Output.withFormat(OutputFormat.JSON).withIndentation());
		query.withPagination(Pagination.paginate().from(2).to(4));
		query.withCallback("callback");//test that callback will be set to null
		query.withDistanceField(true);
		query.withStreetType(StreetType.BRIDLEWAY);
		query.withOneWay(true);
		query.withName("name");
		return query;
	}


}
