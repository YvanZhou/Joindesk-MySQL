package com.ariseontech.joindesk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static String serialize(Object target) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(target);
        } catch (Exception e) {
            logger.error("serialize error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static <T> T deserialize(String input, TypeReference<T> valueTypeRef) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(input, valueTypeRef);
        } catch (Exception e) {
            logger.error("deserialize error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
