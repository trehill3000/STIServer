package com.stiserver.webAutomation.utils;

import java.sql.Connection;
import java.sql.Statement;

public class testing {
    public static void main(String[] args) throws Exception {

      //  CsvReader reader = new CsvReader();
        //LinkedHashMap<String, ArrayList<String>> map = reader.readCSVHash();

        //pass credentials to connect to DB
        ConnectingToDB c = new ConnectingToDB("davie");
        Connection conn = c.getConnection();
        Statement stmt = conn.createStatement();

     //   ResultSet rs = stmt.executeQuery("SELECT * FROM dd");
        //stmt.executeQuery("INSERT INTO dd VALUES ('1221','3333')");
    }
}
