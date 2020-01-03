package com.stiserver.webAutomation.bLogic;
import com.opencsv.CSVReader;

import com.stiserver.webAutomation.bLogic.ModifyBadgerReports;
import com.stiserver.webAutomation.bLogic.ModifySensusReports;
import com.stiserver.webAutomation.bLogic.WebBadger;
import com.stiserver.webAutomation.bLogic.WebSensus;
import com.stiserver.webAutomation.service.crud.DeleteFromTable;
import com.stiserver.webAutomation.service.crud.InsertIntoTable;
import com.stiserver.webAutomation.service.crud.RunProcedure;
import com.stiserver.webAutomation.utils.ConnectingToDB;

import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) throws Exception {

        //Load all sites from .csv
        ArrayList<String[]> sites = new ArrayList<>();

        try (CSVReader reader = new CSVReader((new FileReader("src\\sites.csv")))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                sites.add(nextLine);
            }
        }
        for (int i = 1; i < sites.size(); i++) {
            //  run(sites, i);
        }
        run(sites, 2);
    }

    private static void run(ArrayList<String[]> sites, int index) throws Exception {

        if (sites.get(index)[5].toLowerCase().trim().equals("badger")) {

            //GET REPORTS FOR WEB
            WebBadger connectingTo = new WebBadger();
            connectingTo.Badger(sites.get(index)[4], sites.get(index)[2], sites.get(index)[3], sites.get(index)[0]);

            //PARSE .CSV REPORT
            ModifyBadgerReports report = new ModifyBadgerReports(connectingTo.getPath(), sites.get(index)[0]);
            report.processBadger();

            //CONNECT TO DB
            ConnectingToDB conn = new ConnectingToDB(sites.get(index)[0]);

            //DELETE EXISTING DATA
            DeleteFromTable.deleteFromTable(conn, "beacon");

            //INSERT NETWORK REPORT INTO TABLE
            InsertIntoTable.beacon(conn, report.getModifiedNetworkReport());

            //RUN PROCEDURE
            RunProcedure.runNetwork_Analysis_Badger(conn);



            conn.close();

        } else if (sites.get(index)[5].toLowerCase().trim().equals("sensus")) {

            //GET REPORTS FOR WEB
            WebSensus connectingTo = new WebSensus();
            connectingTo.sensus(sites.get(index)[4], sites.get(index)[2], sites.get(index)[3], sites.get(index)[1]);

            //PARSE .CSV REPORT
            ModifySensusReports report = new ModifySensusReports(connectingTo.getPath(), sites.get(index)[0]);
            report.processSensus();

            //CONNECT TO DB
            ConnectingToDB conn = new ConnectingToDB(sites.get(index)[0]);

            //DELETE EXISTING DATA
            DeleteFromTable.deleteFromTable(conn, "Sensusimr");

            //INSERT NETWORK REPORT INTO TABLE
            InsertIntoTable.sensusimr(conn, report.getModifiedNetworkReport());

            //RUN PROCEDURE
            RunProcedure.runNetwork_Analysis_Sensus(conn);

            conn.close();
        }
    }
}
