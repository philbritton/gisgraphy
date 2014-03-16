package com.gisgraphy.fulltext;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.placetype.City;
import com.gisgraphy.domain.placetype.Country;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.serializer.common.OutputFormat;
import com.gisgraphy.service.ServiceException;
import com.gisgraphy.test.GisgraphyUtilsTestHelper;

public class FulltextClientIntegrationTest {

	private static final String BASE_URL = "http://localhost:8080/fulltext/";

	@Test
	public void executeQueryCountry() {
		FulltextQuery query = new FulltextQuery("france");
		Output output = Output.withFormat(OutputFormat.JSON).withStyle(OutputStyle.FULL);
		query.withOutput(output);
		FulltextClient client = new FulltextClient(BASE_URL);
		FulltextResultsDto dto = client.executeQuery(query);
		Assert.assertNotNull(dto.getMaxScore());
		Assert.assertNotNull(dto.getNumFound());
		Assert.assertTrue(dto.getQTime() > 0);
		Assert.assertNotNull(dto.getResults());
		Assert.assertTrue(dto.getResultsSize() > 0);
		SolrResponseDto doc = dto.getResults().get(0);
		Assert.assertNotNull(doc.getFeature_id());
		Assert.assertNotNull(doc.getFeature_class());
		Assert.assertNotNull(doc.getFeature_code());
		Assert.assertNotNull(doc.getName());
		Assert.assertNotNull(doc.getName_ascii());
		// Assert.assertNotNull(doc.getElevation());
		Assert.assertNotNull(doc.getGtopo30());
		Assert.assertNotNull(doc.getTimezone());
		Assert.assertNotNull(doc.getPlacetype());
		Assert.assertNotNull(doc.getPopulation());
		Assert.assertNotNull(doc.getLat());
		Assert.assertNotNull(doc.getLng());
		// Assert.assertNotNull(doc.getAdm1_code());
		// Assert.assertNotNull(doc.getAdm2_code());
		// Assert.assertNotNull(doc.getAdm3_code());
		// Assert.assertNotNull(doc.getAdm4_code());
		// Assert.assertNotNull(doc.getAdm1_name());
		// Assert.assertNotNull(doc.getAdm2_name());
		// Assert.assertNotNull(doc.getAdm3_name());
		// Assert.assertNotNull(doc.getAdm4_name());
		Assert.assertNotNull(doc.getCountry_code());
		Assert.assertNotNull(doc.getCountry_name());
		Assert.assertNotNull(doc.getCountry_flag_url());
		Assert.assertNotNull(doc.getGoogle_map_url());
		Assert.assertNotNull(doc.getYahoo_map_url());
		//Assert.assertNotNull(doc.getOpenstreetmap_map_url());
		// Assert.assertNotNull(doc.getContinent());
		Assert.assertNotNull(doc.getCurrency_code());
		Assert.assertNotNull(doc.getCurrency_name());
		Assert.assertNotNull(doc.getFips_code());
		Assert.assertNotNull(doc.getIsoalpha2_country_code());
		Assert.assertNotNull(doc.getIsoalpha3_country_code());
		Assert.assertNotNull(doc.getPostal_code_mask());
		Assert.assertNotNull(doc.getPostal_code_regex());
		Assert.assertNotNull(doc.getPhone_prefix());
		Assert.assertNotNull(doc.getTld());
		Assert.assertNotNull(doc.getCapital_name());
		// Assert.assertNotNull(doc.getLevel());
		// Assert.assertNotNull(doc.getLength());
		// Assert.assertNotNull(doc.getStreet_type());
		// Assert.assertNotNull(doc.getOpenstreetmap_id());
		// Assert.assertNotNull(doc.getIs_in());

	}

	@Test
	public void executeQuery() {
		FulltextQuery query = new FulltextQuery("paris");
		Output output = Output.withFormat(OutputFormat.JSON).withStyle(OutputStyle.FULL);
		query.withOutput(output);
		FulltextClient client = new FulltextClient(BASE_URL);
		FulltextResultsDto dto = client.executeQuery(query);
		Assert.assertNotNull(dto.getMaxScore());
		Assert.assertNotNull(dto.getNumFound());
		Assert.assertTrue(dto.getQTime() > 0);
		Assert.assertNotNull(dto.getResults());
		Assert.assertTrue(dto.getResultsSize() > 0);
		SolrResponseDto doc = dto.getResults().get(0);
		Assert.assertNotNull(doc.getFeature_id());
		Assert.assertNotNull(doc.getFeature_class());
		Assert.assertNotNull(doc.getFeature_code());
		Assert.assertNotNull(doc.getName());
		Assert.assertNotNull(doc.getName_ascii());
		// Assert.assertNotNull(doc.getElevation());
		Assert.assertNotNull(doc.getGtopo30());
		Assert.assertNotNull(doc.getTimezone());
		Assert.assertNotNull(doc.getPlacetype());
		Assert.assertNotNull(doc.getPopulation());
		Assert.assertNotNull(doc.getLat());
		Assert.assertNotNull(doc.getLng());
		Assert.assertNotNull(doc.getAdm1_code());
		Assert.assertNotNull(doc.getAdm2_code());
		Assert.assertNotNull(doc.getAdm3_code());
		Assert.assertNotNull(doc.getAdm4_code());
		Assert.assertNotNull(doc.getAdm1_name());
		Assert.assertNotNull(doc.getAdm2_name());
		Assert.assertNotNull(doc.getAdm3_name());
		Assert.assertNotNull(doc.getAdm4_name());
		Assert.assertNotNull(doc.getCountry_code());
		Assert.assertNotNull(doc.getCountry_name());
		Assert.assertNotNull(doc.getCountry_flag_url());
		Assert.assertNotNull(doc.getGoogle_map_url());
		Assert.assertNotNull(doc.getYahoo_map_url());
		// Assert.assertNotNull(doc.getContinent());
		// Assert.assertNotNull(doc.getCurrency_code());
		// Assert.assertNotNull(doc.getCurrency_name());
		// Assert.assertNotNull(doc.getFips_code());
		// Assert.assertNotNull(doc.getIsoalpha2_country_code());
		// Assert.assertNotNull(doc.getIsoalpha3_country_code());
		// Assert.assertNotNull(doc.getPostal_code_mask());
		// Assert.assertNotNull(doc.getPostal_code_regex());
		// Assert.assertNotNull(doc.getPhone_prefix());
		// Assert.assertNotNull(doc.getTld());
		// Assert.assertNotNull(doc.getCapital_name());
		// Assert.assertNotNull(doc.getLevel());
		// Assert.assertNotNull(doc.getLength());
		// Assert.assertNotNull(doc.getStreet_type());
		// Assert.assertNotNull(doc.getOpenstreetmap_id());
		// Assert.assertNotNull(doc.getIs_in());
		// alternate names
		Assert.assertNotNull(doc.getName_alternates());
		Assert.assertTrue(doc.getName_alternates().size() > 0);
		Assert.assertNotNull(doc.getAdm1_names_alternate());
		Assert.assertTrue(doc.getAdm1_names_alternate().size() > 0);
		Assert.assertNotNull(doc.getAdm2_names_alternate());
		Assert.assertTrue(doc.getAdm2_names_alternate().size() > 0);
		Assert.assertNotNull(doc.getCountry_names_alternate());
		Assert.assertTrue(doc.getCountry_names_alternate().size() > 0);
		// alternatenames multilang
		Assert.assertNotNull(doc.getName_alternates_localized());
		Assert.assertTrue(doc.getName_alternates_localized().size() > 0);
		Assert.assertNotNull(doc.getAdm1_names_alternate_localized());
		Assert.assertTrue(doc.getAdm1_names_alternate_localized().size() > 0);
		Assert.assertNotNull(doc.getAdm2_names_alternate_localized());
		Assert.assertTrue(doc.getAdm2_names_alternate_localized().size() > 0);
		Assert.assertNotNull(doc.getCountry_names_alternate_localized());
		Assert.assertTrue(doc.getCountry_names_alternate_localized().size() > 0);

	}

	@Test
	public void executeQueryInLongMode() {
		FulltextQuery query = new FulltextQuery("paris");
		Output output = Output.withFormat(OutputFormat.JSON).withStyle(OutputStyle.LONG);
		query.withOutput(output);
		FulltextClient client = new FulltextClient(BASE_URL);
		FulltextResultsDto dto = client.executeQuery(query);
		Assert.assertNotNull(dto.getMaxScore());
		Assert.assertNotNull(dto.getNumFound());
		Assert.assertTrue(dto.getQTime() > 0);
		Assert.assertNotNull(dto.getResults());
		Assert.assertTrue(dto.getResultsSize() > 0);
		SolrResponseDto doc = dto.getResults().get(0);
		Assert.assertNotNull(doc.getFeature_id());
		Assert.assertNotNull(doc.getFeature_class());
		Assert.assertNotNull(doc.getFeature_code());
		Assert.assertNotNull(doc.getName());
		Assert.assertNotNull(doc.getName_ascii());
		// Assert.assertNotNull(doc.getElevation());
		Assert.assertNotNull(doc.getGtopo30());
		Assert.assertNotNull(doc.getTimezone());
		Assert.assertNotNull(doc.getPlacetype());
		Assert.assertNotNull(doc.getPopulation());
		Assert.assertNotNull(doc.getLat());
		Assert.assertNotNull(doc.getLng());
		Assert.assertNotNull(doc.getAdm1_code());
		Assert.assertNotNull(doc.getAdm2_code());
		Assert.assertNotNull(doc.getAdm3_code());
		Assert.assertNotNull(doc.getAdm4_code());
		Assert.assertNotNull(doc.getAdm1_name());
		Assert.assertNotNull(doc.getAdm2_name());
		Assert.assertNotNull(doc.getAdm3_name());
		Assert.assertNotNull(doc.getAdm4_name());
		Assert.assertNotNull(doc.getCountry_code());
		Assert.assertNotNull(doc.getCountry_name());
		Assert.assertNotNull(doc.getCountry_flag_url());
		Assert.assertNotNull(doc.getGoogle_map_url());
		Assert.assertNotNull(doc.getYahoo_map_url());
		//Assert.assertNotNull(doc.getOpenstreetmap_map_url());
		// Assert.assertNotNull(doc.getContinent());
		// Assert.assertNotNull(doc.getCurrency_code());
		// Assert.assertNotNull(doc.getCurrency_name());
		// Assert.assertNotNull(doc.getFips_code());
		// Assert.assertNotNull(doc.getIsoalpha2_country_code());
		// Assert.assertNotNull(doc.getIsoalpha3_country_code());
		// Assert.assertNotNull(doc.getPostal_code_mask());
		// Assert.assertNotNull(doc.getPostal_code_regex());
		// Assert.assertNotNull(doc.getPhone_prefix());
		// Assert.assertNotNull(doc.getTld());
		// Assert.assertNotNull(doc.getCapital_name());
		// Assert.assertNotNull(doc.getLevel());
		// Assert.assertNotNull(doc.getLength());
		// Assert.assertNotNull(doc.getStreet_type());
		// Assert.assertNotNull(doc.getOpenstreetmap_id());
		// Assert.assertNotNull(doc.getIs_in());
		// alternate names
		Assert.assertNotNull(doc.getName_alternates());
		Assert.assertTrue(doc.getName_alternates().size() == 0);
		Assert.assertNotNull(doc.getAdm1_names_alternate());
		Assert.assertTrue(doc.getAdm1_names_alternate().size() == 0);
		Assert.assertNotNull(doc.getAdm2_names_alternate());
		Assert.assertTrue(doc.getAdm2_names_alternate().size() == 0);
		Assert.assertNotNull(doc.getCountry_names_alternate());
		Assert.assertTrue(doc.getCountry_names_alternate().size() == 0);
		// alternatenames multilang
		Assert.assertNotNull(doc.getName_alternates_localized());
		Assert.assertTrue(doc.getName_alternates_localized().size() == 0);
		Assert.assertNotNull(doc.getAdm1_names_alternate_localized());
		Assert.assertTrue(doc.getAdm1_names_alternate_localized().size() == 0);
		Assert.assertNotNull(doc.getAdm2_names_alternate_localized());
		Assert.assertTrue(doc.getAdm2_names_alternate_localized().size() == 0);
		Assert.assertNotNull(doc.getCountry_names_alternate_localized());
		Assert.assertTrue(doc.getCountry_names_alternate_localized().size() == 0);

	}

	@Test(expected = ServiceException.class)
	public void executeQueryWithWrongURL() {
		FulltextQuery query = new FulltextQuery("test");
		Output output = Output.withFormat(OutputFormat.JSON).withStyle(OutputStyle.FULL);
		query.withOutput(output);
		FulltextClient client = new FulltextClient(BASE_URL + "foo");
		FulltextResultsDto dto = client.executeQuery(query);
		Assert.assertNotNull(dto.getMaxScore());
		Assert.assertNotNull(dto.getNumFound());
		Assert.assertTrue(dto.getQTime() > 0);
		Assert.assertNotNull(dto.getResults());
		Assert.assertTrue(dto.getResultsSize() > 0);
		SolrResponseDto doc = dto.getResults().get(0);
		// alternate names
		Assert.assertNotNull(doc.getName_alternates());
		Assert.assertTrue(doc.getName_alternates().size() == 0);
		Assert.assertNotNull(doc.getAdm1_names_alternate());
		Assert.assertTrue(doc.getAdm1_names_alternate().size() == 0);
		Assert.assertNotNull(doc.getAdm2_names_alternate());
		Assert.assertTrue(doc.getAdm2_names_alternate().size() == 0);
		Assert.assertNotNull(doc.getCountry_names_alternate());
		Assert.assertTrue(doc.getCountry_names_alternate().size() == 0);
		// alternatenames multilang
		Assert.assertNotNull(doc.getName_alternates_localized());
		Assert.assertTrue(doc.getName_alternates_localized().size() == 0);
		Assert.assertNotNull(doc.getAdm1_names_alternate_localized());
		Assert.assertTrue(doc.getAdm1_names_alternate_localized().size() == 0);
		Assert.assertNotNull(doc.getAdm2_names_alternate_localized());
		Assert.assertTrue(doc.getAdm2_names_alternate_localized().size() == 0);
		Assert.assertNotNull(doc.getCountry_names_alternate_localized());
		Assert.assertTrue(doc.getCountry_names_alternate_localized().size() == 0);

	}

	@Test
	public void executeQueryWhenNoResults() {
		FulltextQuery query = createQuery("foobar");
		FulltextClient client = new FulltextClient(BASE_URL);
		FulltextResultsDto dto = client.executeQuery(query);
		Assert.assertNotNull(dto.getMaxScore());
		Assert.assertNotNull(dto.getNumFound());
		Assert.assertTrue(dto.getQTime() > 0);
		Assert.assertNotNull(dto.getResults());
		Assert.assertEquals(0, dto.getResultsSize());

	}

	private FulltextQuery createQuery(String queryString) {
		FulltextQuery query = new FulltextQuery(queryString);
		query.around(GisgraphyUtilsTestHelper.createPoint(2.349F, 48.853F));
		query.withRadius(1000);
		query.withAllWordsRequired(true);
		query.withPagination(Pagination.paginate().from(1).to(5));
		query.withOutput(Output.withFormat(OutputFormat.JSON).withLanguageCode("FR").withStyle(OutputStyle.FULL).withIndentation());
		query.setApikey("123");
		query.limitToCountryCode("FR");
		query.withPlaceTypes(new Class[] { City.class, Country.class });
		query.withoutSpellChecking();
		return query;
	}

}
