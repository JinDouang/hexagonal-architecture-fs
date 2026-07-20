package com.example.catalogue.product.adapter.in.web;

import com.example.catalogue.product.application.ProductNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    ResponseEntity<ApiError> handleProductNotFound(ProductNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("PRODUCT_NOT_FOUND", exception.getMessage(), Map.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest()
                .body(new ApiError("VALIDATION_ERROR", "Request validation failed", fieldErrors));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException exception) {
        return ResponseEntity.badRequest()
                .body(new ApiError("INVALID_PRODUCT", exception.getMessage(), Map.of()));
    }

    record ApiError(String code, String message, Map<String, String> fieldErrors) {
    }
}