package com.example.citycare.model;


import java.sql.Timestamp;


public class ReportModel {
    private String mainCategory;
    private String subCategory;
    private String title;
    private String image;
    private String date;
    private double longitude;
    private double latitude;

    public ReportModel(String title, String date, String image, String mainCategory, String subCategory, double longitude, double latitude) {
        this.title = title;
        this.image = image;
        this.date = date;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }
    public String getDate(){ return date;}
    public String getMainCategory(){ return mainCategory;}
    public String getSubCategory(){ return subCategory;}
    public double getLongitude(){ return longitude;}
    public double getLatitude(){ return latitude;}

    @Override
    public String toString() {
        return "ReportModel{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", date='" + date + '\'' +
                ", mainCategory='" + mainCategory +'\'' +
                ", subCategory='" + subCategory +'\'' +
                '}';
    }
}
