package com.stiserver.webAutomation.utils;

import com.stiserver.webAutomation.model.SiteInfoBadger;
import com.stiserver.webAutomation.service.DB_CRUD.SelectFromView;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TEST2 {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {

        //GET THE EMAILS FROM DB
        ConnectToPost conn = new ConnectToPost();
        ArrayList<SiteInfoBadger> site_info_Badger = new ArrayList<>();

        PreparedStatement preparedStatement = conn.getConnection().prepareStatement("select site_id from site;");
        ResultSet resultSet = preparedStatement.executeQuery();

        conn.close();


    }
}
