package com.gisgraphy.relevance;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.fulltext.FulltextClient;
import com.gisgraphy.fulltext.FulltextQuery;
import com.gisgraphy.fulltext.FulltextResultsDto;
import com.gisgraphy.fulltext.SolrResponseDto;
import com.gisgraphy.serializer.common.OutputFormat;
import com.gisgraphy.test.GisgraphyUtilsTestHelper;

public class FulltextRelevanceTest {

	private static final String BASE_URL = GisgraphyUtilsTestHelper.HTTP_BASE_URL+"/fulltext/";
	
	String[][] addresses = {
			{"paris","FR","paris",null},
			{"rue du caire paris","FR","rue du caire","paris",null}
	};

	public void executeSearch(String text,String countryCode,String nameToTest,String isInToTest) {
		FulltextQuery query = new FulltextQuery(text);
		Output output = Output.withFormat(OutputFormat.JSON).withStyle(OutputStyle.FULL);
		if (countryCode!=null){
			query.limitToCountryCode(countryCode);
		}
		query.withOutput(output);
		FulltextClient client = new FulltextClient(BASE_URL);
		FulltextResultsDto dto = client.executeQuery(query);
		Assert.assertNotNull("the results are null",dto);
		SolrResponseDto solrResponseDto = dto.getResults().get(0);
		Assert.assertNotNull("the result is null",solrResponseDto);
		System.out.println(solrResponseDto);
		if (nameToTest!=null){
			if (solrResponseDto.getName()==null){
				Assert.fail("the name is null but "+nameToTest+ " was expected");
			}
			Assert.assertEquals("the name is not correct for query '"+text+"'",nameToTest.toLowerCase(),solrResponseDto.getName().toLowerCase());
		}
		if (isInToTest!=null){
			if (solrResponseDto.getIs_in()==null){
				Assert.fail("the Is_in is null but "+nameToTest+ " was expected");
			}
			Assert.assertEquals("the is is_in not correct for query '"+text+"'",isInToTest.toLowerCase(),solrResponseDto.getIs_in().toLowerCase());
		}
		

	}

	@Test
	public void testRelevance() {
		for (String[] address:addresses){
			executeSearch(address[0], address[1], address[2],address[3]);
		}
	}

}
