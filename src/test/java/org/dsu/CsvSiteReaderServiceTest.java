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
public class CsvSiteReaderServiceTest {
	
	@Rule
    public TemporaryFolder folder = new TemporaryFolder();

	@Autowired
	private SiteFileReaderService csvSiteFileReaderService;

	@Test
	public void readFile_WithValidData_ShouldReturnSiteEntries() throws InterruptedException, ExecutionException, IOException {
		File temp = folder.newFile("input1.csv");
		String content = "id,name,is mobile,score\n" 
				   + "12000,example.com/csv1,true,454.23\n"
		           + "12001,example.com/csv2,true,128\n" 
				   + "12002,example.com/csv3,false,522\n";
		Files.write(Paths.get(temp.getCanonicalPath()), content.getBytes());
		
		List<Site> sites = csvSiteFileReaderService.readFile(temp.getParent(), temp.getName()).get();
		//System.out.println(""+sites);
		TestCase.assertEquals(sites.size(), 3);
		Site site1 = sites.get(0);
		TestCase.assertEquals(site1.getId(), 12000);
		TestCase.assertEquals(site1.getName(), "example.com/csv1");
		TestCase.assertEquals(site1.isMobile(), true);
		TestCase.assertEquals(site1.getScore(), BigDecimal.valueOf(454.23));
	}
	
	@Test
	public void readFile_WithNoValidData_ShouldReturnEmptyList() throws InterruptedException, ExecutionException, IOException {
		File temp = folder.newFile("input1.csv");
		String content = "id,name,is mobile,score\n" 
				   + "12000example.com/csv1,true,454\n"
		           + "12001,example.com/csv2,true,128\n" 
				   + "12002,example.com/csv3,false,522\n";
		Files.write(Paths.get(temp.getCanonicalPath()), content.getBytes());
		
		List<Site> sites = csvSiteFileReaderService.readFile(temp.getParent(), temp.getName()).get();
		TestCase.assertEquals(sites.isEmpty(), true);
	}
	
	@Test
	public void readFile_NoExistsFile_ShouldReturnEmptyList() throws InterruptedException, ExecutionException {
		List<Site> sites = csvSiteFileReaderService.readFile("a", "b").get();
		TestCase.assertEquals(sites.isEmpty(), true);
	}
	
	@Test
	public void readFile_WithNullInputParams_ShouldReturnEmptyList() throws InterruptedException, ExecutionException {
		List<Site> sites = csvSiteFileReaderService.readFile(null, null).get();
		TestCase.assertEquals(sites.isEmpty(), true);
	}
}
