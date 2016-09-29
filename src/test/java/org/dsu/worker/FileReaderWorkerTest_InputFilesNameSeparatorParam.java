package org.dsu.worker;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.dsu.TestConfig;
import org.dsu.commom.Constant;
import org.dsu.component.worker.Worker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class FileReaderWorkerTest_InputFilesNameSeparatorParam {
    
    Map<String, Object> params;

    @Autowired
    private Worker fileReaderWorker;

    @Before
    public void initialize() {
        params = new HashMap<String, Object>();
        params.put(Constant.PARAM_INPUT_FOLDER_NAME, Paths.get("").toAbsolutePath().toString());
    }

    @Test
    public void whenNotPassInputFilesNames_ShouldReturnFalse() throws Exception {
        Future<Boolean> task = fileReaderWorker.start(params);

        TestCase.assertEquals(false, task.get().booleanValue());
    }

    @Test
    public void whenPassNullInputFilesNames_ShouldReturnFalse() throws Exception {
        params.put(Constant.PARAM_INPUT_FILES_NAMES, null);

        Future<Boolean> task = fileReaderWorker.start(params);

        TestCase.assertEquals(false, task.get().booleanValue());
    }
    
    @Test
    public void whenPassEmptyInputFilesNames_ShouldReturnFalse() throws Exception {
        params.put(Constant.PARAM_INPUT_FILES_NAMES, "");

        Future<Boolean> task = fileReaderWorker.start(params);

        TestCase.assertEquals(false, task.get().booleanValue());
    }
    
    @Test
    public void whenPassNoValidInputFilesNames_ShouldReturnFalse() throws Exception {
        params.put(Constant.PARAM_INPUT_FILES_NAMES, "123456");

        Future<Boolean> task = fileReaderWorker.start(params);

        TestCase.assertEquals(false, task.get().booleanValue());
    }
    
    @Test
    public void whenPassValidInputFilesNamesAndNotPassSeparator_WithNoExistsFiles_ShouldReturnFalse() throws Exception {
        params.put(Constant.PARAM_INPUT_FILES_NAMES, "1.txt,2.js");

        Future<Boolean> task = fileReaderWorker.start(params);

        TestCase.assertEquals(false, task.get().booleanValue());
    }
    
    @Test
    public void whenPassValidInputFilesNamesAndNullSeparator_WithNoExistsFiles_ShouldReturnFalse() throws Exception {
        params.put(Constant.PARAM_INPUT_FILES_NAMES, "1.txt,2.js");
        params.put(Constant.PARAM_INPUT_FILES_NAMES_SEPARATOR, null);

        Future<Boolean> task = fileReaderWorker.start(params);

        TestCase.assertEquals(false, task.get().booleanValue());
    }
    
    @Test
    public void whenPassValidInputFilesNamesAndEmptySeparator_WithNoExistsFiles_ShouldReturnFalse() throws Exception {
        params.put(Constant.PARAM_INPUT_FILES_NAMES, "1.txt,2.js");
        params.put(Constant.PARAM_INPUT_FILES_NAMES_SEPARATOR, "");
        
        Future<Boolean> task = fileReaderWorker.start(params);
        
        TestCase.assertEquals(false, task.get().booleanValue());
    }
    
    @Test
    public void whenPassValidInputFilesNamesAndNonValidSeparator_WithNoExistsFiles_ShouldReturnFalse() throws Exception {
        params.put(Constant.PARAM_INPUT_FILES_NAMES, "1.txt,2.js");
        params.put(Constant.PARAM_INPUT_FILES_NAMES_SEPARATOR, ";");
        
        Future<Boolean> task = fileReaderWorker.start(params);
        
        TestCase.assertEquals(false, task.get().booleanValue());
    }
    
    @Test
    public void whenPassValidParams_WithNoExistsFiles_ShouldReturnFalse() throws Exception {
        params.put(Constant.PARAM_INPUT_FILES_NAMES, "1.txt,2.js");
        params.put(Constant.PARAM_INPUT_FILES_NAMES_SEPARATOR, ",");
        
        Future<Boolean> task = fileReaderWorker.start(params);
        
        TestCase.assertEquals(false, task.get().booleanValue());
    }
}
