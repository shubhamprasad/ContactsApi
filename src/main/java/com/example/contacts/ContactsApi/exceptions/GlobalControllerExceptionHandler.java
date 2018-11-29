package com.example.contacts.ContactsApi.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.MethodNotAllowed;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.example.contacts.ContactsApi.models.ApiErrorResponse;
import com.example.contacts.ContactsApi.models.ErrorResource;
import com.example.contacts.ContactsApi.models.FieldErrorResource;
import com.mongodb.MongoException;
import com.mongodb.MongoWriteException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;


@RestControllerAdvice
public class GlobalControllerExceptionHandler {
	
	public static String getTimeStamp(){
		String timeStamp = new SimpleDateFormat("dd-mm-yyyy-HH-mm-ss.SSS").format(new java.util.Date());
		return timeStamp;
	}
	
	@ExceptionHandler({ InvalidRequestException.class })
    protected ApiErrorResponse handleInvalidRequest(RuntimeException e, WebRequest request) {
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

        //return handleExceptionInternal(e, apiError, headers, HttpStatus.BAD_REQUEST, request);
        return apiError;
	}

    @ExceptionHandler(value = { ConstraintViolationException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse constraintViolationException(ConstraintViolationException ex) {	
        return new ApiErrorResponse(500, "Bad Request", ex.getMessage(), getTimeStamp());
    }

    @ExceptionHandler(value = { NoHandlerFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse noHandlerFoundException(Exception ex) {
        return new ApiErrorResponse(404, "No Handler Found", "No Handler Found", getTimeStamp());
    }
    
    @ExceptionHandler(value = { HttpRequestMethodNotSupportedException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse noMethodFoundException(HttpRequestMethodNotSupportedException ex) {
    	StringBuilder builder = new StringBuilder();
	    builder.append(ex.getMethod());
	    builder.append(
	      " method is not supported for this request. Supported methods are ");
	    ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));
        return new ApiErrorResponse(405, "Method Not Allowed", builder.toString(), getTimeStamp());
    }
    
    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse unknownException(Exception ex) {
        return new ApiErrorResponse(500, "Internal Server Error", ex.getMessage(), getTimeStamp());
    }
    
    @ExceptionHandler(value = { MongoException.class })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse duplicateKeyException(Exception ex) {
        return new ApiErrorResponse(409, "Conflict", ex.getLocalizedMessage(), getTimeStamp());
    }
}