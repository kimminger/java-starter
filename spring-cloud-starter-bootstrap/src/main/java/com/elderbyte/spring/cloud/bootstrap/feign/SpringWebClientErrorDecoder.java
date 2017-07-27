package com.elderbyte.spring.cloud.bootstrap.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Translate Http Error codes to Spring HttpClientErrorException / HttpServerErrorException
 * Note that
 */
public class SpringWebClientErrorDecoder implements ErrorDecoder {

    private ErrorDecoder delegate = new Default();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public Exception decode(String methodKey, Response response) {
        HttpHeaders responseHeaders = new HttpHeaders();
        response.headers().entrySet()
                .forEach(entry -> responseHeaders.put(entry.getKey(), new ArrayList<>(entry.getValue())));

        HttpStatus statusCode = HttpStatus.valueOf(response.status());
        String statusText = response.reason();

        byte[] responseBody = readResponseBody(response);

        if(statusText == null){
            // The response 'reason' is often not populated, and Http2 will drop it
            // Thus we assume additional information might be in the body as text
            statusText = new String(responseBody);
        }

        if (response.status() >= 400 && response.status() <= 499) {
            return new HttpClientErrorException(statusCode, statusText, responseHeaders, responseBody, null);
        }

        if (response.status() >= 500 && response.status() <= 599) {
            return new HttpServerErrorException(statusCode, statusText, responseHeaders, responseBody, null);
        }
        return delegate.decode(methodKey, response);
    }

    private byte[] readResponseBody(Response response){
        Response.Body body = response.body();
        byte[] responseBody;
        if(body != null){
            try {
                responseBody = getBytesFromInputStream(body.asInputStream());
            } catch (IOException e) {
                logger.error("Failed to read response body of error message!", e);
                responseBody = new byte[0];
            }
        }else{
            responseBody = new byte[0];
        }
        return responseBody;
    }


    private static byte[] getBytesFromInputStream(InputStream is) throws IOException
    {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream())
        {
            byte[] buffer = new byte[0xFFFF];
            for (int len; (len = is.read(buffer)) != -1;)
                os.write(buffer, 0, len);
            os.flush();
            return os.toByteArray();
        }
    }
}