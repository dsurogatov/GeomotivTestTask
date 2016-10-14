/**
 * 
 */
package org.dsu.component.sitereader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.dsu.common.Constant;
import org.dsu.domain.Site;
import org.dsu.service.sitereader.SiteFileReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
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
        public Future<List<Site>> readFile(String folder, String fileName) {
            return new AsyncResult<List<Site>>(new ArrayList<>());
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
