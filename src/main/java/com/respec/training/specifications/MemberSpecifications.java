package com.respec.training.specifications;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import com.respec.training.models.DrumCorps;
import com.respec.training.models.Member;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class MemberSpecifications implements Specification<Member> {

    public static Specification<Member> queryFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(root.get("firstName"), firstName);
        };
    }

    public static Specification<Member> queryLastName(String lastName) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(root.get("lastName"), lastName);
        };
    }

    public static Specification<Member> queryAge(int age) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("age"), age);
        };
    }

    public static Specification<Member> queryBirthdate(LocalDate birthDate) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("birthDate"), birthDate);
        };
    }
            
    public static Specification<Member> querySection(String section) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(root.get("section"), section);
        };
    }

    public static Specification<Member> queryInstrument(String instrument) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(root.get("instrument"), instrument);
        };
    }

    public static Specification<Member> queryEmailAddress(String emailAddress) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("emailAddress"), emailAddress);
        };
    }

    public static Specification<Member> queryDrumCorps(DrumCorps drumCorps) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("drumCorps"), drumCorps);
        };
    }

    @SuppressWarnings("null")
    @Override
    @Nullable
    public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        throw new UnsupportedOperationException("Unimplemented method 'toPredicate'");
    }
}