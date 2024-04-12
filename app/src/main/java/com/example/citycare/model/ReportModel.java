package com.example.citycare.model;


import java.sql.Timestamp;


public class ReportModel {
    private String title, image, description, subCategory, mainCategory, locationName, icon, timestamp;
    private double longitude, latitude;

    public ReportModel(String title, String timestamp, String image, String mainCategory, String subCategory, double longitude, double latitude, String description, String locationName) {
        this.title = title;
        this.image = image;
        this.timestamp = timestamp;
        this.description = description;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.longitude = longitude;
        this.latitude = latitude;
        this.locationName = locationName;
    }

    public ReportModel(String title, String icon, String timestamp, String image, double longitude, double latitude) {
        this.title = title;
        this.icon = icon;
        this.timestamp = timestamp;
        this.image = image;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public String getDescription() {return description;}
    public String getTitle() {return title;}
    public String getImage() {return image;}
    public String getTimestamp(){ return timestamp;}
    public String getMainCategory(){ return mainCategory;}
    public String getSubCategory(){ return subCategory;}
    public double getLongitude(){ return longitude;}
    public double getLatitude(){ return latitude;}
    public String getLocationName() {return locationName;}

    public void setTitle(String title) {this.title = title;}
    public void setImage(String image) {this.image = image;}
    public void setTimestamp(String timestamp) {this.timestamp = timestamp;}
    public void setDescription(String description) {this.description = description;}
    public void setSubCategory(String subCategory) {this.subCategory = subCategory;}
    public void setMainCategory(String mainCategory) {this.mainCategory = mainCategory;}
    public void setLongitude(double longitude) {this.longitude = longitude;}
    public void setLatitude(double latitude) {this.latitude = latitude;}
    public void setLocationName(String locationName) {this.locationName = locationName;}

    @Override
    public String toString() {
        return "ReportModel{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", date='" + timestamp + '\'' +
                ", mainCategory='" + mainCategory +'\'' +
                ", subCategory='" + subCategory +'\'' +
                '}';
    }
}
