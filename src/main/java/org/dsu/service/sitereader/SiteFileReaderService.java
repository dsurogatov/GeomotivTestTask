package org.dsu.service.sitereader;

import java.util.List;
import java.util.concurrent.Future;

import org.dsu.domain.Site;

/**
 * The interface for file readers, which reads raw data in different formats into Site instances.
 * 
 * @author nescafe
 */
public interface SiteFileReaderService {

	/** Read data from an input file and convert it to the list of Site instanties
	 * 
	 * @param folder      A path to folder where the input file exists 
	 * @param fileName    A name of input file
	 * 
	 * @return A list of Site instanties. If the input file not exists or can't read 
	 * 		   or happened an error, it will return the empty list.
	 */
	Future<List<Site>> readFile(String folder, String fileName);
}
