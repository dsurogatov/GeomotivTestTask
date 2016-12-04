package org.dsu.worker;

import org.dsu.TestConfig;
import org.dsu.component.sitereader.SiteFileReaderServiceLocator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class SiteFileReaderServiceLocatorTest {
    
    @Autowired
    private SiteFileReaderServiceLocator locator;

    @Test
    public void whenPassNullExt_ThenReturnService() {
        TestCase.assertNotNull(locator.resolve(null));
    }

    @Test
    public void whenPassEmptyExt_ThenReturnService() {
        TestCase.assertNotNull(locator.resolve(""));
    }

    @Test
    public void whenPassValidExt_ThenReturnService() {
        TestCase.assertNotNull(locator.resolve("ext"));
    }
}
