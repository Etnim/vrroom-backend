package com.vrrom.application.util;

import com.vrrom.application.model.Application;
import com.vrrom.application.model.ApplicationStatus;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;

public class ApplicationSpecifications {

    public static Specification<Application> hasManager(Long managerId) {
        return (root, query, cb) -> {
            if (managerId == null) return cb.isTrue(cb.literal(true));
            System.out.println(managerId);
            return cb.equal(root.join("manager").get("id"), managerId);
        };
    }

    public static Specification<Application> hasStatus(ApplicationStatus status) {
        return (root, query, cb) -> {
            if (status == null) return cb.isTrue(cb.literal(true));
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Application> isCreatedBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            if (startDate == null || endDate == null) return cb.isTrue(cb.literal(true));
            return cb.between(root.get("createdAt"), startDate, endDate);
        };
    }

    public static Specification<Application> isCreatedAfter(LocalDate startDate) {
        return (root, query, cb) -> {
            if (startDate == null) return cb.isTrue(cb.literal(true));
            return cb.greaterThanOrEqualTo(root.get("createdAt"), startDate);
        };
    }
    public static Specification<Application> isCreatedBefore(LocalDate endDate) {
        return (root, query, cb) -> {
            if (endDate == null) return cb.isTrue(cb.literal(true));
            return cb.lessThanOrEqualTo(root.get("createdAt"), endDate);
        };
    }
}
