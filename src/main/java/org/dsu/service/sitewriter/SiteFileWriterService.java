package org.dsu.service.sitewriter;

import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

import org.dsu.domain.SiteBunch;

/** The interface for writing a list of SiteBunch instances to a file.
 * 
 * @author nescafe
 */
public interface SiteFileWriterService {

	/** Write to the file
	 * 
	 * @param path     A path
	 * @param queue    A queue of bunchies
	 * 
	 * @return         true if data has written
	 */
	boolean writeFile(Path path, BlockingQueue<SiteBunch> queue);
}
