package org.dsu;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.util.concurrent.BlockingQueue;

import org.dsu.component.worker.Worker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTestConfig.class })
@ActiveProfiles("test")
public class ApplicationTest {

	@Autowired
	private Application app;
	
	@Autowired
	private Worker fileReaderWorker;

	@Autowired
	private Worker fileWriterWorker;
	
	@Before
	public void beforeTest() {
		Mockito.reset(fileReaderWorker);
		Mockito.reset(fileWriterWorker);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void whenRun_ThenOk() throws Exception {
		when(fileReaderWorker.start(any(BlockingQueue.class))).thenReturn(Worker.RETURN_OK);
		when(fileWriterWorker.start(any(BlockingQueue.class))).thenReturn(Worker.RETURN_OK);
		
		app.run(new String[]{});
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void givenFailWorker_whenRun_ThenFail() throws Exception {
		when(fileReaderWorker.start(any(BlockingQueue.class))).thenReturn(Worker.RETURN_FAIL);
		when(fileWriterWorker.start(any(BlockingQueue.class))).thenReturn(Worker.RETURN_OK);
		
		app.run(new String[]{});
	}
}
