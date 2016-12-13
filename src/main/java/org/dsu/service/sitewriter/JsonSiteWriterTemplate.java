/**
 * 
 */
package org.dsu.service.sitewriter;

import java.io.IOException;
import java.io.Writer;

import org.dsu.domain.Site;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Writes the site bunch as json to the Writer
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

    private static final String COLLECTION_ID = "collectionId";
    private static final String SITES = "sites";

    private ObjectWriter objectWriter;
    private JsonGenerator jsonGenerator;

    JsonSiteWriterTemplate(Writer writer) throws Exception {
        if (writer == null) {
            throw new IllegalArgumentException("The param 'writer' must not be null .");
        }

        JsonFactory jsonfactory = new JsonFactory();
        jsonGenerator = jsonfactory.createGenerator(writer);

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Site.class, new SiteSerializer());
        mapper.registerModule(module);
        
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        jsonGenerator.setPrettyPrinter(pp);
        objectWriter = mapper.writer(pp);
    }

    @Override
    public SiteWriterTemplate start() throws Exception {
        jsonGenerator.writeStartArray();
        return this;
    }

    @Override
    public SiteWriterTemplate startBunch() throws Exception {
        jsonGenerator.writeStartObject();
        return this;
    }

    @Override
    public SiteWriterTemplate writeCollectionId(String collectionId) throws Exception {
        jsonGenerator.writeStringField(COLLECTION_ID, collectionId);
        return this;
    }

    @Override
    public SiteWriterTemplate startSites() throws Exception {
        jsonGenerator.writeArrayFieldStart(SITES);
        return this;
    }

    @Override
    public SiteWriterTemplate writeSite(Site site) throws Exception {
        objectWriter.writeValue(jsonGenerator, site);
        return this;
    }

    @Override
    public SiteWriterTemplate finishSites() throws Exception {
        jsonGenerator.writeEndArray();
        return this;
    }

    @Override
    public SiteWriterTemplate finishBunch() throws Exception {
        jsonGenerator.writeEndObject();
        return this;
    }

    @Override
    public SiteWriterTemplate finish() throws Exception {
        jsonGenerator.writeEndArray();
        return this;
    }

    @Override
    public void close() throws Exception {
        jsonGenerator.close();        
    }
}
