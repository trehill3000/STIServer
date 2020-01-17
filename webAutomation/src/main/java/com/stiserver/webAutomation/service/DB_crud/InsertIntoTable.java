package com.stiserver.webAutomation.service.DB_crud;
import com.stiserver.webAutomation.model.Report;
import com.stiserver.webAutomation.utils.ConnectingToDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * INSERT DATA INTO DB
 * We only need to know the site, table name and data to insert
 */
public class InsertIntoTable {

    /**
     *  INSERT INTO BEACON TABLE
     * @param c CONNECTION
     * @param d DATA TO INSERT
     * @throws SQLException E
     */
    public static void beacon(ConnectingToDB c, Report d) throws SQLException {

        //DATA TO INSERT
        List<String[]> data = new ArrayList<>(d.getList());

        //CONNECT TO DB
        Connection conn = c.getConnection();

        //INSERT INTO DB //https://stackify.com/streams-guide-java-8/
        //https://www.boraji.com/jdbc-batch-insert-example
        System.out.println("INSERTING "+data.size() +" records into the table...");

        String INSERT_SQL = "INSERT INTO BEACON "
                + "(MIU,LAST_READ_DATE,READING,SHIP_DATE,TYPE,FIRMWARE_VERSION,LAST_MESSAGE_DATE,ACTIVATION_DATE,REPORT,METER_KEY) VALUES (?,?,?,?,?,?,?,?,?,?)";

        conn.setAutoCommit(false); // default true
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {

            // Insert sample records
            int counter = 1;
            for (int i = 0; i < data.size(); i++) {
                stmt.setString(1, data.get(i)[0]);
                stmt.setString(2, data.get(i)[1]);
                stmt.setString(3, data.get(i)[2]);
                stmt.setString(4, data.get(i)[3]);
                stmt.setString(5, data.get(i)[4]);
                stmt.setString(6, data.get(i)[5]);
                stmt.setString(7, data.get(i)[6]);
                stmt.setString(8, data.get(i)[7]);
                stmt.setString(9, data.get(i)[8]);
                stmt.setString(10, "");

                //Add statement to batch
                stmt.addBatch();

                //Execute batch of 1000 records
                if (i % 2000 == 0) {
                    stmt.executeBatch();
                    conn.commit();
                    System.out.println("Batch " + (counter++) + " executed successfully");
                }
            }
            //execute final batch
            stmt.executeBatch();
            conn.commit();
            System.out.println("Final batch executed successfully\n");
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("Transaction is being rolled back.");
                conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void sensusimr(ConnectingToDB c, Report d) throws SQLException {

        //DATA TO INSERT
        List<String[]> data = new ArrayList<>(d.getList());

        //CONNECT TO DB
        Connection conn = c.getConnection();

        //INSERT INTO DB //https://stackify.com/streams-guide-java-8/
        //https://www.boraji.com/jdbc-batch-insert-example
        System.out.println("Inserting records into the table...");

        String INSERT_SQL = "INSERT INTO SENSUSIMR "
                + "(FLEXNET_ID,METER_ID,LATITUDE,LONGITUDE,DIR_SNR,TRANSMIT_INT,LAST_READ_HRS,LIFECYCLE_STATE,LIFECYCLE_DATE,READING,PROGRAMMED_UNITS,PROGRAMMED_RESOLUTION,LAST_READ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        conn.setAutoCommit(false); // default true
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {

            // Insert sample records
            int counter = 1;
            for (int i = 0; i < data.size(); i++) {
                stmt.setString(1, data.get(i)[0]);
                stmt.setString(2, data.get(i)[1]);
                stmt.setString(3, data.get(i)[2]);
                stmt.setString(4, data.get(i)[3]);
                stmt.setString(5, data.get(i)[4]);
                stmt.setString(6, data.get(i)[5]);
                stmt.setString(7, data.get(i)[6]);
                stmt.setString(8, data.get(i)[7]);
                stmt.setString(9, data.get(i)[8]);
                stmt.setString(10, data.get(i)[9]);
                stmt.setString(11, data.get(i)[10]);
                stmt.setString(12, data.get(i)[11]);
                stmt.setString(13, data.get(i)[12]);

                //Add statement to batch
                stmt.addBatch();

                //Execute batch of 1000 records
                if (i % 1050 == 0) {
                    stmt.executeBatch();
                    conn.commit();
                    System.out.println("Batch " + (counter++) + " executed successfully");
                }
            }
            //execute final batch
            stmt.executeBatch();
            conn.commit();
            System.out.println("Final batch executed successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("Transaction is being rolled back.");
                conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
