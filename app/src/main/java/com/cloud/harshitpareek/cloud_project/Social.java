package com.cloud.harshitpareek.cloud_project;

/**
 * Created by harshitpareek on 5/11/17.
 */

public class Social
{
    private long id;
    private String name;
    private String img_url;
    private String site_url;
    private double rating;
    private double lat;
    private double lng;
    private String address;
    private String phone;
    private double zip;
    private String city;

    public Social(long id, String name, String img_url, String site_url, double rating, double lat, double lng, String address, String phone, double zip, String city) {
        this.id = id;
        this.name = name;
        this.img_url = img_url;
        this.site_url = site_url;
        this.rating = rating;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.phone = phone;
        this.zip = zip;
        this.city = city;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getSite_url() {
        return site_url;
    }

    public void setSite_url(String site_url) {
        this.site_url = site_url;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getZip() {
        return zip;
    }

    public void setZip(double zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
