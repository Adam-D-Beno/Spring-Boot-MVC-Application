package org.das.springmvc.exception;

import java.time.LocalDateTime;

public record ErrorMessageResponse(
        String message,
        String detailMessage,
        LocalDateTime localDateTime
) {
}
