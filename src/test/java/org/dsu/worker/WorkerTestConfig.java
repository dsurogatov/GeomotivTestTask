package org.dsu.worker;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/** The configuration for testing Worker components
 * 
 * @author nescafe
 */
@Configuration
@ComponentScan({ "org.dsu.component.worker" })
@Profile("test")
public class WorkerTestConfig {

}
