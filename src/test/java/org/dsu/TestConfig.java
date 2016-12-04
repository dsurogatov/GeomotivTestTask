/**
 * 
 */
package org.dsu;

import org.dsu.component.ApplicationProperties;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * @author nescafe Spring configuration for testing purporses
 */
@Configuration
@ComponentScan({ "org.dsu.service" })
@Profile("test")
public class TestConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public ApplicationProperties appProps() {
	    return Mockito.mock(ApplicationProperties.class);
	}
}
