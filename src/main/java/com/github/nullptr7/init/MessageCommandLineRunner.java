package com.github.nullptr7.init;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MessageCommandLineRunner implements CommandLineRunner {

    private static String deploymentDefinition;

    @Override
    public void run(String... args) {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        deploymentDefinition = processEngine.getRepositoryService()
                                 .createDeployment()
                                 .addInputStream("task_demo.bpmn",
                                                 this.getClass()
                                                     .getClassLoader()
                                                     .getResourceAsStream("task_demo.bpmn"))
                                 .addInputStream("messageVerifierNew.dmn",
                                                 this.getClass()
                                                     .getClassLoader()
                                                     .getResourceAsStream("messageVerifierNew.dmn"))
                                 .deployWithResult()
                                 .getDeployedProcessDefinitions()
                                 .get(0)
                                 .getId();

        System.out.println("deploymentDefinition = " + deploymentDefinition);

    }

    public static String getDeploymentDefinition() {
        return deploymentDefinition;
    }
}
