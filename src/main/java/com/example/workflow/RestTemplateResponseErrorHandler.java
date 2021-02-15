package com.example.workflow;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {


    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        //log.error("Response error: {} {}", response.getStatusCode(), response.getStatusText());
        System.out.println("Response error: " + response.getStatusCode() + " " + response.getStatusText());
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return isError(response.getStatusCode());
    }

    private boolean isError(HttpStatus status) {
        HttpStatus.Series series = status.series();
        return (HttpStatus.Series.CLIENT_ERROR.equals(series) || HttpStatus.Series.SERVER_ERROR.equals(series)
                || HttpStatus.Series.REDIRECTION.equals(series));
    }
}