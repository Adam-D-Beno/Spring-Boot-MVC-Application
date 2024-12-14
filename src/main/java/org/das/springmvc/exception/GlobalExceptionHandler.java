package org.das.springmvc.exception;

import org.das.springmvc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidationException(MethodArgumentNotValidException e) {
        LOGGER.error("Got validation exception: MethodArgumentNotValidException", e);
        String detailMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " : "
                        + error.getDefaultMessage() + "."
                        + " Отклоненное значение: " + error.getRejectedValue())
                .collect(Collectors.joining(", "));

        var errorDto = new ErrorMessageResponse(
                "Ошибка валидации запроса",
                detailMessage,
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessageResponse> handleGenericException(Exception e) {
        LOGGER.error("Got validation exception: ", e);
        var error = new ErrorMessageResponse(
                "Server error",
                e.getMessage()
                ,LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorMessageResponse> handleNotFoundException(NoSuchElementException e) {
        LOGGER.error("Got validation exception: NoSuchElementException", e);
        var error = new ErrorMessageResponse(
                "No such Element found",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
}
