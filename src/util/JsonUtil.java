package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
    public static String readBody(InputStream inputStream) throws IOException {
        StringBuilder body = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        )) {
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        }

        return body.toString();
    }

    public static Map<String, String> parseJsonObject(String json) {
        Map<String, String> values = new HashMap<>();

        if (json == null || json.trim().isEmpty()) {
            return values;
        }

        String content = json.trim();
        if (content.startsWith("{")) {
            content = content.substring(1);
        }
        if (content.endsWith("}")) {
            content = content.substring(0, content.length() - 1);
        }

        for (String pair : splitTopLevel(content)) {
            String[] parts = pair.split(":", 2);
            if (parts.length != 2) {
                continue;
            }

            String key = unquote(parts[0].trim());
            String value = unquote(parts[1].trim());
            values.put(key, value);
        }

        return values;
    }

    public static String quote(String value) {
        if (value == null) {
            return "null";
        }

        return "\"" + value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t") + "\"";
    }

    private static String[] splitTopLevel(String content) {
        java.util.ArrayList<String> parts = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inString = false;
        boolean escaping = false;

        for (int i = 0; i < content.length(); i++) {
            char character = content.charAt(i);

            if (escaping) {
                current.append(character);
                escaping = false;
                continue;
            }

            if (character == '\\') {
                current.append(character);
                escaping = true;
                continue;
            }

            if (character == '"') {
                inString = !inString;
                current.append(character);
                continue;
            }

            if (character == ',' && !inString) {
                parts.add(current.toString());
                current.setLength(0);
                continue;
            }

            current.append(character);
        }

        if (current.length() > 0) {
            parts.add(current.toString());
        }

        return parts.toArray(new String[0]);
    }

    private static String unquote(String value) {
        String result = value.trim();

        if (result.startsWith("\"") && result.endsWith("\"") && result.length() >= 2) {
            result = result.substring(1, result.length() - 1);
        }

        return result
                .replace("\\\"", "\"")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\\", "\\");
    }
}
