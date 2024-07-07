package com.email_verification.adonis.exception;

public class BusinessValidationException extends RuntimeException{

    public BusinessValidationException(String message) {
        super(message);
    }
}
