package com.stiserver.webAutomation.utils;

import com.stiserver.webAutomation.service.DB_crud.DeleteFromTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TEST {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        //CONNECT TO DB
        ConnectingToDB conn = new ConnectingToDB("columbia");
        Statement stmt = conn.getConnection().createStatement();

        //GET DATA
        String sql = "select exp_data, route_key from v_network_analysis";
        ResultSet rs = stmt.executeQuery(sql);

        //HOUSE DATA
        List<String> openIssue = new ArrayList<>();
        List<String> closedIssue = new ArrayList<>();
        List<String> autoClosedIssue = new ArrayList<>();

        //Extract data from result set
        while (rs.next()) {
            //Retrieve by column name
            String exp_data = rs.getString("exp_data");
            String route_key = rs.getString("route_key");
           // System.out.println(rs.getString("exp_data") + "," + rs.getString("route_key"));

            switch (route_key) {
                case "Open":
                    openIssue.add(exp_data);
                    break;
                case "Closed":
                    closedIssue.add(exp_data);
                    break;
                case "Auto Closed":
                    autoClosedIssue.add(exp_data);
                    break;
            }
        }


        conn.close();
    }
}