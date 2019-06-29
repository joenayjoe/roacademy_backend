package com.rojunaid.roacademy.exceptionhandler;

import com.rojunaid.roacademy.dto.error.ErrorDetail;
import com.rojunaid.roacademy.dto.error.ValidationError;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rnfe, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTitle("Resource Not Found");
        errorDetail.setOccurredAt(LocalDateTime.now());
        errorDetail.setDetail(rnfe.getMessage());
        errorDetail.setDeveloperMessage(rnfe.getClass().getName());
        errorDetail.setStatus(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(errorDetail, null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException manve, HttpServletRequest request) {

        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTitle("Validation Failed");
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setOccurredAt(LocalDateTime.now());
        errorDetail.setDeveloperMessage(manve.getClass().getName());
        errorDetail.setDetail("Input validation failed");

        List<FieldError> fieldErrors = manve.getBindingResult().getFieldErrors();

        for(FieldError fe: fieldErrors) {
            List<ValidationError> validationErrorList = errorDetail.getErrors().get(fe.getField());
            if(validationErrorList == null) {
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
    public ResponseEntity<?> handleMessageNotReadableException(HttpMessageNotReadableException mnre, HttpServletRequest request) {

        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTitle("Bad Request");
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setOccurredAt(LocalDateTime.now());
        errorDetail.setDetail(mnre.getLocalizedMessage());
        errorDetail.setDeveloperMessage(mnre.getClass().getName());

        return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
    }
}
