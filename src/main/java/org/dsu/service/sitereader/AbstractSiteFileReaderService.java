/**
 * 
 */
package org.dsu.service.sitereader;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.dsu.component.ApplicationProperties;
import org.dsu.domain.Site;
import org.dsu.domain.SiteBunch;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * Define common methods for ancestors
 * 
 * @author nescafe
 *
 */
public abstract class AbstractSiteFileReaderService implements SiteFileReaderService {

	interface SiteReader {
		boolean readNext() throws Exception;

		Site createSite() throws Exception;
	}

	/**
	 * Check input params
	 * 
	 * @param path The input file.
	 * @param queue The queue for exchanging between workers.
	 * @param LOG The logger, which writes log messages.
	 * 
	 * @return If the path or queue is null or file not exists then return false otherwise return true.
	 */
	static boolean checkInputParams(Path path, BlockingQueue<SiteBunch> queue, Logger logger) {
		if (path == null) {
			logger.error("The parameter 'path' cannot be null.");
			return false;
		}
		if (queue == null) {
			logger.error("The parameter 'queue' cannot be null.");
			return false;
		}
		if (!Files.exists(path)) {
			logger.error("The file {} is not exists.", path.toString());
			return false;
		}
		return true;
	}

	@Autowired
	ApplicationProperties appProps;

	abstract Logger logger();

	boolean read(BlockingQueue<SiteBunch> queue, String collectionId, SiteReader reader) throws Exception {
		List<Site> sites = new ArrayList<>(appProps.getProducerConsumerUnitSize());
		logger().debug("ProducerConsumerUnitSize - '{}'", appProps.getProducerConsumerUnitSize());
		while (reader.readNext()) {
			sites.add(reader.createSite());

			if (sites.size() == appProps.getProducerConsumerUnitSize()) {
				if (!put2queue(queue, sites, collectionId)) {
					return false;
				}
				sites.clear();
				Thread.yield();
			}
		}

		if (!sites.isEmpty() && !put2queue(queue, sites, collectionId)) {
			return false;
		}

		return true;
	}

	Charset getCharset() {
		Charset charset = StandardCharsets.UTF_8;
		if (!StringUtils.isEmpty(appProps.getFileCharset())) {
			try {
				charset = Charset.forName(appProps.getFileCharset());
			} catch (Exception e) {
				logger().warn("Wrong charset name for the input file '{}'. Exception message is '{}'",
				        appProps.getFileCharset(), e.getMessage());
			}
		}
		return charset;
	}

	private boolean put2queue(BlockingQueue<SiteBunch> queue, List<Site> sites, String collectionId)
	        throws InterruptedException {
		//logger().debug("Offer the new bunch of sites...");
		boolean result = queue.offer(new SiteBunch(collectionId, new ArrayList<>(sites)),
		        appProps.getProducerConsumerOfferTimeOut(), TimeUnit.MILLISECONDS);
		if (!result) {
			logger().error("The worker's queue is full then offers data.");
		}
		return result;
	}

}
