package org.dsu.worker;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.dsu.TestConfig;
import org.dsu.commom.Constant;
import org.dsu.component.worker.Worker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class FileReaderWorkerTest {
	
	@Autowired
	private Worker fileReaderWorker;

	@Test
	public void whenPassValidFolderName_ThenReturnTrue() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constant.PARAM_INPUT_FOLDER_NAME, Paths.get("").toAbsolutePath().toString());
		
		Future<Boolean> task = fileReaderWorker.start(params);
		
		TestCase.assertEquals(true, task.get().booleanValue());
	}
	
	@Test
	public void whenPassNovalidFolderName_ThenReturnFalse() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constant.PARAM_INPUT_FOLDER_NAME, "123456");
		
		Future<Boolean> task = fileReaderWorker.start(params);
		
		TestCase.assertEquals(false, task.get().booleanValue());
	}
	
	@Test
	public void whenPassNullFolderName_ThenReturnFalse() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constant.PARAM_INPUT_FOLDER_NAME, null);
		
		Future<Boolean> task = fileReaderWorker.start(params);
		
		TestCase.assertEquals(false, task.get().booleanValue());
	}
	
	@Test
	public void whenPassNotStringFolderName_ThenReturnFalse() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constant.PARAM_INPUT_FOLDER_NAME, new Object());
		
		Future<Boolean> task = fileReaderWorker.start(params);
		
		TestCase.assertEquals(false, task.get().booleanValue());
	}
	
	@Test
	public void whenNoPassFolderName_ThenReturnFalse() throws Exception {
		Future<Boolean> task = fileReaderWorker.start(new HashMap<String, Object>());
		
		TestCase.assertEquals(false, task.get().booleanValue());
	}
	
	@Test
	public void whenPassNullParams_ThenReturnFalse() throws Exception {
		Future<Boolean> task = fileReaderWorker.start(null);
		
		TestCase.assertEquals(false, task.get().booleanValue());
	}
	
}
