package com.respec.training.DTO;

import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ValidationErrorDTO {
    private Map<String, String> validationErrors;
    private HttpStatus status;
    
    public ValidationErrorDTO(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
        this.status = HttpStatus.BAD_REQUEST;
    }
}