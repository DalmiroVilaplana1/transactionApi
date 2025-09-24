package com.link.TransactionApi.exceptions;

public class BusinessConflictException extends RuntimeException {
    public BusinessConflictException(String message) { super(message); }
}