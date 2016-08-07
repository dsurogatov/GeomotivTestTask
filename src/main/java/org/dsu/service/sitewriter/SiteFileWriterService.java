package org.dsu.service.sitewriter;

import java.util.List;

import org.dsu.domain.SiteBunch;

/** The interface for writing a list of SiteBunch instances to a file.
 * 
 * @author nescafe
 */
public interface SiteFileWriterService {

	/** Write to the file
	 * 
	 * @param fileName 		 A file name
	 * @param siteBunches    A list of bunchies
	 * 
	 * @exception   If an error occurs
	 */
	void writeFile(String fileName, List<SiteBunch> siteBunches) throws Exception;
}
