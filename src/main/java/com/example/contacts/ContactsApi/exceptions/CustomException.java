package com.example.contacts.ContactsApi.exceptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.contacts.ContactsApi.models.ApiErrorResponse;
import com.example.contacts.ContactsApi.models.Contact;
import com.example.contacts.ContactsApi.models.ErrorResource;
import com.example.contacts.ContactsApi.models.FieldErrorResource;


@ControllerAdvice
public class CustomException extends ResponseEntityExceptionHandler{
	
	public static String getTimeStamp(){
		String timeStamp = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss.SSS").format(new java.util.Date());
		return timeStamp;
	}
	
	@ExceptionHandler({ InvalidRequestException.class })
    protected ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {
        InvalidRequestException ire = (InvalidRequestException) e;
        List<FieldErrorResource> fieldErrorResources = new ArrayList<>();

        List<FieldError> fieldErrors = ire.getErrors().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            FieldErrorResource fieldErrorResource = new FieldErrorResource();
            fieldErrorResource.setResource(fieldError.getObjectName());
            fieldErrorResource.setField(fieldError.getField());
            fieldErrorResource.setCode(fieldError.getCode());
            fieldErrorResource.setMessage(fieldError.getDefaultMessage());
            fieldErrorResources.add(fieldErrorResource);
        }

        ErrorResource error = new ErrorResource("InvalidRequest", ire.getMessage());
        error.setFieldErrors(fieldErrorResources);

        ApiErrorResponse apiError = new ApiErrorResponse(400, "Bad Request", ire.getMessage(), getTimeStamp(), fieldErrorResources);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, apiError, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
	  HttpRequestMethodNotSupportedException ex, 
	  HttpHeaders headers, 
	  HttpStatus status, 
	  WebRequest request) {
	    StringBuilder builder = new StringBuilder();
	    builder.append(ex.getMethod());
	    builder.append(
	      " method is not supported for this request. Supported methods are ");
	    ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));
	 
	    ApiErrorResponse apiError = new ApiErrorResponse(404, "Method Not Allowed", builder.toString(), getTimeStamp());
	    return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
	  HttpMediaTypeNotSupportedException ex, 
	  HttpHeaders headers, 
	  HttpStatus status, 
	  WebRequest request) {
	    StringBuilder builder = new StringBuilder();
	    builder.append(ex.getContentType());
	    builder.append(" media type is not supported. Supported media types are ");
	    ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));
	 
	    ApiErrorResponse apiError = new ApiErrorResponse(415, "Unsupported Media Type", builder.substring(0, builder.length() - 2), getTimeStamp());
	    return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
	  MethodArgumentNotValidException ex, 
	  HttpHeaders headers, 
	  HttpStatus status, 
	  WebRequest request) {
	    List<String> errors = new ArrayList<String>();
	    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
	        errors.add(error.getField() + ": " + error.getDefaultMessage());
	    }
	    for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
	        errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
	    }
	    
	    StringBuilder builder = new StringBuilder();
	    for (String error: errors)
	    	builder.append(error);
	     
	    ApiErrorResponse apiError = new ApiErrorResponse(400, "Bad Request", builder.toString(), getTimeStamp());
	    return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(
	  NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
	    String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
	 
	    ApiErrorResponse apiError = new ApiErrorResponse(404, "No Handler Found", error, getTimeStamp());
	    return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.NOT_FOUND);
	}
	
	
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
	  MissingServletRequestParameterException ex, HttpHeaders headers, 
	  HttpStatus status, WebRequest request) {
	    String error = ex.getParameterName() + " parameter is missing";
	     
	    ApiErrorResponse apiError = new ApiErrorResponse(400, ex.getLocalizedMessage(), error, getTimeStamp());
	    return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	protected ResponseEntity<Object> handleBadCredentials(Exception ex) {
		
		ApiErrorResponse apiError = new ApiErrorResponse(401, "Invalid Credentials", ex.getMessage(), getTimeStamp());
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
		
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<Object> handleAccessDenied(Exception ex) {
		
		ApiErrorResponse apiError = new ApiErrorResponse(401, "Invalid Credentials", ex.getMessage(), getTimeStamp());
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
		
	}
	
	
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
		ApiErrorResponse apiError = new ApiErrorResponse(500, ex.getLocalizedMessage(), "error occurred", getTimeStamp());
	    return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
