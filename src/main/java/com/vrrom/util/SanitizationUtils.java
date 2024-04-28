package com.vrrom.util;

import org.springframework.web.util.HtmlUtils;
import org.apache.commons.lang3.StringUtils;

public class SanitizationUtils {
    public static String sanitizeCarMake(String make) {
        return make.replaceAll("[^a-zA-Z0-9\\s]", "");
    }

    public static String sanitizeInput(String input) {
        String sanitizedInput = HtmlUtils.htmlEscape(input);
        sanitizedInput = StringUtils.replaceEach(sanitizedInput,
                new String[]{"'", "--", ";--", "/*", "*/", "@@"},
                new String[]{"''", "", "", "", "", ""});
        sanitizedInput = StringUtils.trim(StringUtils.substring(sanitizedInput, 0, 100));
        return sanitizedInput;
    }
}
