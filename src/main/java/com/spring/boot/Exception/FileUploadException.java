package com.spring.boot.Exception;

public class FileUploadException extends RuntimeException {

    public FileUploadException(String message) {
        super(message);
    }
}
