package com.sourcefuse.jarc.services.authservice.exception;

import com.sourcefuse.jarc.services.authservice.payload.ErrorDetails;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorDetails> handleResourceNotFoundException(
    ResourceNotFoundException exception,
    WebRequest webRequest
  ) {
    ErrorDetails errorDetails = new ErrorDetails(
      LocalDateTime.now(),
      exception.getMessage(),
      webRequest.getDescription(false)
    );
    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(CommonRuntimeException.class)
  public ResponseEntity<ErrorDetails> handleBlogAPIException(
    CommonRuntimeException exception,
    WebRequest webRequest
  ) {
    ErrorDetails errorDetails = new ErrorDetails(
      LocalDateTime.now(),
      exception.getMessage(),
      webRequest.getDescription(false)
    );
    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDetails> handleGlobalException(
    Exception exception,
    WebRequest webRequest
  ) {
    ErrorDetails errorDetails = new ErrorDetails(
      LocalDateTime.now(),
      exception.getMessage(),
      webRequest.getDescription(false)
    );
    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
    MethodArgumentNotValidException ex,
    HttpHeaders headers,
    HttpStatusCode status,
    WebRequest request
  ) {
    Map<String, String> errors = new HashMap<>();
    ex
      .getBindingResult()
      .getAllErrors()
      .forEach(error -> {
        String fieldName = ((FieldError) error).getField();
        String message = error.getDefaultMessage();
        errors.put(fieldName, message);
      });

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorDetails> handleAccessDeniedException(
    AccessDeniedException exception,
    WebRequest webRequest
  ) {
    ErrorDetails errorDetails = new ErrorDetails(
      LocalDateTime.now(),
      exception.getMessage(),
      webRequest.getDescription(false)
    );
    return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
  }
}
