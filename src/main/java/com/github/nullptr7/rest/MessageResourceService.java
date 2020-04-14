package com.github.nullptr7.rest;

import com.github.nullptr7.model.Message;
import lombok.NonNull;
import org.camunda.bpm.engine.AuthorizationException;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.nullptr7.init.MessageCommandLineRunner.getDeploymentDefinition;
import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.springframework.http.ResponseEntity.ok;

/**
 * TODO: Still a work in progress.
 * Need to figure out the way to get deployment ID or processDefinitionID to trigger normal call instead of REST call.
 */
@RestController
public class MessageResourceService {

    @PostMapping(value = "/message/add", produces = APPLICATION_JSON)
    public ResponseEntity<String> submitMessage(@RequestBody @NonNull Message message) {
        RuntimeService service = ProcessEngines.getDefaultProcessEngine()
                                               .getRuntimeService();

        ProcessInstance executeResult = service.createProcessInstanceById(getDeploymentDefinition())
                                               .businessKey(message.getMessageKey())
                                               .setVariable("content", message.getContent())
                                               .execute();

        return ok(executeResult.toString());
    }

    @GetMapping(value = "/message/unclaimed", produces = APPLICATION_JSON)
    public ResponseEntity<List<TaskDto>> getUnclaimedMessages() {

        List<Task> taskList = ProcessEngines.getDefaultProcessEngine()
                                            .getTaskService()
                                            .createTaskQuery()
                                            .active()
                                            .taskUnassigned()
                                            .processDefinitionId(getDeploymentDefinition())
                                            .list();

        return ok(taskList.stream()
                          .map(TaskDto::fromEntity)
                          .collect(toList()));

    }

    @PostMapping(value = "/message/{messageId}/claim")
    public ResponseEntity<?> assignUserToReview(@PathVariable String messageId, @QueryParam("userId") String userId) {
        String message;

        try {
            ProcessEngines.getDefaultProcessEngine()
                          .getTaskService()
                          .claim(messageId, userId);
            message = "Claimed successful";
        } catch (Exception exception) {
            if (exception instanceof AuthorizationException) {
                message = "User is not authorized to perform this action";
            } else if (exception instanceof ProcessEngineException) {
                message = "Already claimed by other user";
            } else {
                message = "Unknown error";
            }
        }

        return ResponseEntity.ok(message);
    }

    @PostMapping(value = "/message/{messageId}/review", produces = APPLICATION_JSON)
    public ResponseEntity<String> reviewMessage(@PathVariable String messageId, @RequestBody @NonNull Message message) {

        String response;
        final Map<String, Object> variables = new HashMap<String, Object>() {{
            put("comments", message.getComments());
            put("approve", message.isApproved());
        }};

        try {
            ProcessEngines.getDefaultProcessEngine()
                          .getTaskService()
                          .complete(messageId, variables);
            response = "Review Complete.";
        } catch (Exception exception) {
            if (exception instanceof AuthorizationException) {
                response = "User is not authorized to perform this action";
            } else if (exception instanceof ProcessEngineException) {
                response = "No Task Exists for given messageId";
            } else {
                response = "Unknown error";
            }

        }

        return ResponseEntity.ok(response);
    }
}
