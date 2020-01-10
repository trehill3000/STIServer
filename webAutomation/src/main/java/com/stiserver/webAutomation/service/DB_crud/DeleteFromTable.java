package com.stiserver.webAutomation.service.DB_crud;

import com.stiserver.webAutomation.utils.ConnectingToDB;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteFromTable {

    /**
     * GIVE TABLE NAME AND DELETE
     * @param conn CONNECTION
     * @param table TABLE NAME
     * @throws SQLException E
     */
    public static void deleteFromTable(ConnectingToDB conn, String table) throws SQLException {

        System.out.println("\nDELETE FROM "+ table + " table....");

        try(Statement stmt = conn.getConnection().createStatement()) {
            String sql = "DELETE FROM " + table;
            stmt.executeUpdate(sql);
            System.out.println("DELETE executed successfully");
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
