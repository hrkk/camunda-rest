package com.example.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
public class MyJavaDelegate implements JavaDelegate {

    RestTemplate restTemplate = new RestTemplate();

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("BEGIN " + new Date() +" delegateExecution");
        HttpEntity<Object> httpEntity = new HttpEntity<>(null);
        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/hello", HttpMethod.GET, httpEntity, String.class);

        System.out.println("\n\n" + new Date() +" delegateExecution, myVariable=" + delegateExecution.getVariable("myVariable") + " TenantId="+delegateExecution.getTenantId() + " Rest="+exchange.getStatusCodeValue());
    }
}
