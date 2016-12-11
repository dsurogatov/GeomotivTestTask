/**
 * 
 */
package org.dsu.service.sitewriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

import org.dsu.domain.Site;
import org.dsu.domain.SiteBunch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * JsonSiteFileWriterService writes data to a JSON file
 * 
 * @author nescafe
 */
@Service
public class JsonSiteFileWriterService implements SiteFileWriterService {

	/**
	 * SiteJsonSerializer serializes json data to the json format
	 * 
	 * @author nescafe
	 */
	@SuppressWarnings("serial")
	static class SiteSerializer extends StdSerializer<Site> {

		public SiteSerializer() {
			this(null);
		}

		public SiteSerializer(Class<Site> t) {
			super(t);
		}

		@Override
		public void serialize(Site value, JsonGenerator jgen, SerializerProvider provider)
		        throws IOException, JsonProcessingException {
			jgen.writeStartObject();
			jgen.writeStringField("id", String.valueOf(value.getId()));
			jgen.writeStringField("name", value.getName());
			jgen.writeNumberField("mobile", value.isMobile() ? 1 : 0);
			jgen.writeStringField("keywords", value.getKeywords());
			jgen.writeNumberField("score", value.getScore());
			jgen.writeEndObject();
		}
	}
	
	private static final Logger LOG = LoggerFactory.getLogger(JsonSiteFileWriterService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsu.service.sitewriter.SiteFileWriter#writeFile(java.lang.String, java.util.List)
	 */
	@Override
	public boolean writeFile(Path path, BlockingQueue<SiteBunch> queue) {
		if (path == null) {
			LOG.error("The param 'path' musn't be null.");
			return false;
		}
		if (queue == null) {
			LOG.error("The param 'queue' musn't be null.");
			return false;
		}

		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addSerializer(Site.class, new SiteSerializer());
		mapper.registerModule(module);

		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			mapper.writerWithDefaultPrettyPrinter().writeValue(writer, queue);
			return true;
		} catch (Exception e) {
			LOG.error("Error while is writing file: {}.", path.toString(), e);
		} 
		return false;
	}
}
