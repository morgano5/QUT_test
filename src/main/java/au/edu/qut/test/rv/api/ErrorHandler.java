package au.edu.qut.test.rv.api;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {
    return ResponseEntity.badRequest()
        .body(
            new ErrorResponse(HttpStatus.BAD_REQUEST.value(), getOriginalProblem(ex).getMessage()));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException ex) {
    String message =
        ex.getConstraintViolations().stream()
            .map(v -> v.getPropertyPath() + " " + v.getMessage())
            .collect(Collectors.joining(", "));
    return ResponseEntity.badRequest().body(new ErrorResponse(400, message));
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    ErrorResponse response =
        new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(), getOriginalProblem(ex).getMessage().split("\n")[0]);
    return ResponseEntity.badRequest().body(response);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    ErrorResponse response =
        new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            messageFromErrors(ex.getBindingResult().getAllErrors()));
    return ResponseEntity.badRequest().body(response);
  }

  private String messageFromErrors(List<ObjectError> errors) {
    return errors.stream()
        .map(
            error ->
                "In object "
                    + error.getObjectName()
                    + ": "
                    + Arrays.stream(Optional.ofNullable(error.getArguments()).orElse(new Object[0]))
                        .filter(arg -> arg instanceof DefaultMessageSourceResolvable)
                        .map(arg -> ((DefaultMessageSourceResolvable) arg).getDefaultMessage())
                        .collect(Collectors.joining(", "))
                    + " "
                    + error.getDefaultMessage())
        .collect(Collectors.joining("; "));
  }

  private Throwable getOriginalProblem(Throwable ex) {
    Throwable problem = ex;
    while (problem.getCause() != problem && problem.getCause() != null) {
      problem = problem.getCause();
    }
    return problem;
  }
}
