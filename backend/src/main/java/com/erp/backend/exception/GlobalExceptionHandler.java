package com.erp.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 404 - Resource Not Found
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {

		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage(),
				request.getRequestURI());

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	// 409 - Duplicate Resource
	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<ApiError> handleDuplicate(DuplicateResourceException ex, HttpServletRequest request) {

		ApiError error = new ApiError(HttpStatus.CONFLICT.value(), "Conflict", ex.getMessage(),
				request.getRequestURI());

		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

	// 400 - Business Validation
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiError> handleBusinessValidation(BusinessException ex, HttpServletRequest request) {

		ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), "Bad Request", ex.getMessage(),
				request.getRequestURI());

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	// 500 - Generic
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest request) {

		ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
				"Something went wrong. Please contact support.", request.getRequestURI());

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidationErrors(
	        org.springframework.web.bind.MethodArgumentNotValidException ex,
	        jakarta.servlet.http.HttpServletRequest request) {

	    StringBuilder errors = new StringBuilder();

	    ex.getBindingResult().getFieldErrors().forEach(error -> {
	        errors.append(error.getField())
	              .append(": ")
	              .append(error.getDefaultMessage())
	              .append("; ");
	    });

	    ApiError apiError = new ApiError(
	            HttpStatus.BAD_REQUEST.value(),
	            "Validation Failed",
	            errors.toString(),
	            request.getRequestURI()
	    );

	    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
	public ResponseEntity<ApiError> handleInvalidJson(
	        org.springframework.http.converter.HttpMessageNotReadableException ex,
	        jakarta.servlet.http.HttpServletRequest request) {

	    ApiError error = new ApiError(
	            HttpStatus.BAD_REQUEST.value(),
	            "Malformed JSON",
	            "Invalid request body format",
	            request.getRequestURI()
	    );

	    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
	public ResponseEntity<ApiError> handleConstraintViolation(
	        jakarta.validation.ConstraintViolationException ex,
	        jakarta.servlet.http.HttpServletRequest request) {

	    ApiError error = new ApiError(
	            HttpStatus.BAD_REQUEST.value(),
	            "Validation Failed",
	            ex.getMessage(),
	            request.getRequestURI()
	    );

	    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
}