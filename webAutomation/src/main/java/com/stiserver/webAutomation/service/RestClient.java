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
      * SEND NETWORK ANALYSIS REPORT IS EMAIL AUTOMATED READY
      * @param site SITE NAME
      */
     public void sendReport(String site, Report.ReportType type) {

         System.out.println(client
                 .target("http://localhost:5000/api/email/report/send")
                 .queryParam("site", site)
                 .queryParam("report", type)
                 .request(MediaType.APPLICATION_JSON)
                 .get(String.class));

     }
 }
