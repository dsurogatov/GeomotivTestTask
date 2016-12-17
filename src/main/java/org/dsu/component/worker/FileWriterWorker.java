/**
 * 
 */
package org.dsu.component.worker;

import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

import org.dsu.component.ApplicationProperties;
import org.dsu.domain.SiteBunch;
import org.dsu.service.sitewriter.SiteFileWriterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/** The worker writes data to a file.
 *
 */
@Component	
public class FileWriterWorker implements Worker {
	
	private static final Logger LOG = LoggerFactory.getLogger(FileWriterWorker.class);
	
	@Autowired
	private ApplicationProperties appProps;

	@Autowired
	private SiteFileWriterService jsonSiteFileWriterService;

	@Override
	@Async
	public Future<Boolean> start(BlockingQueue<SiteBunch> queue) {
		if(queue == null) {
			LOG.error("The param 'queue' musn't be null.");
			return RETURN_FAIL;
		}
		String outputFileName = appProps.getOutputFileName();
		if(StringUtils.isEmpty(outputFileName)) {
			LOG.error("The name of the output file has not been set.");
			return RETURN_FAIL;
		}
		LOG.debug("The output file is '{}'", outputFileName);
		
		// create file writer
		if(jsonSiteFileWriterService.writeFile(Paths.get(outputFileName), queue)){
			return RETURN_OK;	
		} else {
			return RETURN_FAIL;
		}
		
	}

}
