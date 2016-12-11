package org.dsu.worker;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

import org.dsu.TestConfig;
import org.dsu.component.ApplicationProperties;
import org.dsu.component.worker.Worker;
import org.dsu.service.sitewriter.SiteFileWriterService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class, WorkerTestConfig.class })
@ActiveProfiles("test")
public class FileWriterWorkerTest {
	
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	@Autowired
	private Worker fileWriterWorker;

	@Autowired
	private ApplicationProperties appProps;
	
	@Autowired
	private SiteFileWriterService jsonSiteFileWriterService;

	@Test
	public void givenNotDefinedOutputFileName_WhenStart_ThenReturnFalse() throws Exception {

		// mock appProps to return an empty file name
		when(appProps.getOutputFileName()).thenReturn("");
		
		// start worker
		Future<Boolean> result = fileWriterWorker.start(new ArrayBlockingQueue<>(1));
		
		// check the return result is false
		assertFalse(result.get());
	}
	
	@Test
	public void givenNullQueue_WhenStart_ThenReturnFalse() throws Exception {

		// start worker
		Future<Boolean> result = fileWriterWorker.start(null);
		
		// check the return result is false
		assertFalse(result.get());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void givenInputFile_WhenStart_ThenReturnTrue() throws Exception {

		// create a temp file
		Path tempJson = folder.newFile("output.json").toPath();
		
		// mock appProps to return the temp file name
		when(appProps.getOutputFileName()).thenReturn(tempJson.toString());
		
		// mock writer's method 'writeFile' to return true
		when(jsonSiteFileWriterService.writeFile(any(Path.class), any(BlockingQueue.class))).thenReturn(true);
		
		// start worker
		Future<Boolean> result = fileWriterWorker.start(new ArrayBlockingQueue<>(1));
		
		// check the returned result is true
		assertTrue(result.get());
	}
}
