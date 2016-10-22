package org.dsu;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.dsu.component.ApplicationProperties;
import org.dsu.domain.Site;
import org.dsu.domain.SiteBunch;
import org.dsu.service.sitereader.SiteFileReaderService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class JsonSiteReaderServiceTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Autowired
	private ApplicationProperties properties;

	@Autowired
	private SiteFileReaderService jsonSiteFileReaderService;

	@Before
	public void beforeTest() {
		Mockito.reset(properties);
	}

	@Test
	public void givenFileWithValidData_ReadFile_ShouldReturnTrue()
	        throws InterruptedException, ExecutionException, IOException {
		when(properties.getProducerConsumerUnitSize()).thenReturn(100);

		Path temp = createValidJsonFile();

		BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(1);
		boolean result = jsonSiteFileReaderService.readFile(temp, queue);
		assertEquals(1, queue.size());
		assertTrue(result);

		List<Site> sites = queue.take().getSites();
		// System.out.println(""+sites);
		assertEquals(sites.size(), 3);
		Site site1 = sites.get(0);
		assertEquals(site1.getId(), 13000);
		assertEquals(site1.getName(), "example.com/json1");
		assertEquals(site1.isMobile(), true);
		assertEquals(site1.getScore(), BigDecimal.valueOf(21.003));
	}

	@Test
	public void givenInputFiles150Rows_WhenRead_ThenReturn2SizeQueue() throws Exception {
		when(properties.getProducerConsumerUnitSize()).thenReturn(100);

		StringBuilder content = new StringBuilder(10000);
		content.append("[\n");
		for (int i = 0; i < 150; i++) {
			content.append("{\"site_id\": \"").append("1300").append(i).append("\",");
			content.append(" \"name\": \"example.com/json").append(i).append("\",");
			content.append(" \"mobile\": 1,");
			content.append(" \"score\": 21.003 },\n");
		}
		content.deleteCharAt(content.length() - 1);
		content.deleteCharAt(content.length() - 1);
		content.append("]\n");

		Path temp = folder.newFile("input1.csv").toPath();
		Files.write(temp, content.toString().getBytes());

		BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(10);
		boolean result = jsonSiteFileReaderService.readFile(temp, queue);
		assertEquals(2, queue.size());
		assertEquals(queue.take().getSites().size(), 100);
		assertEquals(queue.take().getSites().size(), 50);
		assertTrue(result);
	}

	@Test
	public void givenMoreDataThanSizeOfQueueWithTimeOut10_WhenRead_ThenReturnFalse() throws Exception {
		when(properties.getProducerConsumerUnitSize()).thenReturn(1);
		when(properties.getProducerConsumerOfferTimeOut()).thenReturn(10);

		Path temp = createValidJsonFile();

		BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(1);
		boolean result = jsonSiteFileReaderService.readFile(temp, queue);

		assertFalse(result);
	}

	@Test
	public void givenMoreDataThanSizeOfQueueWithTimeOut0_WhenRead_ThenReturnFalse() throws Exception {
		when(properties.getProducerConsumerUnitSize()).thenReturn(1);
		when(properties.getProducerConsumerOfferTimeOut()).thenReturn(0);

		Path temp = createValidJsonFile();

		BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(1);
		boolean result = jsonSiteFileReaderService.readFile(temp, queue);

		assertFalse(result);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void givenQueueOfferThrowsException_WhenRead_ThenReturnFalse() throws Exception {
		BlockingQueue<SiteBunch> queue = mock(BlockingQueue.class);
		when(queue.offer(Mockito.any(SiteBunch.class), eq(0l), eq(TimeUnit.MILLISECONDS)))
		        .thenThrow(InterruptedException.class);

		Path temp = createValidJsonFile();

		boolean result = jsonSiteFileReaderService.readFile(temp, queue);

		verify(queue).offer(Mockito.any(SiteBunch.class), eq(0l), eq(TimeUnit.MILLISECONDS));
		assertFalse(result);
	}

	@Test
	public void givenNoValidFileCharset_WhenRead_ThenReturnTrue() throws Exception {
		when(properties.getFileCharset()).thenReturn("a");

		Path temp = createValidJsonFile();

		BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(10);
		boolean result = jsonSiteFileReaderService.readFile(temp, queue);

		assertTrue(result);
	}

	@Test
	public void givenInputFileWithWrongCharset_WhenRead_ThenReturnNoValidSite() throws Exception {
		when(properties.getFileCharset()).thenReturn("CP1251");

		Path temp = folder.newFile("input2.json").toPath();
		String content = "[\n"
		        + "{\"site_id\": \"13000\", \"name\": \"пример.ру/json1\", \"mobile\": 1, \"score\": 21.003 },\n"
		        + "]\n";
		Files.write(temp, content.getBytes());

		BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(10);
		boolean result = jsonSiteFileReaderService.readFile(temp, queue);

		assertTrue(result);

		SiteBunch bunch = queue.take();
		assertEquals(bunch.getCollectionId(), "input1.csv");

		List<Site> sites = bunch.getSites();
		System.out.println("" + sites);
		Site site1 = sites.get(0);
		assertThat(site1.getName(), not(equalTo("пример.ру/json1")));
	}

	@Test
	public void givenFileWithNotValidData_WhenRead_ThenReturnFalse()
	        throws InterruptedException, ExecutionException, IOException {
		File temp = folder.newFile("input2.json");
		String content = "[\n"
		        + "{\"site_id\": \"novalid\", \"name\": \"example.com/json1\", \"mobile\": 1, \"score\": 21 },\n"
		        + "]\n";
		Files.write(Paths.get(temp.getCanonicalPath()), content.getBytes());

		BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(1);
		boolean result = jsonSiteFileReaderService.readFile(temp.toPath(), queue);
		assertEquals(queue.isEmpty(), true);
		assertFalse(result);
	}

	@Test
	public void givenNoExistsFile_WhenRead_ShouldReturnFalse() throws InterruptedException, ExecutionException {
		BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(1);
		boolean result = jsonSiteFileReaderService.readFile(new File("a/b").toPath(), queue);
		assertEquals(queue.isEmpty(), true);
		assertFalse(result);
	}

	@Test
	public void givenNullPath_WhenRead_ShouldReturnFalse() throws InterruptedException, ExecutionException {
		BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(1);
		boolean result = jsonSiteFileReaderService.readFile(null, queue);
		assertEquals(queue.isEmpty(), true);
		assertFalse(result);
	}

	@Test
	public void givenNullQueue_WhenRead_ShouldReturnFalse() throws InterruptedException, ExecutionException {
		boolean result = jsonSiteFileReaderService.readFile(new File("a/b").toPath(), null);
		assertFalse(result);
	}

	private Path createValidJsonFile() throws IOException {
		Path temp = folder.newFile("input2.json").toPath();
		String content = "[\n"
		        + "{\"site_id\": \"13000\", \"name\": \"example.com/json1\", \"mobile\": 1, \"score\": 21.003 },\n"
		        + "{\"site_id\": \"13001\", \"name\": \"example.com/json2\", \"mobile\": 0, \"score\": 97 },\n"
		        + "{\"site_id\": \"13002\", \"name\": \"example.com/json3\", \"mobile\": 0, \"score\": 311 }\n" + "]\n";
		Files.write(temp, content.getBytes());
		return temp;
	}

}
