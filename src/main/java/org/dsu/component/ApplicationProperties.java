package org.dsu.component;

import org.dsu.common.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Defines outer application properties
 * 
 * @author nescafe
 */
@Component
public class ApplicationProperties {

	@Value("${" + Constant.PARAM_INPUT_FOLDER_NAME + ":}")
	private String inputFolderName;

	@Value("${" + Constant.PARAM_INPUT_FILES_NAMES + ":" + Constant.DEFAULT_PARAM_INPUT_FILES_NAMES + "}")
	private String inputFilesNames;

	@Value("${" + Constant.PARAM_INPUT_FILES_NAMES_SEPARATOR + ":" + Constant.DEFAULT_PARAM_INPUT_FILES_NAMES_SEPARATOR + "}")
	private String inputFilesNamesSeparator;

	@Value("${" + Constant.PARAM_PRODUCER_CONSUMER_UNIT_SIZE + ":100}")
	private int producerConsumerUnitSize;

	@Value("${" + Constant.PARAM_PRODUCER_CONSUMER_OFFER_TIMEOUT + ":1000}")
	private int producerConsumerOfferTimeOut;

	@Value("${" + Constant.PARAM_FILES_CHARSET + ":}")
	private String fileCharset;

	/**
	 * Returns SiteBunch.sites size for exchanging between reading and writing workers.
	 * 
	 * @return The value of the unit size
	 */
	public int getProducerConsumerUnitSize() {
		return producerConsumerUnitSize;
	}

	/**
	 * Returns timeout for BlockingQueue.offer method
	 * 
	 * @return The value of timeout in milliseconds
	 */
	public int getProducerConsumerOfferTimeOut() {
		return producerConsumerOfferTimeOut;
	}

	/**
	 * Returns the input, output files charset.
	 * 
	 * @return The name of charset
	 */
	public String getFileCharset() {
		return fileCharset;
	}

	/**
	 * Returns The name of the folder where input files are
	 * 
	 * @return The name of the folder
	 */
	public String getInputFolderName() {
		return inputFolderName;
	}

	/** Returns names of input files, which has been written in a string using the separator char.
	 * @return Names of input files
	 */
	public String getInputFilesNames() {
		return inputFilesNames;
	}

	/** Returns the input files names separator
	 * @return The value of separator.
	 */
	public String getInputFilesNamesSeparator() {
		return inputFilesNamesSeparator;
	}

}
