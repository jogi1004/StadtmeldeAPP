package com.example.citycare.model;

import android.graphics.Bitmap;

import java.util.List;

public class MainCategoryModel {
    private int id;
    private String title;
    private List<SubCategoryModel> subCategorys;
    private Bitmap icon;
    private Integer iconId;

    public MainCategoryModel(int id) {
        this.id = id;
    }

    public MainCategoryModel(int id, String title, Bitmap icon, Integer iconId) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.iconId = iconId;
    }

    public List<SubCategoryModel> getSubCategorys() {
        return subCategorys;
    }

    public void setSubCategorys(List<SubCategoryModel> subCategorys) {
        this.subCategorys = subCategorys;
    }

    public Integer getIconId() {
        return iconId;
    }

    public void setIconId(Integer iconId) {
        this.iconId = iconId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public Bitmap getIcon() {return icon;}
    public void setIcon(Bitmap icon) {this.icon = icon;}

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "MainCategoryModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subCategorys=" + subCategorys +
                ", icon=" + icon +
                '}';
    }
}
