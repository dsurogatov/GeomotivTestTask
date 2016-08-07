package org.dsu;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.dsu.domain.Site;
import org.dsu.service.sitereader.SiteFileReaderService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class JsonSiteReaderServiceTest {

	@Rule
    public TemporaryFolder folder = new TemporaryFolder();

	@Autowired
	private SiteFileReaderService jsonSiteFileReaderService;
	
	@Test
	public void readFile_WithValidData_ShouldReturnSiteEntries() throws InterruptedException, ExecutionException, IOException {
		File temp = folder.newFile("input2.json");
		String content = "[\n"
		                  + "{\"site_id\": \"13000\", \"name\": \"example.com/json1\", \"mobile\": 1, \"score\": 21.003 },\n"
		                  + "{\"site_id\": \"13001\", \"name\": \"example.com/json2\", \"mobile\": 0, \"score\": 97 },\n"
		                  + "{\"site_id\": \"13002\", \"name\": \"example.com/json3\", \"mobile\": 0, \"score\": 311 }\n"
		               + "]\n";
		Files.write(Paths.get(temp.getCanonicalPath()), content.getBytes());
		
		List<Site> sites = jsonSiteFileReaderService.readFile(temp.getParent(), temp.getName()).get();
		//System.out.println(""+sites);
		TestCase.assertEquals(sites.size(), 3);
		Site site1 = sites.get(0);
		TestCase.assertEquals(site1.getId(), 13000);
		TestCase.assertEquals(site1.getName(), "example.com/json1");
		TestCase.assertEquals(site1.isMobile(), true);
		TestCase.assertEquals(site1.getScore(), BigDecimal.valueOf(21.003));
	}
	
	@Test
	public void readFile_WithNoValidData_ShouldReturnEmptyList() throws InterruptedException, ExecutionException, IOException {
		File temp = folder.newFile("input2.json");
		String content = "[\n"
                + "{\"site_id\": \"novalid\", \"name\": \"example.com/json1\", \"mobile\": 1, \"score\": 21 },\n"
             + "]\n";
		Files.write(Paths.get(temp.getCanonicalPath()), content.getBytes());
		
		List<Site> sites = jsonSiteFileReaderService.readFile(temp.getParent(), temp.getName()).get();
		TestCase.assertEquals(sites.isEmpty(), true);
	}
	
	@Test
	public void readFile_NoExistsFile_ShouldReturnEmptyList() throws InterruptedException, ExecutionException {
		List<Site> sites = jsonSiteFileReaderService.readFile("a", "b").get();
		TestCase.assertEquals(sites.isEmpty(), true);
	}
	
	@Test
	public void readFile_WithNullInputParams_ShouldReturnEmptyList() throws InterruptedException, ExecutionException {
		List<Site> sites = jsonSiteFileReaderService.readFile(null, null).get();
		TestCase.assertEquals(sites.isEmpty(), true);
	}
}
