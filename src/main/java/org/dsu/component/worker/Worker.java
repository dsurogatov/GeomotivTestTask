package org.dsu.component.worker;

import java.util.concurrent.Future;

/** The Worker interface provides a method for starting 
 *      an asynchronous task in Spring components.
 * 
 * @author nescafe
 */
public interface Worker {

    /** Start a task.
     * 
     * @return An instance of the Future class  
     *         If one's method get return true, when the task has finished without errors,
     *         otherwise a problem has happened. 
     */
    Future<Boolean> start();
}
