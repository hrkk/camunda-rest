package com.example.workflow;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.jobexecutor.JobExecutorContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class MyJavaDelegate implements JavaDelegate {

    private final RestConnector connector;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        JobExecutorContext jobExecutorContext = Context.getJobExecutorContext();
        int retriesLeft = 1;
        // when manuel retry task is invoked then jobExecutorContext is null
        if(jobExecutorContext!=null) {
             retriesLeft = jobExecutorContext.getCurrentJob().getRetries();
        }
        System.out.println("\n\n" + "BEGIN " + new Date() + " TaskId=" + delegateExecution.getId() + ", TenantId=" + delegateExecution.getTenantId() + ", retries_left=" + retriesLeft+ ", manualRetryRest="+  (delegateExecution.getVariable("manualRetryRest") != null ? "true" : "false"));
        // 1. call external service
        int statusCode = connector.execute(delegateExecution.getTenantId());
        if (statusCode != 200) {
            if(retriesLeft <=1) {
                System.err.println("Crete BpmnError!");
                delegateExecution.setVariable("restErrorCode", statusCode);
                delegateExecution.setVariable("manualRetryRest", false);
                throw new BpmnError("Error_RestServiceError");
            } else {
                throw new RuntimeException("try again");
            }
        }

        // 2. business logic (long running task)
        this.businessLogic(delegateExecution.getTenantId());

        System.out.println("END " + new Date() + " TaskId=" + delegateExecution.getId() + ", myVariable=" + delegateExecution.getVariable("myVariable") + ", TenantId=" + delegateExecution.getTenantId() + ", RestStatus=" + statusCode);
    }

    private void businessLogic(String tenantId) throws InterruptedException {
        System.out.println("  START BUSINESS_LOGIC (" + tenantId + ")");
        Thread.sleep(5000);
        System.out.println("  END BUSINESS_LOGIC (" + tenantId + ")");
    }
}
