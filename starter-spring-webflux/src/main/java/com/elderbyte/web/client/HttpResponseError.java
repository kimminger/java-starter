package com.elderbyte.web.client;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;

public class HttpResponseError extends RuntimeException {

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final HttpRequest request;
    private final HttpStatus status;
    private final String statusText;
    private final String responseBody;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    public HttpResponseError(HttpRequest request, HttpStatus status, String statusText, String responseBody) {
        super(status + " - " + statusText + " @ " + request.getMethod() + " " + request.getURI()  + " --> Body: " + responseBody);
        this.request = request;
        this.status = status;
        this.statusText = statusText;
        this.responseBody = responseBody;
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    public HttpRequest getRequest() {
        return request;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
