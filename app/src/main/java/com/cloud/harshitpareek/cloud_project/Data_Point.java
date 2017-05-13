package com.cloud.harshitpareek.cloud_project;

import java.math.BigDecimal;

/**
 * Created by harshitpareek on 5/10/17.
 */

public class Data_Point
{
    private String Date; // 1st element
    private String agency; // 2nd element
    private String type; // 3rd element
    private String description; // 4th element
    private double lat; // 9th element
    private double lng; // 10th element

    public Data_Point(String date, String agency, String type, String description, double lat, double lng) {
        Date = date;
        this.agency = agency;
        this.type = type;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
