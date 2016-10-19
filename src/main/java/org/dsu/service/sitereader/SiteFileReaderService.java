package org.dsu.service.sitereader;

import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

import org.dsu.domain.SiteBunch;

/**
 * The interface for file readers, which reads raw data in different formats into Site instances.
 * 
 * @author nescafe
 */
public interface SiteFileReaderService {

	/** Read data from an input file and convert it to the list of Site instanties
	 * 
	 * @param path    A path instance where the input file exists.
	 * 
	 * @return        true if the input file was read.
	 */
	boolean readFile(Path path, BlockingQueue<SiteBunch> queue);
}
