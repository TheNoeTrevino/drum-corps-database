package com.respec.training.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.respec.training.DTO.NotFoundDTO;
import com.respec.training.DTO.PageableErrorDTO;
import com.respec.training.DTO.ValidationErrorDTO;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @SuppressWarnings("null")
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatusCode status,
                                                                WebRequest request) {

        // Format errors
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO(errors);
        return new ResponseEntity<Object>(validationErrorDTO, status);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<NotFoundDTO> handleNotFoundException(NotFoundException ex) {

        NotFoundDTO notFoundDTO = new NotFoundDTO(ex.getMessage());
        return new ResponseEntity<NotFoundDTO>(notFoundDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PageableException.class)
    protected ResponseEntity<PageableErrorDTO> handlePageableException(PageableException ex) {

        PageableErrorDTO pageableErrorDTO = new PageableErrorDTO(ex.getMessage());
        return new ResponseEntity<PageableErrorDTO>(pageableErrorDTO, HttpStatus.BAD_REQUEST);
    }
}