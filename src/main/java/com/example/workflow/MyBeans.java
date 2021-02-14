package com.example.workflow;

import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class MyBeans {

    @Bean
    public JavaDelegate ahoyService() {
        return execution -> System.out.println("\n\n" + new Date() +" Ahoy, " + execution.getVariable("myVariable") + "!");
    }
}
