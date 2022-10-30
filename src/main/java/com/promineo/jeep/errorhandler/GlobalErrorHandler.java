package com.promineo.jeep.errorhandler;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalErrorHandler {
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNoSuchElementException(NoSuchElementException e, WebRequest webRequest){
	return createExceptionMessage(e, HttpStatus.NOT_FOUND, webRequest); 
    }

    private Map<String, Object> createExceptionMessage(NoSuchElementException e, HttpStatus status, WebRequest webRequest) {

	Map<String, Object> error = new HashMap<>();	
	String timestamp = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME); 
	
	if(webRequest instanceof ServletWebRequest ) {
	    error.put("uri", ((ServletWebRequest)webRequest).getRequest().getRequestURI());
	    
	}
	
	error.put("message", e.toString()); 
	error.put("status code", status.value()); 
	error.put("timestamp", timestamp); 
	error.put("reason", status.getReasonPhrase()); 
	
	return error;
    }
    
    
}
