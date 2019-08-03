package com.rojunaid.roacademy.exceptionhandler;

import com.rojunaid.roacademy.dto.error.ErrorDetail;
import com.rojunaid.roacademy.dto.error.ValidationError;
import com.rojunaid.roacademy.exception.ResourceAlreadyExistException;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@ControllerAdvice
public class RestExceptionHandler {

  private static Map<String, String> constraintCodeMap;

  static {
    constraintCodeMap =
        new HashMap<String, String>() {
          {
            put("uk_grade_name", "Grade name already exist");
            put("uk_course_name", "Course name already exist");
            put("uk_chapter_name", "Chapter name already exist");
            put("tag_name_uk", "Tag name already exist");
            put("category_name_uk", "Category name already exist");
            put("user_email_uk", "Email id already exist");
            put("uk_role_name", "Role name already exist");
            put("uk_resource_title", "Title already exist");
          }
        };
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<?> handleResourceNotFoundException(
      ResourceNotFoundException rnfe, HttpServletRequest request) {
    ErrorDetail errorDetail = new ErrorDetail();
    errorDetail.setTitle("Resource Not Found");
    errorDetail.setOccurredAt(LocalDateTime.now());
    errorDetail.setDetail(rnfe.getMessage());
    errorDetail.setDeveloperMessage(rnfe.getClass().getName());
    errorDetail.setStatus(HttpStatus.NOT_FOUND.value());

    return new ResponseEntity<>(errorDetail, null, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidationError(
      MethodArgumentNotValidException manve, HttpServletRequest request) {

    ErrorDetail errorDetail = new ErrorDetail();
    errorDetail.setTitle("Validation Failed");
    errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
    errorDetail.setOccurredAt(LocalDateTime.now());
    errorDetail.setDeveloperMessage(manve.getClass().getName());
    errorDetail.setDetail("Input validation failed");

    List<FieldError> fieldErrors = manve.getBindingResult().getFieldErrors();

    for (FieldError fe : fieldErrors) {
      List<ValidationError> validationErrorList = errorDetail.getErrors().get(fe.getField());
      if (validationErrorList == null) {
        validationErrorList = new ArrayList<>();
        errorDetail.getErrors().put(fe.getField(), validationErrorList);
      }
      ValidationError validationError = new ValidationError();
      validationError.setCode(fe.getCode());
      validationError.setMessage(fe.getDefaultMessage());

      validationErrorList.add(validationError);
    }

    return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<?> handleMessageNotReadableException(
      HttpMessageNotReadableException mnre, HttpServletRequest request) {

    ErrorDetail errorDetail = new ErrorDetail();
    errorDetail.setTitle("Bad Request");
    errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
    errorDetail.setOccurredAt(LocalDateTime.now());
    errorDetail.setDetail(mnre.getLocalizedMessage());
    errorDetail.setDeveloperMessage(mnre.getClass().getName());

    return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResourceAlreadyExistException.class)
  public ResponseEntity<?> handleResourceAlreadyExistException(
      ResourceAlreadyExistException raee, HttpServletRequest request) {

    ErrorDetail errorDetail = new ErrorDetail();
    errorDetail.setTitle("Request Conflict");
    errorDetail.setStatus(HttpStatus.CONFLICT.value());
    errorDetail.setOccurredAt(LocalDateTime.now());
    errorDetail.setDetail(raee.getMessage());
    errorDetail.setDeveloperMessage(raee.getClass().getName());

    return new ResponseEntity<>(errorDetail, null, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
  public ResponseEntity<?> handleMediaTypeNotAcceptableException(
      HttpMediaTypeNotAcceptableException mtnae, HttpServletRequest request) {
    ErrorDetail errorDetail = new ErrorDetail();
    errorDetail.setTitle("Content Type Not Acceptable");
    errorDetail.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
    errorDetail.setDetail(mtnae.getMessage());
    errorDetail.setOccurredAt(LocalDateTime.now());
    errorDetail.setDeveloperMessage(mtnae.getClass().getName());

    return new ResponseEntity<>(errorDetail, null, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  @ResponseStatus(value = HttpStatus.CONFLICT) // 409
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<?> constrainViolation(
      DataIntegrityViolationException exp, HttpServletRequest req) {

    String rootMsg = exp.getLocalizedMessage();
    Optional<Map.Entry<String, String>> entry =
        constraintCodeMap.entrySet().stream()
            .filter((it) -> rootMsg.contains(it.getKey()))
            .findAny();

    if (entry.isPresent()) {
      ErrorDetail errorDetail = new ErrorDetail();
      errorDetail.setTitle("Constrain Violation Error");
      errorDetail.setStatus(HttpStatus.CONFLICT.value());
      errorDetail.setOccurredAt(LocalDateTime.now());
      errorDetail.setDetail(entry.get().getValue());
      errorDetail.setDeveloperMessage(exp.getClass().getName());

      return new ResponseEntity<>(errorDetail, null, HttpStatus.CONFLICT);

    } else {
      return globalErrorResponse(exp.getLocalizedMessage(), exp.getClass().getName());
    }
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> globalExceptionHandler(Exception exp, HttpServletRequest request) {
    return globalErrorResponse(exp.getLocalizedMessage(), exp.getClass().getName());
  }

  private ResponseEntity<?> globalErrorResponse(String msg, String cause) {
    ErrorDetail errorDetail = new ErrorDetail();
    errorDetail.setTitle("Internal Server Error");
    errorDetail.setDetail(msg);
    errorDetail.setOccurredAt(LocalDateTime.now());
    errorDetail.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    errorDetail.setDeveloperMessage(cause);

    return new ResponseEntity<>(errorDetail, null, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
