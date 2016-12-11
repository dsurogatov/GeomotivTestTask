package org.dsu;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.dsu.domain.Site;
import org.dsu.domain.SiteBunch;
import org.dsu.service.sitewriter.SiteFileWriterService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
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
	private SiteFileWriterService jsonSiteFileWriterService;

	@Test
	public void givenSiteBunch_WhenWriteFile_ThenTestWrittenFileWithExpectedString() throws Exception {
		File temp = folder.newFile("output.json");

		Site site = new Site();
		site.setId(1);
		site.setName("name");
		site.setMobile(true);
		site.setKeywords("key1,key2");
		site.setScore(new BigDecimal("21.33"));
		List<Site> sites = new ArrayList<Site>();
		sites.add(site);
		SiteBunch bunch = new SiteBunch("input1.csv", sites);
		BlockingQueue<SiteBunch> bunches = new ArrayBlockingQueue<>(10);
		bunches.add(bunch);

		jsonSiteFileWriterService.writeFile(temp.toPath(), bunches);

		String jsonWritten = new String(Files.readAllBytes(Paths.get(temp.getCanonicalPath())));
		String expectedJSON = "[ {" 
						+ "  \"collectionId\" : \"input1.csv\"," 
						+ "  \"sites\" : [ {" 
						+ "    \"id\" : \"1\","
						+ "    \"name\" : \"name\"," 
						+ "    \"mobile\" : 1," 
						+ "    \"keywords\" : \"key1,key2\","
						+ "    \"score\" : 21.33" 
						+ "  } "
						+ "]" 
						+ "} ]";
		JSONAssert.assertEquals(expectedJSON, jsonWritten, false);
	}
}
