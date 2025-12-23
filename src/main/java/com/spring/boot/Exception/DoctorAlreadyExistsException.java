package com.spring.boot.Exception;

public class DoctorAlreadyExistsException extends RuntimeException {

    public DoctorAlreadyExistsException(String message) {
        super(message);
    }
}
