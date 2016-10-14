package org.dsu.component.sitereader;

import org.dsu.service.sitereader.SiteFileReaderService;

/** Resolves site files readers by a file extension
 * 
 * @author nescafe
 */
public interface SiteFileReaderServiceLocator {

    /** Resolve service by a file extension
     * 
     * @param ext   A value of the file extension
     * 
     * @return      An instance implemented of the SiteFileReaderService interface
     */
    SiteFileReaderService resolve(String ext);
}
