package com.example.citycare.model;

public class ReportModel {
    String title;
    String image;

    public ReportModel(String title, String image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }
}
