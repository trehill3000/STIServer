package com.stiserver.webAutomation.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Upon login, this class will validate the connection and return True or False
 * -Create and instance of the connection.
 */
public class ConnectToPost {

    private Connection conn; //Final connection for this login. Will be used and passed/referenced in all classes.
    private boolean isConnected = false;

    /**
     * Attempts to connect to DB.
     * Take the data from login.fxml and connect to DB
     */
    public ConnectToPost() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/email_test", "thill", "2550");

        if (conn != null)
        {
            System.out.println("::::::::::::::::::::::::::::::::::::\n" +
                                "Connected to Postgresql database!\n::::::::::::::::::::::::::::::::::::");
            isConnected = true;
        } else
        {
            System.out.println(":*****:\nFailed to make connection!\n:*****:");
        }
    }

    /**
     * Is connected to DB
     * @return True or False
     */
    public boolean isConnected()
    {
        return isConnected;
    }

    /**
     * Pass connection to not make or recreate another connection.
     * @return connection to DB
     */
    public Connection getConnection()
    {
        return conn;
    }

    public void close() throws SQLException {conn.close();}
}
