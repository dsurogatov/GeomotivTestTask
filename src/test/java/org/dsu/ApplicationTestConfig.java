package org.dsu;

import org.dsu.component.worker.Worker;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class ApplicationTestConfig {

	@Bean
	public Application app() {
		return new Application();
	}
	
	@Bean
	public Worker fileReaderWorker() {
		return Mockito.mock(Worker.class);
	}
	
	@Bean
	public Worker fileWriterWorker() {
		return Mockito.mock(Worker.class);
	}
}
