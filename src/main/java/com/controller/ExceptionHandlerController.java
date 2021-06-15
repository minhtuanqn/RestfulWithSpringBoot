package com.controller;

import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.BeanExpressionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for handle exception from controller
 */
@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    Map<String, String> invalidMap;

    @ExceptionHandler(BeanExpressionException.class)
    @ResponseBody
    public String handleConflict() {
        return "Exception handled outside the Controller";
    }

    private void initMap() {
        if (invalidMap == null) {
            invalidMap = new HashMap<>();
        }
    }

    /**
     * handle mismatch type
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return response entity
     */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        initMap();
        invalidMap.put("Type mismatch error:", ex.getMessage());
        return handleExceptionInternal(ex, invalidMap, headers, status, request);
    }

}
