package com.stiserver.webAutomation.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SiteInfoBadger {

    private final String site_name;
    private final String rni;
    private final String username;
    private final String password;
    private final String vendor;
    private final String avail_api;
    private final String prepro_api;
    private final String pro_api;
    private final String leak_api;
    private final String tamper_api;
    private final String encoder_api;
    private final String additional_login;

    public SiteInfoBadger(@JsonProperty("site_name") String site_name,
                          @JsonProperty("rni") String rni,
                          @JsonProperty("username") String username,
                          @JsonProperty("password") String password,
                          @JsonProperty("vendor")  String vendor,
                          @JsonProperty("avail_api") String avail_api,
                          @JsonProperty("prepro_api") String prepro_api,
                          @JsonProperty("pro_api") String pro_api,
                          @JsonProperty("leak_api")  String leak_api,
                          @JsonProperty("tamper_api") String tamper_api,
                          @JsonProperty("encoder_api") String encoder_api,
                          @JsonProperty("additional_login")  String additional_login){

        this.site_name = site_name;
        this.rni = rni;
        this.username = username;
        this.password = password;
        this.vendor = vendor;
        this.avail_api = avail_api;
        this.prepro_api = prepro_api;
        this.pro_api = pro_api;
        this.leak_api = leak_api;
        this.tamper_api = tamper_api;
        this.encoder_api = encoder_api;
        this.additional_login = additional_login;
    }

    public String getSite_name() {
        return site_name;
    }

    public String getRni() {
        return rni;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getVendor() {
        return vendor;
    }

    public String getAvail_api() {
        return avail_api;
    }

    public String getPrepro_api() {
        return prepro_api;
    }

    public String getPro_api() {
        return pro_api;
    }

    public String getLeak_api() {
        return leak_api;
    }

    public String getTamper_api() {
        return tamper_api;
    }

    public String getEncoder_api() {
        return encoder_api;
    }

    public String getAdditional_login() {
        return additional_login;
    }

    @Override
    public String toString() {
        return "SiteInfoBadger{" +
                "site_name='" + site_name + '\'' +
                ", rni='" + rni + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", vendor='" + vendor + '\'' +
                ", avail_api='" + avail_api + '\'' +
                ", prepro_api='" + prepro_api + '\'' +
                ", pro_api='" + pro_api + '\'' +
                ", leak_api='" + leak_api + '\'' +
                ", tamper_api='" + tamper_api + '\'' +
                ", encoder_api='" + encoder_api + '\'' +
                ", additional_login='" + additional_login + '\'' +
                '}';
    }
}
