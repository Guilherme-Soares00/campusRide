package br.com.fiap.campusride.exception;

import java.time.Instant;
import java.util.Map;

public record ApiError(
        Instant timestamp,
        int status,
        String message,
        String path,
        Map<String, String> fields
) {
}
