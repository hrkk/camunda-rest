package com.example.workflow;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.jobexecutor.JobExecutorContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class RestService3JavaDelegate implements JavaDelegate {

    private final RestConnector connector;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        JobExecutorContext jobExecutorContext = Context.getJobExecutorContext();
        // when manuel retry task is invoked then jobExecutorContext is null otherwise get configured retries
        int retriesLeft = 1;
        if (jobExecutorContext != null) {
            retriesLeft = jobExecutorContext.getCurrentJob().getRetries();
        }

        System.out.println("\n\n" + "BEGIN " + new Date() + " TaskId=" + delegateExecution.getId() + ", TenantId=" + delegateExecution.getTenantId() + ", retries_left=" + retriesLeft + ", manualRetryRest=" + (delegateExecution.getVariable("manualRetryRest") != null ? "true" : "false") + " ProcessInstanceId="+delegateExecution.getProcessInstanceId());
        // 1. call external service
        try {
            int statusCode = connector.execute(delegateExecution.getTenantId());
            delegateExecution.setVariable("success", true);
            System.out.println("END " + new Date() + " TaskId=" + delegateExecution.getId() + ", myVariable=" + delegateExecution.getVariable("myVariable") + ", TenantId=" + delegateExecution.getTenantId() + ", RestStatus=" + statusCode);
        } catch (ResourceAccessException | HttpServerErrorException e) {
            System.err.println(e.getClass() + " " + e.getMessage());
            handleFailure(delegateExecution, retriesLeft, e);
        }
    }

    private void handleFailure(DelegateExecution delegateExecution, int retriesLeft, Exception e) throws Exception {
        if (retriesLeft <= 1) {
            System.err.println("Create BpmnError for processInstanceId="+delegateExecution.getProcessInstanceId());
            delegateExecution.setVariable("manualRetryRest", false);
            delegateExecution.setVariable("success", false);
        } else {
            throw e;
        }
    }
}
