package com.github.nullptr7.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named("rejectTask")
public class RejectionDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {

        String content = (String) delegateExecution.getVariable("content");
        String comments = (String) delegateExecution.getVariable("comments");

        System.out.println("Message is rejected - " + content + " \ncomments: " + (comments == null ? "Auto Rejected!" : comments));
    }
}
