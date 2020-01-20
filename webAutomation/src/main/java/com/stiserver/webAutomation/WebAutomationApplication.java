package com.stiserver.webAutomation;

import com.opencsv.CSVReader;
import com.stiserver.webAutomation.bLogic.ModifyBadgerReports;
import com.stiserver.webAutomation.bLogic.ModifySensusReports;
import com.stiserver.webAutomation.bLogic.WebBadger;
import com.stiserver.webAutomation.bLogic.WebSensus;
import com.stiserver.webAutomation.service.DB_crud.DeleteFromTable;
import com.stiserver.webAutomation.service.DB_crud.InsertIntoTable;
import com.stiserver.webAutomation.service.DB_crud.RunProcedure;
import com.stiserver.webAutomation.service.DB_crud.SelectFromView;
import com.stiserver.webAutomation.service.DirPathFinder;
import com.stiserver.webAutomation.service.RestClient;
import com.stiserver.webAutomation.utils.ConnectingToDB;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileReader;
import java.util.ArrayList;

@SpringBootApplication
public class WebAutomationApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(WebAutomationApplication.class, args);

		main2();
	}

	public static void main2() throws Exception {

		//Load all sites from .csv
		ArrayList<String[]> sites = new ArrayList<>();

		try (CSVReader reader = new CSVReader((new FileReader("C:\\Users\\UMS\\OneDrive - UMS\\SIServer\\Sites\\sites.csv")))) {
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				sites.add(nextLine);
			}
		}
		for (int i  =2; i < sites.size(); i++) {
			//run(sites, i);
		}
		run(sites, 1);
	}

	private static void run(ArrayList<String[]> sites, int index) throws Exception {

		//BADGER
		if (sites.get(index)[4].toLowerCase().trim().equals("badger")) {

			//GET NETWORK REPORTS FOR WEB
			WebBadger downloadReport = new WebBadger();
			downloadReport.Badger(DirPathFinder.networkDownloadPath(sites.get(index)[0]), sites.get(index)[2], sites.get(index)[3], sites.get(index)[0]);

			//PARSE .CSV NETWORK REPORT
			ModifyBadgerReports report = new ModifyBadgerReports(downloadReport.getPath(), sites.get(index)[0]);
			report.processBadger();

			//CONNECT TO DB
			ConnectingToDB conn = new ConnectingToDB(sites.get(index)[0]);

			//DELETE EXISTING DATA
			DeleteFromTable.deleteFromTable(conn, "beacon");

			//INSERT NETWORK REPORT INTO TABLE
			InsertIntoTable.beacon(conn, report.getModifiedNetworkReport());

			//RUN PROCEDURE
			RunProcedure.runNetwork_Analysis_Badger(conn);
//
			//GET NETWORK ANALYSIS VIEW
			SelectFromView.V_Network_Analysis(conn);

			conn.close();

			//LET EMAIL SERVER KNOW THE NA IS COMPLETE
			//RestClient r = new RestClient();
		//	r.sendReport(sites.get(index)[0], report.getModifiedNetworkReport().getReportType());

			//SEND LEAK REPORT
			//r.sendReport(sites.get(index)[0], new BadgerLeakReport());

		}
		/*SENSUS*/
		else if (sites.get(index)[4].toLowerCase().trim().equals("sensus")) {

			//GET NETWORK REPORTS FOR WEB
			WebSensus connectingTo = new WebSensus();
			connectingTo.sensus(DirPathFinder.networkDownloadPath(sites.get(index)[0]), sites.get(index)[2], sites.get(index)[3], sites.get(index)[1]);

			//PARSE .CSV NETWORK REPORT
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

			//LET EMAIL SERVER KNOW THE NA IS COMPLETE
		//	RestClient c1 = new RestClient();
		//	c1.sendReport(sites.get(index)[0], report.getModifiedNetworkReport());

		   //RestClient c2 = new RestClient();
			//c2.sendReport(sites.get(index)[0], report.getLeakReport());
		}
	}
}
