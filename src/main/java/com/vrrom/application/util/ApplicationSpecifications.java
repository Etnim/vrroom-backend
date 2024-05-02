package com.vrrom.application.util;

import com.vrrom.application.model.Application;
import com.vrrom.application.model.ApplicationStatus;
import com.vrrom.util.SanitizationUtils;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ApplicationSpecifications {
    public static Specification<Application> hasCustomer(Long customerId) {
        return (root, query, cb) -> {
            if (customerId == null) return cb.isTrue(cb.literal(true));
            return cb.equal(root.get("customer").get("id"), customerId);
        };
    }

    public static Specification<Application> hasManager(Long managerId) {
        return (root, query, cb) -> {
            if (managerId == null) return cb.isTrue(cb.literal(true));
            System.out.println(managerId);
            return cb.equal(root.join("manager").get("id"), managerId);
        };
    }

    public static Specification<Application> hasManagerFullName(String fullName) {
        return (root, query, cb) -> {
            if (fullName == null || fullName.isBlank()) {
                return cb.isTrue(cb.literal(true));
            }
            String sanitizedFullName = SanitizationUtils.sanitizeInput(fullName.trim());
            String[] nameParts = sanitizedFullName.split("\\s+");
            int numParts = nameParts.length;
            if (numParts == 1) {
                String part = nameParts[0];
                Predicate namePredicate = cb.like(root.join("manager").get("name"), "%" + part + "%");
                Predicate surnamePredicate = cb.like(root.join("manager").get("surname"), "%" + part + "%");
                return cb.or(namePredicate, surnamePredicate);
            } else {
                String name = nameParts[0];
                String surname = nameParts[numParts - 1];
                Predicate namePredicate = cb.like(root.join("manager").get("name"), "%" + name + "%");
                Predicate surnamePredicate = cb.like(root.join("manager").get("surname"), "%" + surname + "%");
                return cb.and(namePredicate, surnamePredicate);
            }
        };
    }

    public static Specification<Application> hasStatus(ApplicationStatus status) {
        return (root, query, cb) -> {
            if (status == null) return cb.isTrue(cb.literal(true));
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Application> isCreatedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, cb) -> {
            if (startDate == null || endDate == null) return cb.isTrue(cb.literal(true));
            return cb.between(root.get("createdAt"), startDate, endDate);
        };
    }

    public static Specification<Application> isCreatedAfter(LocalDateTime startDate) {
        return (root, query, cb) -> {
            if (startDate == null) return cb.isTrue(cb.literal(true));
            return cb.greaterThanOrEqualTo(root.get("createdAt"), startDate);
        };
    }

    public static Specification<Application> isCreatedBefore(LocalDateTime endDate) {
        return (root, query, cb) -> {
            if (endDate == null) return cb.isTrue(cb.literal(true));
            return cb.lessThanOrEqualTo(root.get("createdAt"), endDate);
        };
    }
}
