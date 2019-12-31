package com.stiserver.webAutomation.repository;

public class BeaconTable {

    private String miu;
    private String last_read_date;
    private String reading;
    private String ship_date;
    private String type;
    private String firmware_version;
    private String last_message_date;
    private String activation_date;
    private String report;
    private String meter_key;

    public BeaconTable(){

    }

    public String getMiu() {
        return miu;
    }

    public void setMiu(String miu) {
        this.miu = miu;
    }

    public String getLast_read_date() {
        return last_read_date;
    }

    public void setLast_read_date(String last_read_date) {
        this.last_read_date = last_read_date;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public String getShip_date() {
        return ship_date;
    }

    public void setShip_date(String ship_date) {
        this.ship_date = ship_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFirmware_version() {
        return firmware_version;
    }

    public void setFirmware_version(String firmware_version) {
        this.firmware_version = firmware_version;
    }

    public String getLast_message_date() {
        return last_message_date;
    }

    public void setLast_message_date(String last_message_date) {
        this.last_message_date = last_message_date;
    }

    public String getActivation_date() {
        return activation_date;
    }

    public void setActivation_date(String activation_date) {
        this.activation_date = activation_date;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getMeter_key() {
        return meter_key;
    }

    public void setMeter_key(String meter_key) {
        this.meter_key = meter_key;
    }
}
