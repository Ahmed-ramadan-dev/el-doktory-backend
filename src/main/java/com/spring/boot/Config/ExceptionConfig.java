package com.spring.boot.Config;

import com.spring.boot.Dto.Vm.ExceptionResponseVm;
import com.spring.boot.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice

public class ExceptionConfig {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseVm> handleValidationException(MethodArgumentNotValidException ex) {


        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());


        ExceptionResponseVm response = new ExceptionResponseVm(errors, HttpStatus.BAD_REQUEST);

        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ExceptionResponseVm> handlePatientNotFound(PatientNotFoundException ex) {
        ExceptionResponseVm response = new ExceptionResponseVm(
                List.of(ex.getMessage()), HttpStatus.NOT_FOUND
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ExceptionResponseVm> handleFileUpload(FileUploadException ex) {
        ExceptionResponseVm response = new ExceptionResponseVm(
                List.of(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @ExceptionHandler(AnalysisNotFoundException.class)
    public ResponseEntity<ExceptionResponseVm> handleAnalysisNotFoundException(AnalysisNotFoundException ex) {
        ExceptionResponseVm response = new ExceptionResponseVm(
                List.of(ex.getMessage()), HttpStatus.NOT_FOUND
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseVm> handleGenericException(Exception ex) {
        ExceptionResponseVm response = new ExceptionResponseVm(
                List.of("Something went wrong: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @ExceptionHandler(CloudinaryOperationException.class)
    public ResponseEntity<ExceptionResponseVm> handleCloudinaryError(CloudinaryOperationException ex) {
        ExceptionResponseVm response = new ExceptionResponseVm(
                List.of(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(SpecialtyNotFoundException.class)
    public ResponseEntity<ExceptionResponseVm> handleSpecialtyNotFound(SpecialtyNotFoundException ex) {
        ExceptionResponseVm response = new ExceptionResponseVm(
                List.of(ex.getMessage()), HttpStatus.NOT_FOUND
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(DoctorAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponseVm> handleDoctorAlreadyExists(DoctorAlreadyExistsException ex) {
        ExceptionResponseVm response = new ExceptionResponseVm(
                List.of(ex.getMessage()), HttpStatus.BAD_REQUEST
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}



