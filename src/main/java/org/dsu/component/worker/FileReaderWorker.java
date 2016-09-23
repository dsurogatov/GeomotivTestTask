/**
 * 
 */
package org.dsu.component.worker;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

/** The worker reads data from files 
 * 
 * @author nescafe
 */
@Component
class FileReaderWorker implements Worker {
    
    private static final Logger LOG = LoggerFactory.getLogger(FileReaderWorker.class);
    
    @Value("${test}")
    private String inputFilesProperty;
    private String separatorFilesProperty;

    /* (non-Javadoc)
     * @see org.dsu.component.worker.Worker#start()
     */
    @Override
    @Async
    public Future<Boolean> start() {
        LOG.debug("Start. Thread is '{}'. Files are '{}'.", Thread.currentThread().getName(), inputFilesProperty);
        // TODO Auto-generated method stub
        return new AsyncResult<Boolean>(Boolean.FALSE);
    }

}
