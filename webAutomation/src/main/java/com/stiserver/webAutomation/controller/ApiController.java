package com.stiserver.webAutomation.controller;

import oracle.jdbc.proxy.annotation.Post;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ApiController {

    private static RestTemplate restTemplate;

    public ApiController(RestTemplateBuilder RestTemplateBuilder) {
        ApiController.restTemplate = RestTemplateBuilder.build();
    }


    public static void sendCompletedNotification(){
        String url = "localhost:8080/api/mail/send";
        ResponseEntity<Post> response = restTemplate.getForEntity(url, Post.class);
        if(response.getStatusCode() == HttpStatus.OK) {
            response.getBody();
        }
    }
}
