package org.dsu.component;

import org.dsu.common.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Defines outer application properties
 * 
 * @author nescafe
 */
@Component
public class ApplicationProperties {

    @Value("${" + Constant.PARAM_PRODUCER_CONSUMER_UNIT_SIZE + ":100}")
    private int producerConsumerUnitSize;
    
    @Value("${" + Constant.PARAM_PRODUCER_CONSUMER_OFFER_TIMEOUT + ":1000}")
    private int producerConsumerOfferTimeOut;
    
    @Value("${" + Constant.PARAM_FILES_CHARSET + ":}")
    private String fileCharset;

    /** Gets SiteBunch.sites size for exchanging between reading and writing workers.
     * @return  The value of the unit size
     */
    public int getProducerConsumerUnitSize() {
        return producerConsumerUnitSize;
    }

	/** Gets timeout for BlockingQueue.offer method
	 * @return The value of timeout in milliseconds
	 */
	public int getProducerConsumerOfferTimeOut() {
		return producerConsumerOfferTimeOut;
	}

	/** Gets the input, output files charset.
	 * @return The name of charset
	 */
	public String getFileCharset() {
		return fileCharset;
	}

}
