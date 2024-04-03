package com.example.citycare.model;

import java.util.Date;

public class ReportModel {
    String title;
    String image;
    String date;

    public ReportModel(String title, String date, String image) {
        this.title = title;
        this.image = image;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }
    public String getDate(){ return date;}
}
