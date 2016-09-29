/**
 * 
 */
package org.dsu.component.worker;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.Future;

import org.dsu.commom.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
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

    private static String getStringParamByName(Map<String, Object> params, String paramName) {
        if (params == null) {
            LOG.info("The parameter 'params' cannot be null.");
            return null;
        }
        if (StringUtils.isEmpty(paramName)) {
            LOG.info("The parameter 'paramName' cannot be empty.");
            return null;
        }
        if (!params.containsKey(paramName)) {
            LOG.info("The '{}' name has not set.", paramName);
            return null;
        }
        Object param = params.get(paramName);
        if (param == null || !(param instanceof String)) {
            LOG.info("The '{}' name has not set.", paramName);
            return null;
        }

        return (String) param;
    }

    private static Set<Path> getFiles(Map<String, Object> params, String inputFolderName) {
        Set<Path> retSet = new HashSet<>();

        if (params == null) {
            LOG.info("The parameter 'params' cannot be null.");
            return null;
        }
        if (inputFolderName == null) {
            LOG.info("The parameter 'inputFolderName' cannot be null.");
            return null;
        }

        String inputFilesNames = getStringParamByName(params, Constant.PARAM_INPUT_FILES_NAMES);
        if (StringUtils.isEmpty(inputFilesNames)) {
            return retSet;
        }

        String inputFilesNamesSeparator = getStringParamByName(params, Constant.PARAM_INPUT_FILES_NAMES_SEPARATOR);
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

    /*
     * (non-Javadoc)
     * 
     * @see org.dsu.component.worker.Worker#start()
     */
    @Override
    @Async
    public Future<Boolean> start(Map<String, Object> params) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start. Params is '{}'.", params.toString());
        }
        
        String inputFolderName = getStringParamByName(params, Constant.PARAM_INPUT_FOLDER_NAME);
        if (inputFolderName == null) {
            return RETURN_FAIL;
        }

        Set<Path> inputFiles = getFiles(params, inputFolderName);
        if (inputFiles.isEmpty()) {
            return RETURN_FAIL;
        }

        // TODO Auto-generated method stub

        return RETURN_OK;
    }

}
