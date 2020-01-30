package com.stiserver.webAutomation.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * MAPS TO CURRENT DIR IN ONE DRIVE
 */
public class DirPathFinder {

    //__________NETWORK
    public static String networkDownloadPath(String siteName){
        return "C:\\Users\\UMS\\OneDrive - UMS\\SIServer\\Sites\\Active\\"+siteName+"\\Network Analysis\\Downloaded Reports"; }
    public static String networkModPath(String sitename) {

        return "C:\\Users\\UMS\\OneDrive - UMS\\SIServer\\Sites\\Active\\" + sitename + "\\Network Analysis\\Modified Reports";
    }
    public static String networkSendPathOpened(String sitename){

        return "C:\\Users\\UMS\\OneDrive - UMS\\SIServer\\Sites\\Active\\"+ sitename+"\\Network Analysis\\SEND\\open\\";
    }
    public static String networkSendPathClosed(String sitename){

        return "C:\\Users\\UMS\\OneDrive - UMS\\SIServer\\Sites\\Active\\"+ sitename+"\\Network Analysis\\SEND\\closed\\";
    }
    public static String networkSendPathAuto(String sitename){

        return "C:\\Users\\UMS\\OneDrive - UMS\\SIServer\\Sites\\Active\\"+ sitename+"\\Network Analysis\\SEND\\auto_closed\\";
    }

    //____________FPR
    public static String fprSend(String name){

        return "C:\\Users\\UMS\\OneDrive - UMS\\SIServer\\Sites\\Active\\"+name+"\\FPR\\SEND";
    }

    //____________ANALYTICS
    public static String backFlowSendPath(String name){

        return "C:\\Users\\UMS\\OneDrive - UMS\\SIServer\\Sites\\Active\\"+ name+"\\ANALYTICS\\BACKFLOW\\";
    }

    public static String encoderSendPath(String name){
        return "C:\\Users\\UMS\\OneDrive - UMS\\SIServer\\Sites\\Active\\"+name+"\\ANALYTICS\\Encoder\\";

    }

    public static String leakSendPath(String name){
        return "C:\\Users\\UMS\\OneDrive - UMS\\SIServer\\Sites\\Active\\"+name+"\\ANALYTICS\\leaks\\";
    }

    public static String tamperSendPath(String name){
        return "C:\\Users\\UMS\\OneDrive - UMS\\SIServer\\Sites\\Active\\"+name+"\\ANALYTICS\\tamper\\";

    }

    /**
     * GET MOST RECENTLY MODIFIED FILE IN A DIR
     * @param fileName FILE
     * @return MOD FILE
     * @throws ParseException V
     * @throws IOException V
     */
    public static File getLastModFile(String fileName) throws ParseException, IOException {
        //GET STRING NAME OF FILES IN A DIR
        Stream<Path> walk = Files.walk(Paths.get(fileName));
        List<String> result = walk.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());
        //result.forEach(System.out::println);
        File lastModFile = null;

        //SET STRING TO FILE obj
        ArrayList<File> tempFileName = new ArrayList<>();
        for (String s : result) {
            tempFileName.add(new File(s));
        }

        //GET THE LAST MODIFIED DATE
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date1 = sdf.parse(sdf.format(tempFileName.get(0).lastModified()));//<--default date

        for (File file : tempFileName) {
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

        return lastModFile;
    }
}
