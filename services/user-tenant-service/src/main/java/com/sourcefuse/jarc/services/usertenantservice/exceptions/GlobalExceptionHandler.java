package com.sourcefuse.jarc.services.usertenantservice.exceptions;

import com.sourcefuse.jarc.services.usertenantservice.dto.ErrorDetails;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  private static final ResponseEntity<ErrorDetails> handleHttpError(
    ResponseStatusException exception,
    WebRequest webRequest
  ) {
    ErrorDetails errorDetails = new ErrorDetails(
      LocalDateTime.now(),
      exception.getReason(),
      webRequest.getDescription(false)
    );
    return new ResponseEntity<>(errorDetails, exception.getStatusCode());
  }

  @ExceptionHandler(Exception.class)
  private static final ResponseEntity<ErrorDetails> handleGlobalException(
    Exception exception,
    WebRequest webRequest
  ) {
    ErrorDetails errorDetails = new ErrorDetails(
      LocalDateTime.now(),
      exception.getMessage(),
      webRequest.getDescription(true)
    );
    exception.printStackTrace();
    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  private static final ResponseEntity<ErrorDetails> handleReScrNotFndException(
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
  private static final ResponseEntity<ErrorDetails> handleBlogAPIException(
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
}