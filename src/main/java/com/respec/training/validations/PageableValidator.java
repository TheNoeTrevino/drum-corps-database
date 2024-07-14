package com.respec.training.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.respec.training.exceptions.PageableException;

@Component
public class PageableValidator implements ConstraintValidator<PageableConstraint, Pageable> {

    private int maxPerPage;

    @Override
    public void initialize(PageableConstraint constraintAnnotation) {

        maxPerPage = constraintAnnotation.maxPerPage(); // Getting this from the controller method
    }

    @Override
    public boolean isValid(Pageable pageable, ConstraintValidatorContext context) {

        if (pageable == null) {
            return true; // If no page parameters provided, skip it
        }

        boolean isValid = true;

        if (pageable.getPageSize() > maxPerPage) {
            isValid = false;
            throw new PageableException("Page size must be less than or equal to " + maxPerPage);
        }

        return isValid;
    }
}