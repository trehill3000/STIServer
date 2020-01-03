package com.stiserver.webAutomation.service;

import com.stiserver.webAutomation.model.Report;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

/**
 * SEND CLIENT Rest API
 */
public class RestClient {

    private Client client = ClientBuilder.newClient(); //Jersery Client API

    /**
     * SEND EMAIL SERVER COMPLETED SITE
     * @param site SITE NAME
     * @param report TYPE OF REPORT
     * @return ""
     */
    public String sendNetworkAnalysis(String site, Report report) {

        String REST_URI = "http://localhost:8080/api/mail";
        return client
                .target(REST_URI)
                .path(site).path(report.getName()).path("send")
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
    }
}

