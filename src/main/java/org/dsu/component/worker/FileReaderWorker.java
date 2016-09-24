/**
 * 
 */
package org.dsu.component.worker;

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

	@Value("${input.files:input1.csv,input2.json}")
	private String inputFilesProperty;
	@Value("${separator.files:,}")
	private String separatorFilesProperty;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsu.component.worker.Worker#start()
	 */
	@Override
	@Async
	public Future<Boolean> start(Map<String, Object> params) {
		String inputFolderName = getFolderName(params);
		if (inputFolderName == null) {
			return RETURN_FAIL;
		}

		LOG.debug("Start. Folder is '{}'. Files are '{}'. Separator is '{}'", inputFolderName, inputFilesProperty,
		        separatorFilesProperty);

		// TODO Auto-generated method stub
		Set<String> fileNames = getFileNamesFromString();
		
		return RETURN_OK;
	}

	private String getFolderName(Map<String, Object> params) {
		if (params == null) {
			LOG.info("The parameter 'params' cannot be null.");
			return null;
		}
		if (!params.containsKey(Constant.PARAM_INPUT_FOLDER_NAME)) {
			LOG.info("The input folder name has not set.");
			return null;
		}
		Object inputFolder = params.get(Constant.PARAM_INPUT_FOLDER_NAME);
		if (inputFolder == null || !(inputFolder instanceof String)) {
			LOG.info("The input folder name has not set.");
			return null;
		}

		String inputFolderName = (String) inputFolder;
		if (Files.notExists(Paths.get(inputFolderName))) {
			LOG.info("The input folder '{}' is not exist.", inputFolderName);
			return null;
		}
		return inputFolderName;
	}

	private Set<String> getFileNamesFromString() {
		Set<String> retSet = new HashSet<>();
		if (StringUtils.isEmpty(inputFilesProperty) || StringUtils.isEmpty(separatorFilesProperty)) {
			LOG.info("The 'inputFilesProperty' or 'separatorFilesProperty' is empty.");
			return retSet;
		}

		StringTokenizer tokenizer = new StringTokenizer(inputFilesProperty, separatorFilesProperty);
		while (tokenizer.hasMoreTokens()) {
			retSet.add(tokenizer.nextToken());
		}
		return retSet;
	}

}
