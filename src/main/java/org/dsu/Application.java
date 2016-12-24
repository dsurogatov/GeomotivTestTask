package org.dsu;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import org.dsu.component.worker.Worker;
import org.dsu.domain.SiteBunch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Application implements CommandLineRunner {

	private static final Logger LOG = LoggerFactory.getLogger(Application.class);
	private static final int EXCHANGE_QUEUE_SIZE = 100;
	
    @Bean
    public Executor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
    
	@Autowired
	private Worker fileReaderWorker;

	@Autowired
	private Worker fileWriterWorker;

	@Override
	public void run(String... args) throws Exception {
		LOG.info("App start ================================== ");
		LOG.info("Args: {}", Arrays.toString(args));
		
		BlockingQueue<SiteBunch> exchangingQueue = new ArrayBlockingQueue<>(EXCHANGE_QUEUE_SIZE);
		Future<Boolean> readerResult = fileReaderWorker.start(exchangingQueue);
		Future<Boolean> writerResult = fileWriterWorker.start(exchangingQueue);
		
		boolean readerResultValue = readerResult.get();
		if(!readerResultValue) {
			writerResult.cancel(true);
		}
		if(Boolean.FALSE.equals(writerResult.get()) || !readerResultValue) {
			throw new Exception("The job has failed.");
		}

		LOG.info("App stop ==================================");
	}

}
