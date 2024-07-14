package com.respec.training.specifications;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import com.respec.training.models.DrumCorps;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class DrumCorpsSpecifications implements Specification<DrumCorps> {

    public static Specification<DrumCorps> queryCorpsName(String corpsName) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(root.get("corpsName"), corpsName);
        };
    }

    public static Specification<DrumCorps> queryNumOfChamps(int numOfChamp) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("numOfChamp"), numOfChamp);
        };
    }

    public static Specification<DrumCorps> queryDateFounded(LocalDate dateFounded) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("dateFounded"), dateFounded);
        };
    }
            
    public static Specification<DrumCorps> isFolded(Boolean folded) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("folded"), folded);
        };
    }

    public static Specification<DrumCorps> queryDateFolded(LocalDate dateFolded) {
        return (root, query, criteriaBuilder) -> {
            if (dateFolded == null) return null;
            return criteriaBuilder.equal(root.get("dateFolded"), dateFolded);
        };
    }

    @SuppressWarnings("null")
    @Override
    @Nullable
    public Predicate toPredicate(Root<DrumCorps> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        throw new UnsupportedOperationException("Unimplemented method 'toPredicate'");
    }
}