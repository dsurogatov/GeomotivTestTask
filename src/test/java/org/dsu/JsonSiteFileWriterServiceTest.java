package org.dsu;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.dsu.builder.SiteBuilder;
import org.dsu.component.ApplicationProperties;
import org.dsu.domain.Site;
import org.dsu.domain.SiteBunch;
import org.dsu.service.sitewriter.SiteFileWriterService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
@ActiveProfiles("test")
public class JsonSiteFileWriterServiceTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Autowired
	private ApplicationProperties appProps;
	
	@Autowired
	private SiteFileWriterService jsonSiteFileWriterService;
	
	@Before
	public void beforeTest() {
		Mockito.reset(appProps);
	}
	
	@Test
	public void givenNullPathParam_WhenWriteFile_ThenReturnFalse() {
		boolean result = jsonSiteFileWriterService.writeFile(null, new ArrayBlockingQueue<>(10));
		
		assertFalse(result);
	}
	
	@Test
	public void givenNullQueueParam_WhenWriteFile_ThenReturnFalse() {
		boolean result = jsonSiteFileWriterService.writeFile(Paths.get("test"), null);
		
		assertFalse(result);
	}
	
	@Test
	public void givenQueueWithoutPoison_WhenWriteFile_ThenReturnFalse() throws Exception {
		File temp = folder.newFile("output.json");
		
		when(appProps.getProducerConsumerOfferTimeOut()).thenReturn(1);
		
		SiteBunch bunch = new SiteBunch("input1.csv", new ArrayList<>());
		BlockingQueue<SiteBunch> bunches = new ArrayBlockingQueue<>(10);
		bunches.add(bunch);
		
		boolean result = jsonSiteFileWriterService.writeFile(temp.toPath(), bunches);
		
		assertFalse(result);
	}

	@Test
	public void givenSiteBunch_WhenWriteFile_ThenAssertWrittenFileWithExpectedString() throws Exception {
		File temp = folder.newFile("output.json");

		Site site = new SiteBuilder().id(1).name("name").mobile(true).keywords("key1,key2").score("21.33").build();
		List<Site> sites = new ArrayList<Site>();
		sites.add(site);
		SiteBunch bunch = new SiteBunch("input1.csv", sites);
		
		BlockingQueue<SiteBunch> bunches = new ArrayBlockingQueue<>(10);
		bunches.add(bunch);
		bunches.add(SiteBunch.POISON);

		boolean result = jsonSiteFileWriterService.writeFile(temp.toPath(), bunches);
		
		assertTrue(result);

		String jsonWritten = new String(Files.readAllBytes(Paths.get(temp.getCanonicalPath())));
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
		//System.out.println(jsonWritten);
		JSONAssert.assertEquals(expectedJSON, jsonWritten, false);
	}
	
	@Test
	public void givenTwoSiteBunchesWithTwoSites_WhenWriteFile_ThenAssertWrittenFileWithExpectedString() throws Exception {
		File temp = folder.newFile("output.json");

		List<Site> sites = new ArrayList<Site>();
		sites.add(new SiteBuilder().id(1).name("name").mobile(true).keywords("key1,key2").score("21.33").build());
		sites.add(new SiteBuilder().id(2).name("name2").mobile(true).keywords("key12,key22").score("22.33").build());
		SiteBunch bunch = new SiteBunch("input1.csv", sites);
		
		List<Site> sites2 = new ArrayList<Site>();
		sites2.add(new SiteBuilder().id(3).name("name3").mobile(false).keywords("key3,key3").score("3000.335").build());
		sites2.add(new SiteBuilder().id(4).name("name4").mobile(true).keywords("key4,key4").score("444.33").build());
		SiteBunch bunch2 = new SiteBunch("input2.json", sites2);

		BlockingQueue<SiteBunch> bunches = new ArrayBlockingQueue<>(10);
		bunches.add(bunch);
		bunches.add(bunch2);
		bunches.add(SiteBunch.POISON);

		boolean result = jsonSiteFileWriterService.writeFile(temp.toPath(), bunches);
		
		assertTrue(result);

		String jsonWritten = new String(Files.readAllBytes(Paths.get(temp.getCanonicalPath())));
		String expectedJSON = "[ {" 
						+ "  \"collectionId\" : \"input1.csv\"," 
						+ "  \"sites\" : [ {" 
						+ "    \"id\" : \"1\","
						+ "    \"name\" : \"name\"," 
						+ "    \"mobile\" : 1," 
						+ "    \"keywords\" : \"key1,key2\","
						+ "    \"score\" : 21.33" 
						+ "  }, { "
		                + "    \"id\" : \"2\","
		                + "    \"name\" : \"name2\"," 
		                + "    \"mobile\" : 1," 
		                + "    \"keywords\" : \"key12,key22\","
		                + "    \"score\" : 22.33" 
		                + "  } ]"
		                + "}, {"
		                + "  \"collectionId\" : \"input2.json\"," 
		                + "  \"sites\" : [ {" 
		                + "    \"id\" : \"3\","
		                + "    \"name\" : \"name3\"," 
		                + "    \"mobile\" : 0," 
		                + "    \"keywords\" : \"key3,key3\","
		                + "    \"score\" : 3000.335" 
		                + "  }, { "
		                + "    \"id\" : \"4\","
		                + "    \"name\" : \"name4\"," 
		                + "    \"mobile\" : 1," 
		                + "    \"keywords\" : \"key4,key4\","
		                + "    \"score\" : 444.33" 
		                + "  } ]"
		                + "} ]";
		//System.out.println(jsonWritten);
		JSONAssert.assertEquals(expectedJSON, jsonWritten, false);
	}
}

