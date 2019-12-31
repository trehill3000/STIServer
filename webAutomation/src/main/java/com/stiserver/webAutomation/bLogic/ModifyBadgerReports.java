package com.stiserver.webAutomation.bLogic;


import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.stiserver.webAutomation.model.BadgerLeakReport;
import com.stiserver.webAutomation.model.BadgerNetworkReport;
import com.stiserver.webAutomation.service.CsvReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModifyBadgerReports {

/**
 * STILL TESTING
 * Open most recently downloaded reports and edit them
 * Save edit report to another dir location
 * Set the instance variable of this modified file.
 */
    private String path;
    private File preProReport;
    private File proReport;
    private File modifiedReport;
    private String siteName;
    private List<String[]> allRows = new ArrayList<>();

    /**
     * Primarley used for Badger Report.
     */
    public ModifyBadgerReports(String path, String s) {
        this.path = path;
        siteName = s;
    }

    /**
     * Process Badger files to be ready to insert into DB
     * @throws IOException e
     */
    public void processBadger() throws IOException, ParseException, CsvValidationException {
        setBadgerFiles();
        readCsv(path, preProReport.getName());
        editPreProReport();
        readCsv(path, proReport.getName());
        editProReport();
    }

    /**
     * USED TO READ .CSV FILE
     * Get List<String[]> allRows</String[]>
     * Will receive new instance allocated in memory of .csv file.
     * @param path     file explorer
     * @param fileName file name
     * @throws IOException e
     */
    private void readCsv(String path, String fileName) throws IOException, CsvValidationException {
        CsvReader reader = new CsvReader();
        allRows = reader.readCsv(new FileReader(path + "\\" + fileName));

     //   allRows.forEach(s-> System.out.println(Arrays.toString(s)));
    }

    /**
     * REMOVE COLUMN "Encoder ID".
     * Parse .csv and abstract data to map to assist with removing the "encoder id" column by finding it by name.
     * Take map and stitch it back together for CSV WRITER
     * @throws IOException e
     */
    private void editPreProReport() throws IOException {

        CsvReader reader = new CsvReader();

        allRows = reader.removeColumn(allRows, "Encoder ID");

        allRows = reader.changeColumnName(allRows, "All Dates in US/Eastern", "Report");

        //CUSTOM EDIT TO FILE. ADD PRE PROVISIONED TO THE END OF FILE.
        for(int i =1; i < allRows.size();i++) {
            allRows.get(i)[8] = "PRE PROVISIONED";
        }
        //TEST PRINT
     //   allRows.forEach(n -> System.out.println(Arrays.toString(n)));

        //GET CURRENT DATE
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

        //WRITE TO THE .CSV FILE https://www.javatpoint.com/java-get-current-date
        CSVWriter writer = new CSVWriter(new FileWriter("C:\\Users\\UMS\\Documents\\z_New computer\\Sites\\Active\\" + siteName + "\\NETWORK ANALYSIS\\MODIFIED REPORTS\\"+ siteName + "_Network_Analysis_"+ formatter.format(new Date()) +".csv" ,true));

        //WRITE TO THE .CSV FILE
        allRows.forEach(s -> writer.writeAll(Collections.singleton(s)));
        writer.close();
    }

    /**
     * REMOVE COLUMN "Unit/KUnit".
     * Parse .csv and abstract data to map to assist with removing the "encoder id" column by finding it by name.
     * Take map and stitch it back together for CSV WRITER
     * @throws IOException e
     */
    private void editProReport() throws IOException {

        CsvReader reader = new CsvReader();

        allRows = reader.removeColumn(allRows, "Unit");

        //REMOVE HEADER FOR CONCAT TO PRE PRO REPORT
        allRows.remove(0);

        //CUSTOM EDIT TO FILE. ADD PRE PROVISIONED TO THE END OF FILE.
        allRows.forEach(s-> s[8] = "PROVISIONED");

        //TEST PRINT
        //allRows.forEach(n -> System.out.println(Arrays.toString(n)));

        //https://www.javatpoint.com/java-get-current-date
        //GET CURRENT DATE
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

        //WRITE TO THE .CSV FILE
        CSVWriter writer = new CSVWriter(new FileWriter("C:\\Users\\UMS\\Documents\\z_New computer\\Sites\\Active\\" + siteName + "\\NETWORK ANALYSIS\\MODIFIED REPORTS\\"+ siteName + "_Network_Analysis_"+formatter.format(new Date()) +".csv" ,true));

        //WRITE TO THE .CSV FILE
        allRows.forEach(s -> writer.writeAll(Collections.singleton(s)));

        modifiedReport = new File("C:\\Users\\UMS\\Documents\\z_New computer\\Sites\\Active\\" + siteName + "\\NETWORK ANALYSIS\\MODIFIED REPORTS\\"+ siteName + "_Network_Analysis_"+formatter.format(new Date()) +".csv");
        writer.close();
    }

    /**
     * Set the Most recent modified file names for 2 reports.
     * Must find the most recent and second most recent file by date modified.
     */
    private void setBadgerFiles() throws IOException, ParseException {

        //GET STRING NAME OF FILES IN A DIR
        Stream<Path> walk = Files.walk(Paths.get(path));
        List<String> result = walk.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());
        //result.forEach(System.out::println);
        File lastModFile =  null;

        //SET STRING TO FILE obj
        ArrayList<File> tempFileName = new ArrayList<>();
        for(String s: result) {
            tempFileName.add(new File(s));
        }

        //GET THE LAST MODIFIED DATE
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date1 = sdf.parse(sdf.format(tempFileName.get(0).lastModified()));//<--default date

        for(File file: tempFileName) {
            //  System.out.println(" Date1: " + date1);
            //    System.out.println(" Date2: " + sdf.format(file.lastModified()));
            // System.out.println(" Format:" + file + " " + sdf.format(file.lastModified()));
            Date date2 = sdf.parse(sdf.format(file.lastModified()));

            if (date1.compareTo(date2) < 0) {
                //    System.out.println("--Date1 is before Date2");
                date1 = date2;
                lastModFile = file;
            } else if (date1.compareTo(date2) == 0) {
                //    System.out.println("--Date1 is equal to Date2");
                date1 = date2;
                lastModFile = file;
            }
        }
        assert lastModFile != null;
        //System.out.println("Last Modified date1 : " + date1 +": "+ lastModFile.getName());

        proReport = (lastModFile);
        tempFileName.remove(lastModFile);

        date1 = sdf.parse(sdf.format(tempFileName.get(0).lastModified()));//<--default date

        for(File file: tempFileName) {
            //  System.out.println(" Date1: " + date1);
            //    System.out.println(" Date2: " + sdf.format(file.lastModified()));
            // System.out.println(" Format:" + file + " " + sdf.format(file.lastModified()));
            Date date2 = sdf.parse(sdf.format(file.lastModified()));

            if (date1.compareTo(date2) < 0) {
                //    System.out.println("--Date1 is before Date2");
                date1 = date2;
                lastModFile = file;
            } else if (date1.compareTo(date2) == 0) {
                //    System.out.println("--Date1 is equal to Date2");
                date1 = date2;
                lastModFile = file;
            }
        }
        //  System.out.println("Last Modified date2 : " + date1 +": "+ lastModFile.getName());

        preProReport = (lastModFile);
    }

    /**
     * RETURN MODIFIED REPORT FOR DB.
     * @return M
     */
    public BadgerNetworkReport getModifiedNetworkReport() {
        //allRows.forEach(e-> System.out.println(Arrays.toString(e)));

        return new BadgerNetworkReport(modifiedReport, allRows);
    }

    public BadgerLeakReport getModifiedLeakReport() {
        return null;
    }
}

