package com.codegym.specification;

import com.codegym.model.CcdvProfile;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class CcdvProfileSpecs {
    public static Specification<CcdvProfile> nameContains(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return cb.conjunction(); // always true
            }
            String like = "%" + keyword.trim().toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("fullName")), like),
                    cb.like(cb.lower(root.get("user").get("username")), like),
                    cb.like(cb.lower(root.get("user").get("firstName")), like),
                    cb.like(cb.lower(root.get("user").get("lastName")), like),
                    cb.like(cb.lower(root.get("user").get("nickname")), like)
            );
        };
    }

    public static Specification<CcdvProfile> genderEquals(String gender) {
        return (root, query, cb) -> {
            if (gender == null || gender.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("gender"), gender);
        };
    }

    public static Specification<CcdvProfile> cityEquals(String city) {
        return (root, query, cb) -> {
            if (city == null || city.isEmpty()) {
                return cb.conjunction();
            }
            String like = "%" + city.trim().toLowerCase() + "%";
            return cb.like(cb.lower(root.get("city")), like);
        };
    }

    public static Specification<CcdvProfile> ageBetween(Integer ageFrom, Integer ageTo) {
        return (root, query, cb) -> {
            if (ageFrom == null && ageTo == null) {
                return cb.conjunction();
            }

            Integer currentYear = LocalDate.now().getYear();

            if (ageFrom != null && ageTo != null) {
                return cb.between(
                        root.get("yearOfBirth"),
                        currentYear - ageTo,
                        currentYear - ageFrom
                );
            } else if (ageFrom != null) {
                return cb.greaterThanOrEqualTo(
                        root.get("yearOfBirth"),
                        currentYear - ageFrom
                );
            } else {
                return cb.lessThanOrEqualTo(
                        root.get("yearOfBirth"),
                        currentYear - ageTo
                );
            }
        };
    }
}