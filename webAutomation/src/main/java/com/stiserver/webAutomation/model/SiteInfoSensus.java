package com.stiserver.webAutomation.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SiteInfoSensus {

    private final String site_name;

    private final String rni;

    private final String username;

    private final String password;

    public SiteInfoSensus(@JsonProperty("site_name") String site_name,
                          @JsonProperty("rni") String rni,
                          @JsonProperty("username") String username,
                          @JsonProperty("password") String password){

     this.site_name = site_name;
     this.rni = rni;
     this.username = username;
     this.password = password;
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

    @Override
    public String toString() {
        return "SiteInfoSensus{" +
                "site_name='" + site_name + '\'' +
                ", rni='" + rni + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
