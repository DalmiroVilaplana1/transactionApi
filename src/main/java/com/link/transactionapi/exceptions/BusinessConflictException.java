package com.link.transactionapi.exceptions;

public class BusinessConflictException extends RuntimeException {
    public BusinessConflictException(String message) { super(message); }
}