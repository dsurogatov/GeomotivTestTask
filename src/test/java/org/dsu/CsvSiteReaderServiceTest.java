package org.dsu;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
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
public class CsvSiteReaderServiceTest {
	
	@Rule
    public TemporaryFolder folder = new TemporaryFolder();

	@Autowired
	private SiteFileReaderService csvSiteFileReaderService;

	@Test
	public void givenFileWithValidData_ReadFile_ShouldReturnTrue() throws InterruptedException, ExecutionException, IOException {
		File temp = folder.newFile("input1.csv");
		String content = "id,name,is mobile,score\n" 
				   + "12000,example.com/csv1,true,454.23\n"
		           + "12001,example.com/csv2,true,128\n" 
				   + "12002,example.com/csv3,false,522\n";
		Files.write(temp.toPath(), content.getBytes());
		
		BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(1);
		boolean result = csvSiteFileReaderService.readFile(temp.toPath(), queue);
		TestCase.assertEquals(queue.size(), 1);
		TestCase.assertTrue(result);

		List<Site> sites = queue.take().getSites();
		//System.out.println(""+sites);
		Site site1 = sites.get(0);
		TestCase.assertEquals(site1.getId(), 12000);
		TestCase.assertEquals(site1.getName(), "example.com/csv1");
		TestCase.assertEquals(site1.isMobile(), true);
		TestCase.assertEquals(site1.getScore(), BigDecimal.valueOf(454.23));
	}
	
	// TODO write test for 100 sites in one bunch
	
	@Test
	public void givenFileWithNoValidData_WhenRead_ThenReturnFalse() throws InterruptedException, ExecutionException, IOException {
		File temp = folder.newFile("input1.csv");
		String content = "id,name,is mobile,score\n" 
				   + "12000example.com/csv1,true,454\n"
		           + "12001,example.com/csv2,true,128\n" 
				   + "12002,example.com/csv3,false,522\n";
		Files.write(temp.toPath(), content.getBytes());
		
		BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(1);
		boolean result = csvSiteFileReaderService.readFile(temp.toPath(), queue);
		TestCase.assertEquals(queue.isEmpty(), true);
		TestCase.assertFalse(result);
	}
	
	@Test
	public void givenNoExistsFile_WhenRead_ShouldReturnFalse() throws InterruptedException, ExecutionException {
		BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(1);
		boolean result = csvSiteFileReaderService.readFile(new File("a/b").toPath(), queue);
		TestCase.assertEquals(queue.isEmpty(), true);
		TestCase.assertFalse(result);
	}
	
	@Test
	public void givenNullPath_WhenRead_ShouldReturnFalse() throws InterruptedException, ExecutionException {
		BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(1);
		boolean result = csvSiteFileReaderService.readFile(null, queue);
		TestCase.assertEquals(queue.isEmpty(), true);
		TestCase.assertFalse(result);
	}
	
	@Test
	public void givenNullQueue_WhenRead_ShouldReturnFalse() throws InterruptedException, ExecutionException {
		boolean result = csvSiteFileReaderService.readFile(new File("a/b").toPath(), null);
		TestCase.assertFalse(result);
	}
}
