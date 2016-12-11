/**
 * 
 */
package org.dsu.service.sitewriter;

import java.io.IOException;
import java.io.Writer;

import org.dsu.domain.Site;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Writes the site bunch as json
 *
 */
@Component
@Scope("prototype")
class JsonSiteWriterTemplate implements SiteWriterTemplate {

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

	private static final String SQUARE_BRACKET = "[";
	private static final String SQUARE_BRACKET_CLOSE = "]";
	private static final String CURLY_BRACKET = "{";
	private static final String CURLY_BRACKET_CLOSE = "}";
	private static final String SPACE = " ";
	private static final String D_SPACE = "  ";
	private static final String CRLF = "\n";
	private static final String COMMA = ",";
	private static final String COLLECTION_ID = "collectionId";
	private static final String SITES = "sites";

	private Writer writer;
	private ObjectWriter objectWriter;
	private Site bufferedSite;

	JsonSiteWriterTemplate(Writer writer) {
		if (writer == null) {
			throw new IllegalArgumentException("The param 'writer' must not be this.");
		}
		this.writer = writer;

		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addSerializer(Site.class, new SiteSerializer());
		mapper.registerModule(module);
		DefaultPrettyPrinter pp = new DefaultPrettyPrinter().withObjectIndenter(new DefaultIndenter(D_SPACE, DefaultIndenter.SYS_LF));
		objectWriter = mapper.writer(pp);
	}

	@Override
	public SiteWriterTemplate start() throws Exception {
		writer.write(SQUARE_BRACKET);
		writer.write(SPACE);
		return this;
	}

	@Override
	public SiteWriterTemplate startBunch() throws Exception {
		writer.write(CURLY_BRACKET);
		writer.write(CRLF);
		return this;
	}

	@Override
	public SiteWriterTemplate writeCollectionId(String collectionId) throws Exception {
		writer.write(D_SPACE);
		writeAttributeWithValue(COLLECTION_ID, collectionId);
		writer.write(CRLF);
		return this;
	}

	@Override
	public SiteWriterTemplate startSites() throws Exception {
		writer.write(D_SPACE);
		writer.write(String.format("\"%s\" : [ ", SITES));
		return this;
	}

	@Override
	public SiteWriterTemplate writeSite(Site site) throws Exception {
		if (bufferedSite != null) {
			writeSite2Stream(bufferedSite, true);
		}
		bufferedSite = site;
		return this;
	}

	@Override
	public SiteWriterTemplate finishSites() throws Exception {
		if (bufferedSite != null) {
			writeSite2Stream(bufferedSite, false);
			writer.write(SPACE);
		}
		writer.write(SQUARE_BRACKET_CLOSE);
		writer.write(CRLF);
		return this;
	}

	@Override
	public SiteWriterTemplate finishBunch() throws Exception {
		writer.write(CURLY_BRACKET_CLOSE);
		return this;
	}

	@Override
	public SiteWriterTemplate finish() throws Exception {
		writer.write(SPACE);
		writer.write(SQUARE_BRACKET_CLOSE);
		return this;
	}

	private void writeAttributeWithValue(String attribute, String value) throws IOException {
		writer.write(String.format("\"%s\" : \"%s\",", attribute, value));
	}

	private void writeSite2Stream(Site site, boolean addComma) throws JsonProcessingException, IOException {
		//String jsonSite = mapper.writer(pp).writeValueAsString(site);
		String jsonSite = objectWriter.writeValueAsString(site);
		writer.write(jsonSite.replaceAll("[\\n\\r]+", "\n" + D_SPACE));
		if (addComma) {
			writer.write(COMMA);
			writer.write(SPACE);
		}
	}
}
