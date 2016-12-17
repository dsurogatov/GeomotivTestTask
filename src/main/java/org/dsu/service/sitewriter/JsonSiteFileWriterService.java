/**
 * 
 */
package org.dsu.service.sitewriter;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.dsu.component.ApplicationProperties;
import org.dsu.domain.Site;
import org.dsu.domain.SiteBunch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * JsonSiteFileWriterService writes data to a JSON file
 * 
 * @author nescafe
 */
@Service
public class JsonSiteFileWriterService implements SiteFileWriterService {

	private static final Logger LOG = LoggerFactory.getLogger(JsonSiteFileWriterService.class);
	private static final String JSON_WRITER_TEMPLATE_BEAN_NAME = "jsonSiteWriterTemplate";
	
	private static void finishWriting(SiteWriterTemplate jsonTemplate, String currentCollectionId) throws Exception {
		if(!StringUtils.isEmpty(currentCollectionId)) {
			jsonTemplate.finishSites().finishBunch();
		}
		jsonTemplate.finish().close();
	}
	
	private static void startNewSiteBunch(SiteWriterTemplate jsonTemplate, String currentCollectionId, String newCollectionId)
	        throws Exception {
		if(!StringUtils.isEmpty(currentCollectionId)) {
			jsonTemplate.finishSites().finishBunch();
		}
		jsonTemplate.startBunch().writeCollectionId(newCollectionId).startSites();
	}
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private ApplicationProperties appProps;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsu.service.sitewriter.SiteFileWriter#writeFile(java.lang.String, java.util.List)
	 */
	@Override
	public boolean writeFile(Path path, BlockingQueue<SiteBunch> queue) {
		if (path == null) {
			LOG.error("The param 'path' must not be null.");
			return false;
		}
		if (queue == null) {
			LOG.error("The param 'queue' must not be null.");
			return false;
		}

		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
		
			SiteWriterTemplate jsonTemplate = (SiteWriterTemplate) 
					applicationContext.getBean(JSON_WRITER_TEMPLATE_BEAN_NAME, writer);
		
			String currentCollectionId = "";
			jsonTemplate.start();
		
			while(true) {
				//LOG.debug("Poll the new bunch of sites...");
				SiteBunch siteBunch = queue.poll(appProps.getProducerConsumerOfferTimeOut(), TimeUnit.MILLISECONDS);
				if(siteBunch == null) {
					LOG.error("The worker's queue is empty then polls data.");
					return false;
				}
		
				if(SiteBunch.POISON == siteBunch) {
					finishWriting(jsonTemplate, currentCollectionId);
					return true;
				}

				String newCollectionId = siteBunch.getCollectionId();
				if(!currentCollectionId.equals(newCollectionId)) {
					startNewSiteBunch(jsonTemplate, currentCollectionId, newCollectionId);
					currentCollectionId = newCollectionId;
				}
			
				for(Site idxSite : siteBunch.getSites()) {
					jsonTemplate.writeSite(idxSite);
				}
		
			}
		} catch (Exception e) {
			LOG.error("Error while is writing file: {}.", path.toString(), e);
		} 
		return false;
	}

}
