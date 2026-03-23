package com.cognizant.taxease.exception;

public class InvalidDocumentTypeException extends RuntimeException {
    public InvalidDocumentTypeException(String message) {
        super(message);
    }
}