package org.dsu;

import static org.mockito.Mockito.*;
import static junit.framework.TestCase.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

import org.dsu.component.ApplicationProperties;
import org.dsu.domain.Site;
import org.dsu.domain.SiteBunch;
import org.dsu.service.sitereader.SiteFileReaderService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class CsvSiteReaderServiceTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Autowired
    private ApplicationProperties properties;

    @Autowired
    private SiteFileReaderService csvSiteFileReaderService;

    @Test
    public void givenFileWithValidData_ReadFile_ShouldReturnTrue()
            throws InterruptedException, ExecutionException, IOException {
        when(properties.getProducerConsumerUnitSize()).thenReturn(100);
        
        File temp = folder.newFile("input1.csv");
        String content = "id,name,is mobile,score\n" + "12000,example.com/csv1,true,454.23\n"
                + "12001,example.com/csv2,true,128\n" + "12002,example.com/csv3,false,522\n";
        Files.write(temp.toPath(), content.getBytes());

        BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(1);
        boolean result = csvSiteFileReaderService.readFile(temp.toPath(), queue);
        assertEquals(queue.size(), 1);
        assertTrue(result);

        SiteBunch bunch = queue.take();
        assertEquals(bunch.getCollectionId(), "input1.csv");
        
        List<Site> sites = bunch.getSites();
        // System.out.println(""+sites);
        Site site1 = sites.get(0);
        assertEquals(site1.getId(), 12000);
        assertEquals(site1.getName(), "example.com/csv1");
        assertEquals(site1.isMobile(), true);
        assertEquals(site1.getScore(), BigDecimal.valueOf(454.23));
    }

    // TODO write test for 100 sites in one bunch
    @Test
    public void givenInputFiles150Rows_WhenRead_ThenReturn2SizeQueue() throws Exception {
        when(properties.getProducerConsumerUnitSize()).thenReturn(100);
        
        StringBuilder content = new StringBuilder(10000);
        content.append("id,name,is mobile,score\n");
        for (int i = 0; i < 150; i++) {
            content.append("1200").append(i).append(',');
            content.append("example.com/csv").append(i).append(',');
            content.append("true").append(',');
            content.append(i).append('\n');
        }
        
        Path temp = folder.newFile("input1.csv").toPath();
        Files.write(temp, content.toString().getBytes());
        
        BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(10);
        boolean result = csvSiteFileReaderService.readFile(temp, queue);
        assertEquals(queue.size(), 2);
        assertEquals(queue.take().getSites().size(), 100);
        assertEquals(queue.take().getSites().size(), 50);
        assertTrue(result);
    }
    
    // TODO run test on timeout put in queue
    // run test for throw interaption exception by offer method

    @Test
    public void givenFileWithNoValidData_WhenRead_ThenReturnFalse()
            throws InterruptedException, ExecutionException, IOException {
        File temp = folder.newFile("input1.csv");
        String content = "id,name,is mobile,score\n" + "12000example.com/csv1,true,454\n"
                + "12001,example.com/csv2,true,128\n" + "12002,example.com/csv3,false,522\n";
        Files.write(temp.toPath(), content.getBytes());

        BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(1);
        boolean result = csvSiteFileReaderService.readFile(temp.toPath(), queue);
        assertEquals(queue.isEmpty(), true);
        assertFalse(result);
    }

    @Test
    public void givenNoExistsFile_WhenRead_ShouldReturnFalse() throws InterruptedException, ExecutionException {
        BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(1);
        boolean result = csvSiteFileReaderService.readFile(new File("a/b").toPath(), queue);
        assertEquals(queue.isEmpty(), true);
        assertFalse(result);
    }

    @Test
    public void givenNullPath_WhenRead_ShouldReturnFalse() throws InterruptedException, ExecutionException {
        BlockingQueue<SiteBunch> queue = new ArrayBlockingQueue<>(1);
        boolean result = csvSiteFileReaderService.readFile(null, queue);
        assertEquals(queue.isEmpty(), true);
        assertFalse(result);
    }

    @Test
    public void givenNullQueue_WhenRead_ShouldReturnFalse() throws InterruptedException, ExecutionException {
        boolean result = csvSiteFileReaderService.readFile(new File("a/b").toPath(), null);
        assertFalse(result);
    }
}
