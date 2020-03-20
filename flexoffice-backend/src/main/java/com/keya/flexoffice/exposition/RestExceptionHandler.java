package com.keya.flexoffice.exposition;

import com.keya.flexoffice.domain.ErrorDetail;
import com.keya.flexoffice.infra.FlexBusinessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;



@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(FlexBusinessException.class)
    public ResponseEntity<?> handleException(FlexBusinessException fle, HttpServletRequest request){

        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(Instant.now().toEpochMilli());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setTitle(fle.getMessage());
        errorDetail.setDetail(fle.getMessage());
        errorDetail.setDeveloperMessage(fle.getClass().getName());

        return new ResponseEntity<>(errorDetail,null,HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException manv, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {

        ErrorDetail errorDetail  = new ErrorDetail();

        // Create ValidationError instances
        List<FieldError> fieldErrors = manv.getBindingResult().getFieldErrors();
        for(FieldError fe : fieldErrors) {

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

        return new ResponseEntity<>(errorDetail,null,HttpStatus.BAD_REQUEST);

    }


}
