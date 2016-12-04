/**
 * 
 */
package org.dsu.component.sitereader;

import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

import org.dsu.common.Constant;
import org.dsu.domain.SiteBunch;
import org.dsu.service.sitereader.SiteFileReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Resolves site file readers by a file extension. Uses predefined implementations of file readers
 * 
 * @author nescafe
 */
@Component
public class SiteFileReaderServiceLocatorImpl implements SiteFileReaderServiceLocator {

    private final static SiteFileReaderService EMPTY_SITE_FILE_READER_SERVICE = new SiteFileReaderService() {

        @Override
        public boolean readFile(Path path, BlockingQueue<SiteBunch> queue) {
            return true;
        }
    };

    @Autowired
    private SiteFileReaderService jsonSiteFileReaderService;

    @Autowired
    private SiteFileReaderService csvSiteFileReaderService;

    @Override
    public SiteFileReaderService resolve(String ext) {
        if (StringUtils.isEmpty(ext)) {
            return EMPTY_SITE_FILE_READER_SERVICE;
        }

        switch (ext) {
        case Constant.FILE_EXT_CSV:
            return csvSiteFileReaderService;
        case Constant.FILE_EXT_JSON:
            return jsonSiteFileReaderService;
        }
        return EMPTY_SITE_FILE_READER_SERVICE;
    }

}
