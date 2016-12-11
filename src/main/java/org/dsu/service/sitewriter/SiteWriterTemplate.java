/**
 * 
 */
package org.dsu.service.sitewriter;

import org.dsu.domain.Site;

/**
 * The interface defines methods to write sites to output.
 *
 */
public interface SiteWriterTemplate {

	/** Starts writing data 
	 * 
	 * @return this
	 * @throws Exception
	 */
	SiteWriterTemplate start() throws Exception;

	/** Starts writing the new sites bunch 
	 * 
	 * @return this 
	 * @throws Exception
	 */
	SiteWriterTemplate startBunch() throws Exception;

	/** Writes the collection id 
	 * 
	 * @param collectionId The id of the collection
	 * @return this
	 * @throws Exception
	 */
	SiteWriterTemplate writeCollectionId(String collectionId) throws Exception;

	/** Starts writing the sites part 
	 * 
	 * @return this
	 * @throws Exception
	 */
	SiteWriterTemplate startSites() throws Exception;

	/** Writes the site 
	 * 
	 * @param site The site instance
	 * @return this
	 * @throws Exception
	 */
	SiteWriterTemplate writeSite(Site site) throws Exception;

	/** Finishes writing the sites part 
	 * 
	 * @return this
	 * @throws Exception
	 */
	SiteWriterTemplate finishSites() throws Exception;

	/** Finishes writing the bunch 
	 * 
	 * @return this
	 * @throws Exception
	 */
	SiteWriterTemplate finishBunch() throws Exception;

	/** Finishes writing 
	 * 
	 * @return this
	 * @throws Exception
	 */
	SiteWriterTemplate finish() throws Exception;
}
