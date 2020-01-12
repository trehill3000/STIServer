package com.stiserver.webAutomation.model;

import java.io.File;
import java.util.List;

public class BadgerLeakReport implements Report {

    private File file;
    private List<String[]> data;

    public BadgerLeakReport(File file, List<String[]> data) {
        this.file = file;
        this.data = data;
    }

    public File getFile() {
        return file;
    }

    public List<String[]> getData() {
        return data;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setData(List<String[]> data) {
        this.data = data;
    }

    @Override
    public List<String[]> getList() {
        return data;
    }

    @Override
    public String getName() {
        return "BadgerLeakReport";
    }

    /**
     * RETURN ENUMS REPORT TYPE
     * @return BADGER NETWORK REPORT
     */
    public ReportType getReportType(){
        return ReportType.LEAKS;
    }

    @Override
    public String getPath() {
        return file.getPath();
    }
}
