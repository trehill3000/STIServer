package com.stiserver.webAutomation.service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class RestClient {

    private static final String REST_URI
            = "http://localhost:8080/api/mail/send";

    private Client client = ClientBuilder.newClient();

    public String getJsonEmployee() {
        return client
                .target(REST_URI)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
    }
}

