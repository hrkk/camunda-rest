package com.example.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MyJavaDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("\n\n" + new Date() +" delegateExecution, myVariable=" + delegateExecution.getVariable("myVariable") + " TenantId="+delegateExecution.getTenantId() + "!");
    }
}
