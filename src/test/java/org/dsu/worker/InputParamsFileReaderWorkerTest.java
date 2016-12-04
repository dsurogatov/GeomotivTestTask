package org.dsu.worker;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

import org.dsu.TestConfig;
import org.dsu.component.ApplicationProperties;
import org.dsu.component.worker.Worker;
import org.dsu.domain.SiteBunch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class, WorkerTestConfig.class })
@ActiveProfiles("test")
public class InputParamsFileReaderWorkerTest {
	
	private static final BlockingQueue<SiteBunch> BQ = new ArrayBlockingQueue<>(1);
	
	@Autowired
	private Worker fileReaderWorker;
	
	@Autowired
	private ApplicationProperties appProps;
	
	@Before
	public void beforeTest() {
		Mockito.reset(appProps);
	}

	@Test
	public void givenNoValidFolderName_WhenStart_ThenReturnFalse() throws Exception {
		when(appProps.getInputFolderName()).thenReturn("123456");
		
		Future<Boolean> task = fileReaderWorker.start(BQ);
		
		assertEquals(false, task.get().booleanValue());
	}
	
	@Test
	public void givenNullFolderName_WhenStart_ThenReturnFalse() throws Exception {
		Future<Boolean> task = fileReaderWorker.start(BQ);
		
		assertEquals(false, task.get().booleanValue());
	}
	
	@Test
    public void givenNullInputFilesNames_WhenStart_ThenReturnFalse() throws Exception {
		when(appProps.getInputFolderName()).thenReturn("");
		when(appProps.getInputFilesNames()).thenReturn(null);
		
		Future<Boolean> task = fileReaderWorker.start(BQ);

        assertEquals(false, task.get().booleanValue());
    }

    @Test
    public void givenEmptyInputFilesNames_WhenStart_ThenReturnFalse() throws Exception {
    	when(appProps.getInputFolderName()).thenReturn("");
		when(appProps.getInputFilesNames()).thenReturn("");
		
        Future<Boolean> task = fileReaderWorker.start(BQ);

        assertEquals(false, task.get().booleanValue());
    }
    
    @Test
    public void givenNoExistInputFiles_WhenStart_ThenReturnFalse() throws Exception {
    	when(appProps.getInputFolderName()).thenReturn("");
		when(appProps.getInputFilesNames()).thenReturn("123456789012");

        Future<Boolean> task = fileReaderWorker.start(BQ);

        assertEquals(false, task.get().booleanValue());
    }
    
    @Test
    public void givenInputFilesNamesAndEmptySeparator_WhenStart_ThenReturnFalse() throws Exception {
        when(appProps.getInputFolderName()).thenReturn("");
		when(appProps.getInputFilesNames()).thenReturn("123456789012.txt,123456789012.js");
		when(appProps.getInputFilesNamesSeparator()).thenReturn("");

        Future<Boolean> task = fileReaderWorker.start(BQ);

        assertEquals(false, task.get().booleanValue());
    }
    
    @Test
    public void givenNoExistInputFilesAndValidSeparator_WhenStart_ThenReturnFalse() throws Exception {
        when(appProps.getInputFolderName()).thenReturn("");
		when(appProps.getInputFilesNames()).thenReturn("123456789012.txt,123456789012.js");
		when(appProps.getInputFilesNamesSeparator()).thenReturn(",");
        
        Future<Boolean> task = fileReaderWorker.start(BQ);
        
        assertEquals(false, task.get().booleanValue());
    }
    
    @Test
    public void givenNullQueue_WhenStart_ThenReturnFalse() throws Exception {
        Future<Boolean> task = fileReaderWorker.start(null);
        
        assertEquals(false, task.get().booleanValue());
    }
	
}
