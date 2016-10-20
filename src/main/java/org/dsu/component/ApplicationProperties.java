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

    /** Defines SiteBunch.sites size for exchanging between reading and writing workers.
     * @return  A value of the unit size
     */
    public int getProducerConsumerUnitSize() {
        return producerConsumerUnitSize;
    }
    
}
