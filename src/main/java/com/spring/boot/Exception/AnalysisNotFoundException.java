package com.spring.boot.Exception;

public class AnalysisNotFoundException extends RuntimeException {
    public AnalysisNotFoundException(String message) {
        super(message);
    }
}
