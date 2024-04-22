package com.vrrom.application.mapper;

import org.springframework.data.domain.Sort;

import java.util.Map;

public class SortFieldMapper {
    private static final Map<String, String> sortFieldMappings = Map.of(
            "applicationId", "id",
            "applicationCreatedDate", "createdAt",
            "leasingAmount", "price",
            "applicationStatus", "status"
    );

    public static Sort translateSort(String sortField, String sortDir) {
        String entityField = sortFieldMappings.getOrDefault(sortField, sortField);
        return Sort.by(Sort.Direction.fromString(sortDir.toUpperCase()), entityField);
    }
}
