package org.dsu.component.worker;

import java.util.Map;
import java.util.concurrent.Future;

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
     * @param params A map of parameters for processing a task.
     * 
     * @return An instance of the Future class  
     *         If Future.get return true, when the task has finished without errors,
     *         otherwise a problem has happened. 
     */
    Future<Boolean> start(Map<String, Object> params);
}
