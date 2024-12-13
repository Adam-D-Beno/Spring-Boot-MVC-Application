package org.das.springmvc.util;

import java.time.LocalDateTime;

public record ErrorMessageResponse(
        String message,
        String detailMessage,
        LocalDateTime localDateTime
) {
}
