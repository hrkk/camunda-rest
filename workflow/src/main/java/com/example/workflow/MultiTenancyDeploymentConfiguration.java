package com.example.workflow;


import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;

import org.camunda.bpm.spring.boot.starter.configuration.Ordering;
import org.camunda.bpm.spring.boot.starter.configuration.impl.AbstractCamundaConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordering.DEFAULT_ORDER + 1)
public class MultiTenancyDeploymentConfiguration extends AbstractCamundaConfiguration {

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {

        System.err.println("preInit");



    }

    @Override
    public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        System.err.println("postInit");
    }

    @Override
    public void postProcessEngineBuild(ProcessEngine processEngine) {
        System.err.println("postProcessEngineBuild");
        RepositoryService repositoryService= processEngine.getRepositoryService();

        repositoryService.createDeployment()
                .tenantId("020")
                .addClasspathResource("processes/retry-incidents.bpmn")
                .deploy();

//        repositoryService.createDeployment()
//                .tenantId("369")
//                .addClasspathResource("processes/workflow-timer.bpmn")
//                .deploy();
    }
}
