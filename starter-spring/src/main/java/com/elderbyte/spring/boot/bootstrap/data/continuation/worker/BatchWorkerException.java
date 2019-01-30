package com.elderbyte.spring.boot.bootstrap.data.continuation.worker;

public class BatchWorkerException extends RuntimeException {

    public BatchWorkerException(String message, Throwable cause){
        super(message, cause);
    }
}
