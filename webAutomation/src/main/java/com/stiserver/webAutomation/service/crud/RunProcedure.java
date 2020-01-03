package com.stiserver.webAutomation.service.crud;

import com.stiserver.webAutomation.utils.ConnectingToDB;
import java.sql.CallableStatement;
import java.sql.SQLException;

public class RunProcedure {

    public static void runNetwork_Analysis_Badger(ConnectingToDB conn){


        System.out.println("Running Procedure: Network_Analysis.Badger....");

        try(CallableStatement stmt = conn.getConnection().prepareCall("{call network_analysis.badger}")) {

        stmt.executeQuery();
            System.out.println("Procedure executed successfully");
        }
        catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("Transaction is being rolled back.");
                conn.getConnection().rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public static void runNetwork_Analysis_Sensus(ConnectingToDB conn) {
        System.out.println("Running Procedure: Network_Analysis.Sensus....");

        try(CallableStatement stmt = conn.getConnection().prepareCall("{call network_analysis.Sensus}")) {

            stmt.executeQuery();
            System.out.println("Procedure executed successfully");
        }
        catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("Transaction is being rolled back.");
                conn.getConnection().rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
