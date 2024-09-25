package org.example.httpserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;

public class Json {

    private static final ObjectMapper myobjectMapper = defaultObject();

    private static ObjectMapper defaultObject() {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return om;
    }

    public static JsonNode parse(String jsonSrc) throws IOException {
        return myobjectMapper.readTree(jsonSrc);
    }

    public static <A> A fromJson(JsonNode node, Class<A> clazz) throws JsonProcessingException {
        return myobjectMapper.treeToValue(node, clazz);
    }

    public static JsonNode toJson(Object object) {
        return myobjectMapper.valueToTree(object);
    }

    public static String stringify(JsonNode node) throws JsonProcessingException {
        return generateJson(node, false);
    }

    public static String stringifyPretty(JsonNode node) throws JsonProcessingException {
        return generateJson(node, true);
    }

    private static String generateJson(Object object, boolean pretty) throws JsonProcessingException {
        ObjectWriter objectWriter = myobjectMapper.writer();

        if(pretty) {
            objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        }

        return objectWriter.writeValueAsString(object);
    }

}
