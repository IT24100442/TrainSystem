package org.example.trainsystem.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(Exception e, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();

        // Add useful info to the view
        mav.addObject("error", e.getMessage());
        mav.addObject("url", request.getRequestURL());
        mav.addObject("status", 500); // generic internal error
        mav.setViewName("error"); // error.html in templates

        return mav;
    }
}
