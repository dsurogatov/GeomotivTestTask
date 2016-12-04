package org.dsu.component.worker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

import org.dsu.domain.SiteBunch;
import org.springframework.scheduling.annotation.AsyncResult;

/** The Worker interface provides a method for starting 
 *      an asynchronous task in Spring components.
 * 
 * @author nescafe
 */
public interface Worker {
	
	static final Future<Boolean> RETURN_FAIL = new AsyncResult<Boolean>(Boolean.FALSE);
	static final Future<Boolean> RETURN_OK = new AsyncResult<Boolean>(Boolean.TRUE);

    /** Start a task.
     * 
     * @param queue The buffer is used for exchanging data. 
     * 
     * @return An instance of the Future class  
     *         If Future.get return true, when the task has finished without errors,
     *         otherwise a problem has happened. 
     */
    Future<Boolean> start(BlockingQueue<SiteBunch> queue);
}
