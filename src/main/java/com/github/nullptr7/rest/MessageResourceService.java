package com.github.nullptr7.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@RestController
public class MessageResourceService {

    private final RestTemplate template = new RestTemplate();

    @PostMapping(value = "/message/add", produces = APPLICATION_JSON)
    public ResponseEntity<String> submitMessage(@RequestBody String body) {

        return template.postForEntity("http://localhost:8080/rest/process-engine/", body, String.class);
    }
}
