package org.example.trainsystem.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle RuntimeExceptions for web (Thymeleaf) pages
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("error"); // Use your custom error.html
        mav.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        mav.addObject("error", e.getMessage() != null ? e.getMessage() : "An unexpected error occurred.");
        mav.addObject("url", request.getRequestURL());
        return mav;
    }

    // Handle all other generic exceptions for REST or JSON requests
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", e.getMessage());
        response.put("path", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
