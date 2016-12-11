package org.dsu;

import java.io.StringWriter;
import java.math.BigDecimal;

import org.dsu.domain.Site;
import org.dsu.service.sitewriter.SiteWriterTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
@ActiveProfiles("test")
public class JsonSiteWriterTemplateTest {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void givenCollectionWithSite_WhenWrite2String_ThenAssertWrittenResultWithExpectingString() throws Exception {

		// create the Site instance and the collectionId
		Site site = new Site();
		site.setId(1);
		site.setName("name");
		site.setMobile(true);
		site.setKeywords("key1,key2");
		site.setScore(new BigDecimal("21.33"));
		String collectionId = "input1.csv";

		// create the writer template
		StringWriter sw = new StringWriter();
		SiteWriterTemplate jsonTemplate = (SiteWriterTemplate) applicationContext.getBean("jsonSiteWriterTemplate", sw);

		jsonTemplate.start();
		jsonTemplate.startBunch().writeCollectionId(collectionId);
		jsonTemplate.startSites().writeSite(site).finishSites();
		jsonTemplate.finishBunch();
		jsonTemplate.finish();

		// get result
		String result = sw.toString();
		System.out.printf("The result: %s%n", result);

		// test with expected string
		String expectedJSON = "[ {" 
				+ "  \"collectionId\" : \"input1.csv\"," 
				+ "  \"sites\" : [ {" 
				+ "    \"id\" : \"1\","
				+ "    \"name\" : \"name\"," 
				+ "    \"mobile\" : 1," 
				+ "    \"keywords\" : \"key1,key2\","
				+ "    \"score\" : 21.33" 
				+ "  } ]"
				+ "} ]";
		JSONAssert.assertEquals(expectedJSON, result, false);
	}
}
