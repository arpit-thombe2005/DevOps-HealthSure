/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.servlet.http.HttpServletRequest
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.security.access.AccessDeniedException
 *  org.springframework.ui.Model
 *  org.springframework.web.bind.annotation.ControllerAdvice
 *  org.springframework.web.bind.annotation.ExceptionHandler
 *  org.springframework.web.bind.annotation.ResponseStatus
 *  org.springframework.web.multipart.MaxUploadSizeExceededException
 *  org.springframework.web.servlet.ModelAndView
 *  org.springframework.web.servlet.resource.NoResourceFoundException
 */
package com.healthsure.exception;

import com.healthsure.exception.InvalidStateTransitionException;
import com.healthsure.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value={NoResourceFoundException.class})
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    public void handleNoResourceFound() {
    }

    @ExceptionHandler(value={ResourceNotFoundException.class})
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", (Object)ex.getMessage());
        model.addAttribute("status", (Object)404);
        return "error/404";
    }

    @ExceptionHandler(value={AccessDeniedException.class})
    @ResponseStatus(value=HttpStatus.FORBIDDEN)
    public String handleAccessDenied(HttpServletRequest request, Model model) {
        model.addAttribute("status", (Object)403);
        return "error/access-denied";
    }

    @ExceptionHandler(value={InvalidStateTransitionException.class})
    public ModelAndView handleInvalidStateTransition(InvalidStateTransitionException ex, HttpServletRequest request) {
        log.warn("Invalid state transition: {}", (Object)ex.getMessage());
        ModelAndView mav = new ModelAndView("error/generic");
        mav.addObject("errorMessage", (Object)ex.getMessage());
        mav.addObject("status", (Object)400);
        mav.setStatus((HttpStatusCode)HttpStatus.BAD_REQUEST);
        return mav;
    }

    @ExceptionHandler(value={MaxUploadSizeExceededException.class})
    public ModelAndView handleMaxUploadSize(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        log.warn("Upload size exceeded: {}", (Object)ex.getMessage());
        ModelAndView mav = new ModelAndView("error/generic");
        mav.addObject("errorMessage", (Object)"The uploaded file exceeds the maximum allowed size. Please upload a smaller file.");
        mav.addObject("status", (Object)400);
        mav.setStatus((HttpStatusCode)HttpStatus.BAD_REQUEST);
        return mav;
    }

    @ExceptionHandler(value={Exception.class})
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, Model model, HttpServletRequest request) {
        log.error("Unhandled exception for request {}: {}", (Object)request.getRequestURI(), (Object)ex.getClass().getSimpleName());
        model.addAttribute("status", (Object)500);
        model.addAttribute("errorMessage", (Object)"An unexpected error occurred. Please try again or contact support.");
        return "error/generic";
    }
}
