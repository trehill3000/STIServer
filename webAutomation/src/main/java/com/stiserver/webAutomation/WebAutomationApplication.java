package com.stiserver.webAutomation;

import com.opencsv.CSVReader;
import com.stiserver.webAutomation.bLogic.ModifyBadgerReports;
import com.stiserver.webAutomation.bLogic.ModifySensusReports;
import com.stiserver.webAutomation.bLogic.WebBadger;
import com.stiserver.webAutomation.bLogic.WebSensus;
import com.stiserver.webAutomation.model.SiteInfoBadger;
import com.stiserver.webAutomation.model.SiteInfoSensus;
import com.stiserver.webAutomation.service.DB_CRUD.DeleteFromTable;
import com.stiserver.webAutomation.service.DB_CRUD.InsertIntoTable;
import com.stiserver.webAutomation.service.DB_CRUD.RunProcedure;
import com.stiserver.webAutomation.service.DB_CRUD.SelectFromView;
import com.stiserver.webAutomation.service.DirPathFinder;
import com.stiserver.webAutomation.service.RestClient;
import com.stiserver.webAutomation.utils.ConnectToPost;
import com.stiserver.webAutomation.utils.ConnectingToDB;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

@SpringBootApplication
public class WebAutomationApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(WebAutomationApplication.class, args);
		main2();
	}

	public static void main2() throws Exception {

		//GET BADGER SITE INFO FROM DB
		ConnectToPost conn = new ConnectToPost();

		ArrayList<SiteInfoBadger> site_info_Badger = new ArrayList<>();
		PreparedStatement preparedStatement = conn.getConnection().prepareStatement("SELECT b.site_name, s.rni, s.username, s.password, b.avail_api, b.prepro_api,b.pro_api, b.leak_api, b.tamper_api, b.encoder_api, b.additional_login, l.vendor FROM site_login s, badger_api b, site l WHERE l.site_id = s.site_id and s.site_id = b.site_id;");
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			String site_name = resultSet.getString("site_name");
			String rni = resultSet.getString("rni");
			String username = resultSet.getString("username");
			String password = resultSet.getString("password");
			String avail_api = resultSet.getString("avail_api");
			String prepro_api = resultSet.getString("prepro_api");
			String pro_api = resultSet.getString("pro_api");
			String leak_api = resultSet.getString("leak_api");
			String tamper_api = resultSet.getString("tamper_api");
			String encoder_api = resultSet.getString("encoder_api");
			String additional_login = resultSet.getString("additional_login");
			String vendor = resultSet.getString("vendor");
			SiteInfoBadger s = new SiteInfoBadger(site_name, rni, username, password, vendor, avail_api, prepro_api, pro_api, leak_api, tamper_api, encoder_api, additional_login);
			site_info_Badger.add(s);
		}

		//LOOP LIST
		for (int i  =0; i <site_info_Badger.size(); i++) {
			//	runBadger(site_info_Badger, i);
		}
	//	runBadger(site_info_Badger, 1);

        //<><><<><><><><><><><><><><>_________SENSUS_____<><><><><><><><><><><><><><><><><><><_________________________________<><><<><><><><><><><><><><>_________SENSUS_____<><><><><><><><><><><><><><><><><><><______________________________________

		//GET SENSUS SITE INFO FROM DB
		ArrayList<SiteInfoSensus> site_sensus = new ArrayList<>();
		PreparedStatement preparedStatement1 = conn.getConnection().prepareStatement("select l.site_name, l.rni, l.username, l.password from site s, site_login l where s.site_id = l.site_id and lower( s.vendor) = 'sensus';");
		ResultSet resultSet1 = preparedStatement1.executeQuery();
		while (resultSet1.next()) {
			String site_name = resultSet1.getString("site_name");
			String rni = resultSet1.getString("rni");
			String username = resultSet1.getString("username");
			String password = resultSet1.getString("password");
			SiteInfoSensus ss = new SiteInfoSensus(site_name, rni, username, password);
			site_sensus.add(ss);
		}
		conn.close();//System.out.println(site_sensus);

		//LOOP LIST
		for (int i  = 0; i < site_sensus.size(); i++) {
		//	runSensus(site_sensus, i);
		}
    	runSensus(site_sensus, 1);
	}
	private static void runBadger(ArrayList<SiteInfoBadger> site, int i) throws Exception {

		 System.out.println("_____" + site.get(i).getSite_name() + "___STARTED");

	     	//GET NETWORK REPORTS FOR WEB
			WebBadger downloadReport = new WebBadger();
			downloadReport.Badger(DirPathFinder.networkDownloadPath(site.get(i).getSite_name()),site.get(i));


				//PARSE .CSV NETWORK REPORT
				ModifyBadgerReports report = new ModifyBadgerReports(downloadReport.getPath(), site.get(i).getSite_name());
				report.process();

				//MOVE ON IF REPORTS WERE DOWNLOADED

				//CONNECT TO DB
					ConnectingToDB conn = new ConnectingToDB(site.get(i).getSite_name());

				//DELETE EXISTING DATA
		     DeleteFromTable.deleteFromTable(conn, "beacon");

				//INSERT NETWORK REPORT INTO TABLE
		     InsertIntoTable.beacon(conn, report.getModifiedNetworkReport());

				//		//RUN PROCEDURE
			//	RunProcedure.runNetwork_Analysis_Badger(conn);

				//GET NETWORK ANALYSIS VIEW
						SelectFromView.V_Network_Analysis(conn);

					       conn.close();

				//LET EMAIL SERVER KNOW THE NA IS COMPLETE
			//	RestClient r = new RestClient();

			//	r.sendReport(site.get(i).getSite_name(), report.getModifiedNetworkReport().getReportType());
			//	System.out.println("_____" + site.get(i).getSite_name() + "___COMPLETE.");

				//SEND LEAK REPORT
				//r.sendReport(sites.get(index)[0], new BadgerLeakReport());

		}
    /*SENSUS*/
	public static void runSensus(ArrayList<SiteInfoSensus> site_sensus, int i) throws Exception {

		//GET NETWORK REPORTS FOR WEB
		WebSensus downloadReport = new WebSensus();
		downloadReport.sensus(DirPathFinder.networkDownloadPath(site_sensus.get(i).getSite_name()), site_sensus.get(i));


			//PARSE .CSV NETWORK REPORT
			ModifySensusReports report = new ModifySensusReports(downloadReport.getPath(), site_sensus.get(i).getSite_name());
			report.processSensus();

			//CONNECT TO DB
		    ConnectingToDB conn = new ConnectingToDB(site_sensus.get(i).getSite_name());

			//DELETE EXISTING DATA
				DeleteFromTable.deleteFromTable(conn, "Sensusimr");

			//INSERT NETWORK REPORT INTO TABLE
				InsertIntoTable.sensusimr(conn, report.getModifiedNetworkReport());

			//RUN PROCEDURE
				//RunProcedure.runNetwork_Analysis_Sensus(conn);

			//GET NETWORK ANALYSIS VIEW
		SelectFromView.V_Network_Analysis(conn);

				conn.close();

			/*try {//LET EMAIL SERVER KNOW THE NA IS COMPLETE
				RestClient c1 = new RestClient();
				c1.sendReport(site_sensus.get(i).getSite_name(), report.getModifiedNetworkReport().getReportType());

				System.out.println("_____" + site_sensus.get(i).getSite_name() + "___COMPLETE.");
			}catch (Exception ignored){};*/


			//RestClient c2 = new RestClient();
			//c2.sendReport(sites.get(index)[0], report.getLeakReport());

	}
}

