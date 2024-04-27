package com.vrrom.util;

import org.springframework.web.util.UriComponentsBuilder;

public class UrlBuilder {
    public static String createEncodedUrl(String baseUrl, String pathSegment, String pathSegment2) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .pathSegment(pathSegment)
                .pathSegment(pathSegment2)
                .build()
                .encode().toUriString();
    }
}
