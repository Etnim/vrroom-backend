package com.vrrom.application.util;

import com.vrrom.application.model.ApplicationSortParameters;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToApplicationSortParametersConverter implements Converter<String, ApplicationSortParameters> {
    @Override
    public ApplicationSortParameters convert(String source) {
        source = source.trim();
        for (ApplicationSortParameters param : ApplicationSortParameters.values()) {
            if (param.getJsonValue().equalsIgnoreCase(source)) {
                return param;
            }
        }
        throw new IllegalArgumentException("Invalid sort parameter. " + source);
    }
}

