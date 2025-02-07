package com.Fawrybook.Fawrybook.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TextHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private TextHelper() {

    }

    public static String extractPlainText(String text) {
        try {
            JsonNode jsonNode = objectMapper.readTree(text);
            if (jsonNode.has("text")) {
                return jsonNode.get("text").asText();
            }
        } catch (Exception ignored) {
            System.out.println("error in text");
        }
        return text;
    }
}
