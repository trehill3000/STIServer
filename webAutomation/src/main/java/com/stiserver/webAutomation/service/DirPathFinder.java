
package com.stiserver.webAutomation.service;

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
    public static String networkSendPath(String sitename){

        return "C:\\Users\\UMS\\OneDrive - UMS\\SIServer\\Sites\\Active\\"+ sitename+"\\Network Analysis\\SEND";
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




}
