package com.link.TransactionApi.exceptions;

import java.time.Instant;
import java.util.List;

public record ApiError(
        Instant timestamp,
        int status,
        String code,
        String message,
        String path,
        List<FieldErrorDetail> details
) {
    public static ApiError of(int status, String code, String message, String path, List<FieldErrorDetail> details) {
        return new ApiError(Instant.now(), status, code, message, path, details);
    }

    public record FieldErrorDetail(String field, String error) {}
}