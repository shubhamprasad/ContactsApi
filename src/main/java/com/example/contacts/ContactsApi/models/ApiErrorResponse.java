package com.example.contacts.ContactsApi.models;

import java.util.List;

public class ApiErrorResponse {

	private String timestamp;
    private int status;
    private String code;
    private String message;
    private List<FieldErrorResource> fieldErrors;
    
    public ApiErrorResponse(int status, String code, String message, String timestamp) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
    }
    
    public ApiErrorResponse(int status, String code, String message, String timestamp, List<FieldErrorResource> fieldErrors) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.fieldErrors = fieldErrors;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    
    public String getTimestamp(){
    	return timestamp;
    }
    
    public List<FieldErrorResource> getFieldErrors(){
    	return fieldErrors;
    }

    @Override
    public String toString() {
        return "ApiErrorResponse{" +
        		"timestamp=" + timestamp +
                ", status=" + status +
                ", code=" + code +
                ", message=" + message +
                '}';
    }
}