package com.erp.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(InsufficientStockException.class)
	public ResponseEntity<String> handleInsufficientStock(InsufficientStockException ex) {
	    return ResponseEntity.badRequest().body(ex.getMessage());
	}


	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {

		Map<String, Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("status", HttpStatus.NOT_FOUND.value());
		error.put("error", "Not Found");
		error.put("message", ex.getMessage());

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {

		Map<String, Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.put("error", "Internal Server Error");
		error.put("message", ex.getMessage());

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<?> handleDuplicateResource(DuplicateResourceException ex) {

	    return ResponseEntity.status(HttpStatus.CONFLICT).body(
	            Map.of(
	                    "error", "Conflict",
	                    "message", ex.getMessage()
	            )
	    );
	}

}
