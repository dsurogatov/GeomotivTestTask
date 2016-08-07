/**
 * 
 */
package org.dsu.service.sitewriter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.dsu.domain.Site;
import org.dsu.domain.SiteBunch;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dsu.service.sitewriter.SiteFileWriter#writeFile(java.lang.String, java.util.List)
	 */
	@Override
	public void writeFile(String fileName, List<SiteBunch> siteBunches) throws Exception {
		if (fileName == null || fileName.trim().isEmpty()) {
			throw new IllegalArgumentException("The param 'fileName' musn't be empty.");
		}
		if (siteBunches == null) {
			throw new IllegalArgumentException("The param 'siteBunches' musn't be null.");
		}

		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addSerializer(Site.class, new SiteSerializer());
		mapper.registerModule(module);

		mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), siteBunches);
	}
}
