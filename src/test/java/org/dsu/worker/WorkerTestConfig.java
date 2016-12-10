package org.dsu.worker;

import org.dsu.component.sitereader.SiteFileReaderServiceLocator;
import org.dsu.component.sitereader.SiteFileReaderServiceLocatorImpl;
import org.dsu.service.sitereader.SiteFileReaderService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * The configuration for testing Worker components
 * 
 * @author nescafe
 */
@Configuration
@ComponentScan({ "org.dsu.component.worker" })
@Profile("test")
public class WorkerTestConfig {

	@Bean
	public SiteFileReaderService csvSiteFileReaderService() {
		return Mockito.mock(SiteFileReaderService.class);
	}
	
	@Bean
	public SiteFileReaderService jsonSiteFileReaderService() {
		return Mockito.mock(SiteFileReaderService.class);
	}
	
	@Bean
	public SiteFileReaderServiceLocator serviceLocator() {
		return new SiteFileReaderServiceLocatorImpl();
	}
}
