package com.elderbyte.spring.boot.bootstrap.errors;


public class ExceptionDetailDto {

    public static ExceptionDetailDto build(String status, Throwable exception, String request){

        return new ExceptionDetailDto(
                status,
                exception.getClass().getCanonicalName(),
                exception.getClass().getSimpleName() + " at " + request + ": " + exception.getMessage(),
                request
        );
    }

    public String status;
    public String exception;
    public String message;
    public String request;



    public ExceptionDetailDto() { }

    public ExceptionDetailDto(
            String status,
            String exception,
            String message,
            String request) {
        this.status = status;
        this.exception = exception;
        this.message = message;
        this.request = request;
    }


    @Override
    public String toString() {
        return "ExceptionDetailDto{" +
                "exception='" + exception + '\'' +
                ", message='" + message + '\'' +
                ", request='" + request + '\'' +
                '}';
    }
}
