package com.github.nullptr7.rest;

import com.github.nullptr7.model.Message;
import lombok.NonNull;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.github.nullptr7.init.MessageCommandLineRunner.getDeploymentDefinition;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * TODO: Still a work in progress.
 * Need to figure out the way to get deployment ID or processDefinitionID to trigger normal call instead of REST call.
 */
@RestController
public class MessageResourceService {

    @PostMapping(value = "/message/add", produces = APPLICATION_JSON)
    public ResponseEntity<String> submitMessage(@RequestBody @NonNull Message message) {

        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService service = engine.getRuntimeService();

        ProcessInstance executeResult = service.createProcessInstanceById(getDeploymentDefinition())
                                               .businessKey(message.getMessageKey())
                                               .setVariable("content", message.getContent())
                                               .execute();

        return ResponseEntity.ok(executeResult.toString());
    }
}
