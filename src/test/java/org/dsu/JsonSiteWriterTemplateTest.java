package org.dsu;

import java.io.StringWriter;

import org.dsu.builder.SiteBuilder;
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
		Site site = new SiteBuilder().id(1).name("name1").mobile(true).keywords("k1, k2").score("21.33").build();
		String collectionId = "input1.csv";

		// create the writer template
		StringWriter sw = new StringWriter();
		SiteWriterTemplate jsonTemplate = (SiteWriterTemplate) applicationContext.getBean("jsonSiteWriterTemplate", sw);

		jsonTemplate.start();
		jsonTemplate.startBunch().writeCollectionId(collectionId);
		jsonTemplate.startSites().writeSite(site).finishSites();
		jsonTemplate.finishBunch();
		jsonTemplate.finish();
		jsonTemplate.close();

		// get result
		String result = sw.toString();
		//System.out.printf("The result: %n%s%n", result);

		// test with expected string
		String expectedJSON = "[ {" 
				+ "  \"collectionId\" : \"input1.csv\"," 
				+ "  \"sites\" : [ {" 
				+ "    \"id\" : \"1\","
				+ "    \"name\" : \"name1\"," 
				+ "    \"mobile\" : 1," 
				+ "    \"keywords\" : \"k1, k2\","
				+ "    \"score\" : 21.33" 
				+ "  } ]"
				+ "} ]";
		JSONAssert.assertEquals(expectedJSON, result, false);
	}
	
	@Test
	public void givenCollectionWithSeveralSites_WhenWrite2String_ThenAssertWrittenResultWithExpectingString() throws Exception {
	 
	    // create the Site instances and the collectionId
        Site site = new SiteBuilder().id(1).name("name1").mobile(true).keywords("k1, k2").score("21.33").build();
        Site site2 = new SiteBuilder().id(2).name("name2").mobile(true).keywords("k12, k22").score("222.01").build();
        String collectionId = "input1.csv";

        // create the writer template
        StringWriter sw = new StringWriter();
        SiteWriterTemplate jsonTemplate = (SiteWriterTemplate) applicationContext.getBean("jsonSiteWriterTemplate", sw);

        jsonTemplate.start();
        jsonTemplate.startBunch().writeCollectionId(collectionId);
        jsonTemplate.startSites().writeSite(site).writeSite(site2).finishSites();
        jsonTemplate.finishBunch();
        jsonTemplate.finish();
        jsonTemplate.close();

        // get result
        String result = sw.toString();

        // test with expected string
        String expectedJSON = "[ {" 
                + "  \"collectionId\" : \"input1.csv\"," 
                + "  \"sites\" : [ {" 
                + "    \"id\" : \"1\","
                + "    \"name\" : \"name1\"," 
                + "    \"mobile\" : 1," 
                + "    \"keywords\" : \"k1, k2\","
                + "    \"score\" : 21.33" 
                + "  }, { "
                + "    \"id\" : \"2\","
                + "    \"name\" : \"name2\"," 
                + "    \"mobile\" : 1," 
                + "    \"keywords\" : \"k12, k22\","
                + "    \"score\" : 222.01" 
                + "  } ]"
                + "} ]";
        JSONAssert.assertEquals(expectedJSON, result, false);
	}
	
	@Test
    public void givenSeveralCollectionWithSeveralSites_WhenWrite2String_ThenAssertWrittenResultWithExpectingString() throws Exception {
     
        // create the Site instances and the collectionId
        Site site = new SiteBuilder().id(1).name("name1").mobile(true).keywords("k1, k2").score("21.33").build();
        Site site2 = new SiteBuilder().id(2).name("name2").mobile(true).keywords("k12, k22").score("222.01").build();
        String collectionId = "input1.csv";
        String collectionId2 = "input2.json";

        // create the writer template
        StringWriter sw = new StringWriter();
        SiteWriterTemplate jsonTemplate = (SiteWriterTemplate) applicationContext.getBean("jsonSiteWriterTemplate", sw);

        jsonTemplate.start();
        jsonTemplate.startBunch().writeCollectionId(collectionId);
        jsonTemplate.startSites().writeSite(site).writeSite(site2).finishSites();
        jsonTemplate.finishBunch();
        jsonTemplate.startBunch().writeCollectionId(collectionId2);
        jsonTemplate.startSites().writeSite(site).writeSite(site2).finishSites();
        jsonTemplate.finishBunch();
        jsonTemplate.finish();
        jsonTemplate.close();

        // get result
        String result = sw.toString();

        // test with expected string
        String expectedJSON = "[ {" 
                + "  \"collectionId\" : \"input1.csv\"," 
                + "  \"sites\" : [ {" 
                + "    \"id\" : \"1\","
                + "    \"name\" : \"name1\"," 
                + "    \"mobile\" : 1," 
                + "    \"keywords\" : \"k1, k2\","
                + "    \"score\" : 21.33" 
                + "  }, { "
                + "    \"id\" : \"2\","
                + "    \"name\" : \"name2\"," 
                + "    \"mobile\" : 1," 
                + "    \"keywords\" : \"k12, k22\","
                + "    \"score\" : 222.01" 
                + "  } ]"
                + "}, {"
                + "  \"collectionId\" : \"input2.json\"," 
                + "  \"sites\" : [ {" 
                + "    \"id\" : \"1\","
                + "    \"name\" : \"name1\"," 
                + "    \"mobile\" : 1," 
                + "    \"keywords\" : \"k1, k2\","
                + "    \"score\" : 21.33" 
                + "  }, { "
                + "    \"id\" : \"2\","
                + "    \"name\" : \"name2\"," 
                + "    \"mobile\" : 1," 
                + "    \"keywords\" : \"k12, k22\","
                + "    \"score\" : 222.01" 
                + "  } ]"
                + "} ]";
        JSONAssert.assertEquals(expectedJSON, result, false);
    }
}
