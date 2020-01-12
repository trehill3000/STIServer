package com.stiserver.webAutomation.model;

import java.util.List;

public interface Report {

     enum ReportType{
          NETWORKANALYSIS,
          LEAKS,
          TAMPER,
          ENCODER,
          BACKFLOW,
          FPR

     }

     List<String[]> getList();

}
