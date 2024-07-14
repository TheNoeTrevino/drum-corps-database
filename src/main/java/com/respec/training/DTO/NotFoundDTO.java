package com.respec.training.DTO;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class NotFoundDTO {
    private String notFoundError;
    private HttpStatus status;
    
    public NotFoundDTO(String notFoundError) {
        this.notFoundError = notFoundError;
        this.status = HttpStatus.NOT_FOUND;
    }
}