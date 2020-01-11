package com.stiserver.webAutomation.bLogic;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.stiserver.webAutomation.model.SensusNetworkReport;
import com.stiserver.webAutomation.service.CsvReader;
import com.stiserver.webAutomation.service.DirPathFinder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModifySensusReports {

    private String path;
    private File sensusReport;
    private File modifiedReport;
    private String siteName;
    private List<String[]> allRows = new ArrayList<>();

    /**
     * Primanly used for Sensus Report
     * @param path dir
     */
    public ModifySensusReports(String path, String s) { this.path = path; siteName = s;
    }

    /**
     * Process Sensus file to be ready to insert into DB
     * @throws IOException e
     */
    public void processSensus() throws IOException, ParseException, CsvValidationException {
        setSensusFile();
        readCsv(path, sensusReport.getName());
        editSensusReport();
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
     * REMOVE COLUMNS "City", "Mode".
     * Parse .csv and abstract data to map to assist with removing the "encoder id" column by finding it by name.
     * Take map and stitch it back together for CSV WRITER
     * @throws IOException e
     */
    private void editSensusReport() throws IOException {
        CsvReader reader = new CsvReader();

        //EDIT REPORT
        allRows = reader.removeColumn(allRows, "City");
        allRows = reader.removeColumn(allRows, "Mode");

        //REMOVE HEADER FOR CONCAT TO PRE PRO REPORT
        allRows.remove(0);

        //TEST PRINT
        // allRows.forEach(n -> System.out.println(Arrays.toString(n)));

        //GET CURRENT DATE
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

        //WRITE TO THE .CSV FILE https://www.javatpoint.com/java-get-current-date
    //    CSVWriter writer = new CSVWriter(new FileWriter("C:\\Users\\UMS\\Documents\\z_New computer\\Sites\\Active\\" + siteName + "\\NETWORK ANALYSIS\\MODIFIED REPORTS\\"+ siteName + "_Network_Analysis_"+ formatter.format(new Date()) +".csv" ,true));
        CSVWriter writer = new CSVWriter(new FileWriter(DirPathFinder.networkModPath(siteName) + siteName + "_Network_Analysis_"+ formatter.format(new Date()) +".csv" ,true));

        //WRITE TO THE .CSV FILE
        allRows.forEach(s -> writer.writeAll(Collections.singleton(s)));

        modifiedReport = new File(DirPathFinder.networkModPath(siteName) + siteName + "_Network_Analysis_"+ formatter.format(new Date()) +".csv");

        writer.close();
    }

    /**
     * Set the Most recent modified file names
     * @throws IOException mod fileNames
     */
    private void setSensusFile() throws IOException, ParseException {
        //GET STRING NAME OF FILES IN A DIR
        Stream<Path> walk = Files.walk(Paths.get(path));
        List<String> result = walk.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());
        //result.forEach(System.out::println);
        File lastModFile =  null;

        //SET STRING TO FILE obj
        ArrayList<File> l = new ArrayList<>();
        for(String s: result) {
            l.add(new File(s));
        }
        //GET THE LAST MODIFIED DATE
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date1 = sdf.parse(sdf.format(l.get(0).lastModified()));//<--default date

        for(File file: l) {
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
        // System.out.println("Last Modified date" + date1 +":"+ lastModFile.getName());

        sensusReport = lastModFile;
    }

    /**
     * RETURN MODIFIED REPORT FOR DB.
     * @return M
     */
    public SensusNetworkReport getModifiedNetworkReport() { return new SensusNetworkReport(modifiedReport, allRows); }
}
