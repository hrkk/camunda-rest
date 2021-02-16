package com.example.workflow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
        return restTemplate;
    }

    public static class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            System.err.println("Response error: " + response.getStatusCode() + " " + response.getStatusText());
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
}
