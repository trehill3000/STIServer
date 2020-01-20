package com.stiserver.webAutomation.service.DB_crud;

        import com.opencsv.CSVWriter;
        import com.stiserver.webAutomation.service.DirPathFinder;
        import com.stiserver.webAutomation.utils.ConnectingToDB;

        import java.io.FileWriter;
        import java.io.IOException;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.sql.Statement;
        import java.text.SimpleDateFormat;
        import java.util.*;

public class SelectFromView {

    /**
     * GET DATA FROM VIEW
     * @param conn CONNECTION
     * @throws IOException C
     * @throws SQLException C
     */
    public static void V_Network_Analysis(ConnectingToDB conn) throws IOException, SQLException {

        Statement stmt = conn.getConnection().createStatement();

        //GET DATA FROM EXP_TYPE
        String sql = "select exp_data, route_key from v_network_analysis";
        ResultSet rs = stmt.executeQuery(sql);

        //HOUSE DATA IN LIST
        List<String[]> openIssue = new ArrayList<>();
        List<String[]> closedIssue = new ArrayList<>();

        //EXTRACT DATA FROM LIST
        while (rs.next()) {

            //Retrieve by column name
            String exp_data = rs.getString("exp_data");
            String route_key = rs.getString("route_key");
            //  System.out.println(rs.getString("exp_data") + "," + rs.getString("route_key"));

            switch (route_key.toUpperCase()) {
                case "OPEN":
                    //Add csv string and split commas
                    List<String> parsedString = new ArrayList<>(Arrays.asList(exp_data.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")));
                    String [] s = new String[parsedString.size()];

                    //Loop through csv string into list
                    for(int i =0; i < parsedString.size(); i ++){
                        s[i] = parsedString.get(i);
                    }
                    openIssue.add(s);
                    // System.out.println(Arrays.toString(s));

                    break;
                case "CLOSED":
                    //Add csv string and split commas
                    List<String> parsedString2 = new ArrayList<>(Arrays.asList(exp_data.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")));
                    String [] s2 = new String[parsedString2.size()];

                    //Loop through csv string into list
                    for(int word =0; word < parsedString2.size(); word ++){
                        s2[word] = parsedString2.get(word);
                    }
                    closedIssue.add(s2);
                    // System.out.println(Arrays.toString(s));

                    break;
            }
        }

        //WRITE LIST DATA TO .CSV FILE___________________________________________OPEN ISSUE__________________________________________________________________
        //GET CURRENT DATE
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

        //WRITE TO THE .CSV FILE
        CSVWriter writer = new CSVWriter(new FileWriter(DirPathFinder.networkSendPath(conn.getSiteName()) + conn.getSiteName()  +"_Network_Analysis_OPEN_" + formatter.format(new Date()) +".csv",true));
        //WRITE TO THE .CSV FILE
        openIssue.forEach(s -> writer.writeAll(Collections.singleton(s)));

        writer.close();


        //WRITE LIST DATA TO .CSV FILE___________________________________________CLOSED ISSUE____________________________________________________________________

        //WRITE TO THE .CSV FILE
        CSVWriter writer1 = new CSVWriter(new FileWriter(DirPathFinder.networkSendPath(conn.getSiteName()) + conn.getSiteName()  +"_Network_Analysis_CLOSED_" + formatter.format(new Date()) +".csv",true));
        //WRITE TO THE .CSV FILE
        closedIssue.forEach(s -> writer1.writeAll(Collections.singleton(s)));

        writer1.close();

    }
}
