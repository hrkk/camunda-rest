package com.example.workflow;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class RestConnector {

    private final RestTemplate restTemplate;

    public  int execute(String tenantId) {
        System.out.println("  START CALL REST ("+tenantId+")");
        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:9080/hello", HttpMethod.GET,  new HttpEntity<>(null), String.class);
        System.out.println("  END CALL_REST ("+tenantId+")");
        return exchange.getStatusCodeValue();
    }
}
