package com.example.citycare.model;


import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import java.sql.Timestamp;
import java.util.Objects;


public class ReportModel {
    private String title, description, subCategory, mainCategory, locationName, timestamp;

    private int iconID;
    private double longitude, latitude;
    private Bitmap image;
    private MainCategoryModel mainCategoryModel;
    private int imageId;

    public ReportModel(String title, String timestamp, Bitmap image, String mainCategory, String subCategory, double longitude, double latitude, String description, String locationName) {
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

    public ReportModel(String title, int icon, String timestamp, Bitmap image, Integer imageId, double longitude, double latitude) {
        this.title = title;
        this.iconID = icon;
        this.timestamp = timestamp;
        this.image = image;
        this.imageId = imageId;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }
    public String getDescription() {return description;}
    public String getTitle() {return title;}
    public Bitmap getImage() {return image;}
    public String getTimestamp(){ return timestamp;}
    public String getMainCategory(){ return mainCategory;}
    public String getSubCategory(){ return subCategory;}
    public double getLongitude(){ return longitude;}
    public double getLatitude(){ return latitude;}
    public String getLocationName() {return locationName;}

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public void setTitle(String title) {this.title = title;}
    public void setImage(Bitmap image) {this.image = image;}
    public void setTimestamp(String timestamp) {this.timestamp = timestamp;}
    public void setDescription(String description) {this.description = description;}
    public void setSubCategory(String subCategory) {this.subCategory = subCategory;}
    public void setMainCategory(String mainCategory) {this.mainCategory = mainCategory;}
    public void setLongitude(double longitude) {this.longitude = longitude;}
    public void setLatitude(double latitude) {this.latitude = latitude;}
    public void setLocationName(String locationName) {this.locationName = locationName;}

    public MainCategoryModel getMainCategoryModel() {
        return mainCategoryModel;
    }
    public void setMainCategoryModel(MainCategoryModel mainCategoryModel) {
        this.mainCategoryModel = mainCategoryModel;
    }

    @Override
    public String toString() {
        return "ReportModel{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", imageId='" + imageId + '\'' +
                ", date='" + timestamp + '\'' +
                ", mainCategory='" + mainCategory +'\'' +
                ", subCategory='" + subCategory +'\'' +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {

            if (this == obj) {
                return true;
            }

            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            // Downcast des Objekts
            ReportModel other = (ReportModel) obj;
            // Vergleiche die Felder auf Gleichheit
            return Objects.equals(title, other.title) &&
                    Objects.equals(timestamp, other.timestamp) &&
                    Objects.equals(description, other.description) &&
                    Objects.equals(mainCategory, other.mainCategory) &&
                    Objects.equals(subCategory, other.subCategory) &&
                    Objects.equals(locationName, other.locationName) &&
                    Objects.equals(image, other.image) &&
                    Double.compare(other.longitude, longitude) == 0 &&
                    Double.compare(other.latitude, latitude) == 0;
        }

}
