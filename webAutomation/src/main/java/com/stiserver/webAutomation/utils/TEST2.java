package com.stiserver.webAutomation.utils;

import com.stiserver.webAutomation.model.SiteInfoBadger;
import com.stiserver.webAutomation.service.DB_CRUD.SelectFromView;
import com.stiserver.webAutomation.service.DirPathFinder;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class TEST2 {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException, ParseException {

       boolean d =  DirPathFinder.checkIfDownloaded(DirPathFinder.networkDownloadPath("agua"));

        System.out.println(d);


    }
}
