package com.stiserver.webAutomation.utils;

import com.stiserver.webAutomation.service.DB_crud.SelectFromView;

import java.io.IOException;
import java.sql.SQLException;

public class TEST2 {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {

        ConnectingToDB conn = new ConnectingToDB("Columbia");
        SelectFromView.V_Network_Analysis(conn);
    }
}
