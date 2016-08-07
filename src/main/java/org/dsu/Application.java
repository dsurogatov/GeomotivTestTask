package org.dsu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import org.dsu.domain.Site;
import org.dsu.domain.SiteBunch;
import org.dsu.service.keywords.FillSiteKeywordService;
import org.dsu.service.sitereader.SiteFileReaderService;
import org.dsu.service.sitewriter.SiteFileWriterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Application implements CommandLineRunner {

	private static final Logger LOG = LoggerFactory.getLogger(Application.class);
	
	private static final String CSV_FILE = "input1.csv";
	private static final String JSON_FILE = "input2.json";
	
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage application with options:\n" 
					+ "arg1		 the folder name with input files\n"
			        + "arg2		 the output file name");
			return;
		}
		
		SpringApplication.run(Application.class, args);
	}
	
	/** 
	 * Get a result from a future object
	 * 
	 * @param readerResult   A future object
	 * 
	 * @return   A list of sites. If the future haven't done work or have happened an error
	 * 			 then returns an empty list.
	 */
	private static List<Site> getReaderResult(Future<List<Site>> readerResult) {
		if (readerResult.isDone()) {
			try {
				return readerResult.get();
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		} else {
			readerResult.cancel(true);
		}
		return new ArrayList<>();
	}

	@Autowired
	private SiteFileReaderService csvSiteFileReaderService;

	@Autowired
	private SiteFileReaderService jsonSiteFileReaderService;

	@Autowired
	private SiteFileWriterService jsonSiteFileWriterService;
	
	@Autowired
	private FillSiteKeywordService fillSiteKeywordService;

	@Override
	public void run(String... args) throws Exception {
		LOG.info("App start ==================================");
		LOG.info("Args: {}", Arrays.toString(args));

		Future<List<Site>> csvResult = csvSiteFileReaderService.readFile(args[0], CSV_FILE);
		Future<List<Site>> jsonResult = jsonSiteFileReaderService.readFile(args[0], JSON_FILE);
		waitReadingData(csvResult, jsonResult);

		List<SiteBunch> siteBunches = new ArrayList<>();
		siteBunches.add(new SiteBunch(CSV_FILE, fillSiteKeywordService.fillKeywords(getReaderResult(csvResult))));
		siteBunches.add(new SiteBunch(JSON_FILE, fillSiteKeywordService.fillKeywords(getReaderResult(jsonResult))));
		
		jsonSiteFileWriterService.writeFile(args[1], siteBunches);

		LOG.info("App stop ==================================");
	}

	/**
	 * Wait reading data from files
	 * 
	 * @param csvResult the csv's reader future
	 * @param jsonResult the json's reader future
	 * @throws InterruptedException if the thread has interrupted
	 */
	private void waitReadingData(Future<List<Site>> csvResult, Future<List<Site>> jsonResult)
	        throws InterruptedException {
		int i = 0;
		while (!csvResult.isDone() || !jsonResult.isDone()) {
			Thread.sleep(10);
			i++;
			if (i == 500) {
				break;
			}
		}
	}

}
