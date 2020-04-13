package com.github.nullptr7.rest;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * TODO: Still a work in progress.
 * Need to figure out the way to get deployment ID or processDefinitionID to trigger normal call instead of REST call.
 *
 */
@RestController
public class MessageResourceService {

    private final Random random = new Random();

    @PostMapping(value = "/message/add", produces = APPLICATION_JSON)
    public ResponseEntity<String> submitMessage(@RequestBody String body) {

        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService service = engine.getRuntimeService();


        ProcessInstance executeResult = service.createProcessInstanceById("manual_process_id_" + random.nextLong())
                                               .businessKey("sender_1")
                                               .setVariable("content", "This is test content sent by Sender 1")
                                               .execute();

        System.out.println("executeResult = " + executeResult.toString());

        return ResponseEntity.ok(executeResult.toString());
    }
}
