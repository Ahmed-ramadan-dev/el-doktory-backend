package com.spring.boot.Exception;

public class SpecialtyNotFoundException extends RuntimeException {

    public SpecialtyNotFoundException(String message) {
        super(message);
    }

}
