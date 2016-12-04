/**
 * 
 */
package org.dsu.common;

/** Defines application's constants
 * 
 * @author nescafe
 */
public final class Constant {
	
	public static final String PARAM_INPUT_FOLDER_NAME = "input.folder";
	public static final String PARAM_INPUT_FILES_NAMES = "input.files";
	public static final String PARAM_INPUT_FILES_NAMES_SEPARATOR = "input.files.separator";
	public static final String PARAM_PRODUCER_CONSUMER_UNIT_SIZE = "producer.consumer.unit.size";
	public static final String PARAM_PRODUCER_CONSUMER_OFFER_TIMEOUT = "producer.consumer.offer.timeout";
	public static final String PARAM_FILES_CHARSET = "files.charset";

	public static final String DEFAULT_PARAM_INPUT_FILES_NAMES = "input1.csv,input2.json";
	public static final String DEFAULT_PARAM_INPUT_FILES_NAMES_SEPARATOR = ",";
	
	public static final String FILE_EXT_CSV = "csv";
	public static final String FILE_EXT_JSON = "json";

	private Constant() {
		
	}
}
