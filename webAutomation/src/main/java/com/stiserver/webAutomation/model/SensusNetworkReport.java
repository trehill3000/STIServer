package com.stiserver.webAutomation.model;

import java.io.File;
import java.util.List;

public class SensusNetworkReport implements Report{

    private File file;
    private List<String[]> data;

    public SensusNetworkReport(File file, List<String[]> data) {
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
        return file.getName();
    }

    @Override
    public String getPath() {
        return file.getPath();
    }
}
