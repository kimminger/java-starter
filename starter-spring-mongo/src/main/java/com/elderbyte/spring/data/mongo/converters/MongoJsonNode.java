package com.elderbyte.spring.data.mongo.converters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.io.IOException;

public class MongoJsonNode {

    @ReadingConverter
    public static class DocumentToJsonNodeConverter implements Converter<Document, JsonNode> {

        private final ObjectMapper mapper;
        private final  ObjectReader jsonNodeReader;

        public DocumentToJsonNodeConverter(ObjectMapper mapper){
            this.mapper = mapper;
            jsonNodeReader = mapper.readerFor(JsonNode.class);
        }

        @Override
        public JsonNode convert(Document doc) {
            try {
                return jsonNodeReader.readValue(doc.toJson());
            } catch (IOException e) {
                throw new RuntimeException("Could not read json node: " + doc.toJson(), e);
            }
        }
    }

    @WritingConverter
    public static class JsonNodeToDocumentConverter implements Converter<JsonNode, Document> {


        private final ObjectMapper mapper;

        public JsonNodeToDocumentConverter(ObjectMapper mapper){
            this.mapper = mapper;
        }

        @Override
        public Document convert(JsonNode source) {

            try {
                var json = mapper.writeValueAsString(source);
                return Document.parse(json);
            }catch (Exception e){
                throw new RuntimeException("Could not write json node: " + source.toString(), e);
            }

        }
    }

}