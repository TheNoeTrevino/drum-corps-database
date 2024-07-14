package com.respec.training.DTO;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class PageableErrorDTO {
    private String pageableError;
    private HttpStatus status;
    
    public PageableErrorDTO(String pageableError) {
        this.pageableError = pageableError;
        this.status = HttpStatus.BAD_REQUEST;
    }
}