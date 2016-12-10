package org.dsu.worker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

import org.dsu.TestConfig;
import org.dsu.component.ApplicationProperties;
import org.dsu.component.worker.Worker;
import org.dsu.domain.SiteBunch;
import org.dsu.service.sitereader.SiteFileReaderService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class, WorkerTestConfig.class })
@ActiveProfiles("test")
public class FileReaderWorkerTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Autowired
	private Worker fileReaderWorker;

	@Autowired
	private ApplicationProperties appProps;

	@Autowired
	private SiteFileReaderService csvSiteFileReaderService;

	@Autowired
	private SiteFileReaderService jsonSiteFileReaderService;
	
	@Before
	public void beforeTest() {
		Mockito.reset(appProps);
		Mockito.reset(csvSiteFileReaderService);
		Mockito.reset(jsonSiteFileReaderService);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void givenCsvJsonFiles_WhenStart_ThenReturnTrue() throws Exception {
		Path tempCsv = folder.newFile("input1.csv").toPath();
		folder.newFile("input2.json").toPath();
		String inputFolder = tempCsv.getParent().toString();

		// mock properties for the input folder and files
		when(appProps.getInputFolderName()).thenReturn(inputFolder);
		when(appProps.getInputFilesNames()).thenReturn("input1.csv,input2.json");

		// mock reader services for "read" return true
		when(csvSiteFileReaderService.readFile(any(Path.class), any(BlockingQueue.class))).thenReturn(true);
		when(jsonSiteFileReaderService.readFile(any(Path.class), any(BlockingQueue.class))).thenReturn(true);
		
		// reader worker start
		BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(1);
		Future<Boolean> result = fileReaderWorker.start(queue);

		// check return true
		assertTrue(result.get());
		
		// check the queue contains poison
		assertEquals(queue.iterator().next(), SiteBunch.POISON);
		
		// verify calls readers
		verify(csvSiteFileReaderService).readFile(any(Path.class), any(BlockingQueue.class));
		verify(jsonSiteFileReaderService).readFile(any(Path.class), any(BlockingQueue.class));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void givenWrongExtensionFile_WhenStart_ThenReturnFalse() throws Exception {
		Path tempCsv = folder.newFile("input1.csv").toPath();
		folder.newFile("input2.txt").toPath();
		String inputFolder = tempCsv.getParent().toString();
		
		// mock properties
		when(appProps.getInputFolderName()).thenReturn(inputFolder);
		when(appProps.getInputFilesNames()).thenReturn("input1.csv,input2.txt");
		
		// mock the csv reader read method
		when(csvSiteFileReaderService.readFile(any(Path.class), any(BlockingQueue.class))).thenReturn(true);
		when(jsonSiteFileReaderService.readFile(any(Path.class), any(BlockingQueue.class))).thenReturn(true);
		
		// start worker
		BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(1);
		Future<Boolean> result = fileReaderWorker.start(queue);
		
		// check than the worker return false
		assertFalse(result.get());
		
		// check the queue is empty
		assertTrue(queue.isEmpty());
	}
}
