package com.stiserver.webAutomation.service;

import com.stiserver.webAutomation.model.Report;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

 /**
 * SEND CLIENT Rest API RESPONSES
 */
public class RestClient {

    //https://www.mkyong.com/webservices/jax-rs/restfull-java-client-with-java-net-url/
    private Client client = ClientBuilder.newClient(); //Jersey Client API

    /**
     * SEND EMAIL SERVER COMPLETED SITE
     * @param site SITE NAME
     * @param report TYPE OF REPORT
     */
    public void sendNetworkAnalysis(String site, Report report) {

        String REST_URI = "http://localhost:8080/api/mail/send";
        client
                .target(REST_URI)
                .path(site).path(report.getName())
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
    }
}

