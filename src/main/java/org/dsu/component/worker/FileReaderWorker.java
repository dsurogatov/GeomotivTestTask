/**
 * 
 */
package org.dsu.component.worker;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

import org.dsu.common.Constant;
import org.dsu.component.ApplicationProperties;
import org.dsu.domain.SiteBunch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * The worker reads data from files
 * 
 * @author nescafe
 */
@Component
class FileReaderWorker implements Worker {

    private static final Logger LOG = LoggerFactory.getLogger(FileReaderWorker.class);

    private static Set<Path> getFiles(String inputFilesNames, String inputFolderName, String inputFilesNamesSeparator) {
        Set<Path> retSet = new HashSet<>();

        if (StringUtils.isEmpty(inputFilesNames) || StringUtils.isEmpty(inputFolderName)) {
            return retSet;
        }

        if (StringUtils.isEmpty(inputFilesNamesSeparator)) {
            inputFilesNamesSeparator = Constant.DEFAULT_PARAM_INPUT_FILES_NAMES_SEPARATOR;
        }

        StringTokenizer tokenizer = new StringTokenizer(inputFilesNames, inputFilesNamesSeparator);
        while (tokenizer.hasMoreTokens()) {
            String fileUri = inputFolderName + File.separatorChar + tokenizer.nextToken();
            LOG.debug("Input file is {}", fileUri);

            Path pathFile = Paths.get(fileUri);
            if (Files.exists(pathFile)) {
                retSet.add(pathFile);
            }
        }

        return retSet;
    }
    
    @Autowired
	ApplicationProperties appProps;

    /*
     * (non-Javadoc)
     * 
     * @see org.dsu.component.worker.Worker#start()
     */
    @Override
    @Async
    public Future<Boolean> start(BlockingQueue<SiteBunch> queue) {
    	if(queue == null) {
    		LOG.info("The variable 'queue' is not set.");
            return RETURN_FAIL;
    	}
    	
        String inputFolderName = appProps.getInputFolderName();
        if (inputFolderName == null) {
        	LOG.info("The variable 'inputFolderName' is not set.");
            return RETURN_FAIL;
        }
        if (!Files.exists(Paths.get(inputFolderName))) {
        	LOG.info("The folder '{}' is not exist.", inputFolderName);
            return RETURN_FAIL;
        }

        String inputFilesNames = appProps.getInputFilesNames();
        String inputFilesNamesSeparator = appProps.getInputFilesNamesSeparator();
        Set<Path> inputFiles = getFiles(inputFilesNames, inputFolderName, inputFilesNamesSeparator);
        if (inputFiles.isEmpty()) {
        	LOG.info("Files '{}' is not exist. The separator is '{}'.", inputFilesNames, inputFilesNamesSeparator);
            return RETURN_FAIL;
        }
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start. The input folder is '{}'. Input files are '{}'.", inputFolderName, inputFilesNames);
        }

        // TODO Auto-generated method stub
        inputFiles.forEach(path -> {
            
        });

        return RETURN_OK;
    }

}
