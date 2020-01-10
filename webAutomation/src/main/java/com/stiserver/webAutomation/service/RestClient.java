package com.stiserver.webAutomation.service;

import com.stiserver.webAutomation.model.Report;
import org.apache.catalina.WebResource;
import org.glassfish.jersey.client.ClientResponse;
import org.springframework.util.Assert;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

 /**
 * SEND CLIENT Rest API RESPONSES
 */
public class RestClient {

    //https://www.baeldung.com/jersey-jax-rs-client
     private Client client = ClientBuilder.newClient(); //Jersey Client API

     /**
      * SEND SITE NAME AND REPORT TYPE
      *
      * @param site   SITE NAME
      * @param report TYPE OF REPORT
      */
     public void sendNetworkAnalysis(String site, Report report) {

         String REST_URI = "http://localhost:8080/api/mail/send";

         System.out.println(client
                 .target(REST_URI)
                 .queryParam("site", site)
                 .queryParam("report", report.getName())
                 .request(MediaType.APPLICATION_JSON)
                 .get(String.class));

     }
 }
