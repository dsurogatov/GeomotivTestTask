package org.dsu.service.sitereader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.dsu.domain.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.IntNode;

/** JsonSiteFileReaderService reads data from a JSON file
 * 
 * @author nescafe
 */
@Service
class JsonSiteFileReaderService implements SiteFileReaderService {

	/** SiteJsonDeserializer deserializes json data to the Site instance 
	 * 
	 * @author nescafe
	 */
	@SuppressWarnings("serial")
	static class SiteJsonDeserializer extends StdDeserializer<Site> {
		
		private static final String ID_COL = "site_id";
		private static final String NAME_COL = "name";
		private static final String MOBILE_COL = "mobile";
		private static final String SCORE_COL = "score";

		public SiteJsonDeserializer() {
			this(null);
		}

		public SiteJsonDeserializer(Class<?> vc) {
			super(vc);
		}

		@Override
		public Site deserialize(JsonParser jp, DeserializationContext ctxt)
		        throws IOException, JsonProcessingException {
			JsonNode node = jp.getCodec().readTree(jp);
			
			int id = Integer.valueOf(node.get(ID_COL).asText()).intValue();
			String name = node.get(NAME_COL).asText();
			boolean mobile = ((IntNode) node.get(MOBILE_COL)).asBoolean();
			BigDecimal score = new BigDecimal(node.get(SCORE_COL).asText());

			Site site = new Site();
			site.setId(id);
			site.setName(name);
			site.setMobile(mobile);
			site.setScore(score);

			return site;
		}

	}

	private static final Logger LOG = LoggerFactory.getLogger(CsvSiteFileReaderService.class);

	@Override
	@Async
	public Future<List<Site>> readFile(String folder, String fileName) {
		String file = folder + File.separator + fileName;

		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Site.class, new SiteJsonDeserializer());
		mapper.registerModule(module);

		try {
			List<Site> sites = mapper.readValue(new FileReader(file), new TypeReference<List<Site>>() {	});
			return new AsyncResult<List<Site>>(sites);
		} catch (Exception e) {
			LOG.error("Error while was parsing file: {}.", file, e);
		}

		return new AsyncResult<List<Site>>(new ArrayList<>());
	}

}