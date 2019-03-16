package com.elderbyte.web.client;


import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;

public class HttpClientResponseError extends HttpResponseError {

    public HttpClientResponseError(HttpRequest request, HttpStatus status, String statusText, String responseBody) {
        super(request, status, statusText, responseBody);
    }
}
