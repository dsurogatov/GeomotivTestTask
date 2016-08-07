package org.dsu.service.sitereader;

import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.dsu.domain.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.ParseBigDecimal;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

/** JsonSiteFileReaderService reads data from a CSV file
 * 
 * @author nescafe
 */
@Service
class CsvSiteFileReaderService implements SiteFileReaderService {

	private static final Logger LOG = LoggerFactory.getLogger(CsvSiteFileReaderService.class);
	
	private static final String ID_COL = "id";
	private static final String NAME_COL = "name";
	private static final String MOBILE_COL = "is mobile";
	private static final String SCORE_COL = "score";

	@Override
	@Async
	public Future<List<Site>> readFile(String folder, String fileName) {
		String file = folder + File.separator + fileName;

		try (ICsvMapReader csvMapReader = new CsvMapReader(new FileReader(file), CsvPreference.STANDARD_PREFERENCE)) {
			final String[] headers = csvMapReader.getHeader(true);
			final CellProcessor[] processors = getProcessors();

			Map<String, Object> fieldsInCurrentRow;
			List<Site> sites = new ArrayList<>();
			while ((fieldsInCurrentRow = csvMapReader.read(headers, processors)) != null) {
				sites.add(createSite(fieldsInCurrentRow));
			}
			return new AsyncResult<List<Site>>(sites);
		} catch (Exception e) {
			LOG.error("Error while was parsing file: {}.", file, e);
		}

		return new AsyncResult<List<Site>>(new ArrayList<>());
	}

	private Site createSite(Map<String, Object> fieldsInCurrentRow) {
		Site site = new Site();
		site.setId((int) fieldsInCurrentRow.get(ID_COL));
		site.setName((String) fieldsInCurrentRow.get(NAME_COL));
		site.setMobile((boolean) fieldsInCurrentRow.get(MOBILE_COL));
		site.setScore((BigDecimal) fieldsInCurrentRow.get(SCORE_COL));
		return site;
	}

	/**
	 * Set up the processors used for parsing the file.
	 */
	private static CellProcessor[] getProcessors() {
		final CellProcessor[] processors = new CellProcessor[] { new NotNull(new ParseInt()), // id
		        new NotNull(), // name
		        new NotNull(new ParseBool()), // mobile
		        new NotNull(new ParseBigDecimal()) // score
		};
		return processors;
	}

}
