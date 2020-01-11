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
      * @param report REPORT NAME
      */
     public void sendNetworkAnalysis(String site, Report report) {

         String REST_URI = "http://localhost:8080/api/email/networkanalysis";

         System.out.println(client
                 .target(REST_URI)
                 .queryParam("site", site)
                 .queryParam("report", report.getName())
                 .request(MediaType.APPLICATION_JSON)
                 .get(String.class));

     }

     /**
      * SEND LEAK REPORT IS EMAIL AUTOMATED READY
      * @param site SITE NAME
      * @param report REPORT NAME
      */
     public void sendLeakReport(String site, Report report){

         String REST_URI = "http://localhost:8080/api/email/leaks";

         System.out.println(client
                 .target(REST_URI)
                 .queryParam("site", site)
                 .queryParam("report", report.getName())
                 .request(MediaType.APPLICATION_JSON)
                 .get(String.class));

     }

     /**
      * SEND BACK FLOW REPORT IS EMAIL AUTOMATED READY
      * @param site SITE NAME
      * @param report REPORT NAME
      */
     public void sendBackFlowReport(String site, Report report){

         String REST_URI = "http://localhost:8080/api/email/backflow";

         System.out.println(client
                 .target(REST_URI)
                 .queryParam("site", site)
                 .queryParam("report", report.getName())
                 .request(MediaType.APPLICATION_JSON)
                 .get(String.class));
     }

     /**
      * SEND TAMPER REPORT IS EMAIL AUTOMATED READY
      * @param site SITE NAME
      * @param report REPORT NAME
      */
     public void sendTamperReport(String site, Report report){

         String REST_URI = "http://localhost:8080/api/email/tamper";

         System.out.println(client
                 .target(REST_URI)
                 .queryParam("site", site)
                 .queryParam("report", report.getName())
                 .request(MediaType.APPLICATION_JSON)
                 .get(String.class));
     }

     /**
      * SEND ENCODER REPORT IS EMAIL AUTOMATED READY
      * @param site SITE NAME
      * @param report REPORT NAME
      */
     public void sendEncoderReport(String site, Report report){

         String REST_URI = "http://localhost:8080/api/email/encoder";

         System.out.println(client
                 .target(REST_URI)
                 .queryParam("site", site)
                 .queryParam("report", report.getName())
                 .request(MediaType.APPLICATION_JSON)
                 .get(String.class));
     }

 }
