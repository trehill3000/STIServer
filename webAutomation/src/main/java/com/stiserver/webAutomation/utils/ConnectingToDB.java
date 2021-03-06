package com.stiserver.webAutomation.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Upon login, this class will validate the connection and return True or False
 * -Create and instance of the connection.
 */
public class ConnectingToDB
{
    private Connection conn; //Final connection for this login. Will be used and passed/referenced in all classes.
    private boolean isConnected = false;
    private String siteName;

    /**
     * Used to create empty class for use of getter methods.
     */
    public ConnectingToDB() {

    }

    /**
     * Attempts to connect to DB.
     * Take the data from login.fxml and connect to DB
     * @param username username
     */
    public ConnectingToDB(String username) throws SQLException, ClassNotFoundException {
        siteName = username;
        Class.forName("oracle.jdbc.driver.OracleDriver");
        conn = DriverManager.getConnection("jdbc:oracle:thin:@172.16.199.126:1521:pontus", username, username + "57$");

        if (conn != null)
        {
            System.out.println("::::::::::::::::::::::::::::::::::::\nConnected to Oracle database!\n::::::::::::::::::::::::::::::::::::");
            isConnected = true;
        } else
        {
            System.out.println(":*****:\nFailed to make connection!\n:******:");
        }
    }

    /**
     * Is connected to DB
     * @return True or False
     */
    public boolean connected()
    {
        return isConnected;
    }


    public String getSiteName(){return siteName;}

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
