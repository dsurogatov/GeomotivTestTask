package org.dsu;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

import org.dsu.domain.Site;
import org.dsu.domain.SiteBunch;
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
	public void givenFileWithValidData_ReadFile_ShouldReturnTrue() throws InterruptedException, ExecutionException, IOException {
		File temp = folder.newFile("input2.json");
		String content = "[\n"
		                  + "{\"site_id\": \"13000\", \"name\": \"example.com/json1\", \"mobile\": 1, \"score\": 21.003 },\n"
		                  + "{\"site_id\": \"13001\", \"name\": \"example.com/json2\", \"mobile\": 0, \"score\": 97 },\n"
		                  + "{\"site_id\": \"13002\", \"name\": \"example.com/json3\", \"mobile\": 0, \"score\": 311 }\n"
		               + "]\n";
		Files.write(Paths.get(temp.getCanonicalPath()), content.getBytes());
		
		BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(1);
		boolean result = jsonSiteFileReaderService.readFile(temp.toPath(), queue);
		TestCase.assertEquals(queue.size(), 1);
		TestCase.assertTrue(result);
		
		List<Site> sites = queue.take().getSites();
		//System.out.println(""+sites);
		TestCase.assertEquals(sites.size(), 3);
		Site site1 = sites.get(0);
		TestCase.assertEquals(site1.getId(), 13000);
		TestCase.assertEquals(site1.getName(), "example.com/json1");
		TestCase.assertEquals(site1.isMobile(), true);
		TestCase.assertEquals(site1.getScore(), BigDecimal.valueOf(21.003));
	}
	
	@Test
	public void givenFileWithNoValidData_WhenRead_ThenReturnFalse() throws InterruptedException, ExecutionException, IOException {
		File temp = folder.newFile("input2.json");
		String content = "[\n"
                + "{\"site_id\": \"novalid\", \"name\": \"example.com/json1\", \"mobile\": 1, \"score\": 21 },\n"
             + "]\n";
		Files.write(Paths.get(temp.getCanonicalPath()), content.getBytes());
		
		BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(1);
		boolean result = jsonSiteFileReaderService.readFile(temp.toPath(), queue);
		TestCase.assertEquals(queue.isEmpty(), true);
		TestCase.assertFalse(result);
	}
	
	@Test
	public void givenNoExistsFile_WhenRead_ShouldReturnFalse() throws InterruptedException, ExecutionException {
		BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(1);
		boolean result = jsonSiteFileReaderService.readFile(new File("a/b").toPath(), queue);
		TestCase.assertEquals(queue.isEmpty(), true);
		TestCase.assertFalse(result);
	}
	
	@Test
	public void givenNullPath_WhenRead_ShouldReturnFalse() throws InterruptedException, ExecutionException {
		BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(1);
		boolean result = jsonSiteFileReaderService.readFile(null, queue);
		TestCase.assertEquals(queue.isEmpty(), true);
		TestCase.assertFalse(result);
	}
	
	@Test
	public void givenNullQueue_WhenRead_ShouldReturnFalse() throws InterruptedException, ExecutionException {
		boolean result = jsonSiteFileReaderService.readFile(new File("a/b").toPath(), null);
		TestCase.assertFalse(result);
	}
}
