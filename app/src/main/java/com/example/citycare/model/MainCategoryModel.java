package com.example.citycare.model;

import java.util.List;

public class MainCategoryModel {
    private int id;
    private String title;
    private List<SubCategoryModel> subCategorys;
//    private Bitmap icon;
//    private Integer iconId;
    private IconModel icon;

    public MainCategoryModel(int id) {
        this.id = id;
    }

    public MainCategoryModel(int id, String title, IconModel icon) {
        this.id = id;
        this.title = title;
        this.icon = icon;
    }

    public List<SubCategoryModel> getSubCategorys() {
        return subCategorys;
    }

    public void setSubCategorys(List<SubCategoryModel> subCategorys) {
        this.subCategorys = subCategorys;
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
    public IconModel getIcon() {return icon;}
    public void setIcon(IconModel icon) {this.icon = icon;}

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        String b = "MainCategoryModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subCategorys=" + subCategorys;

        if (icon != null) {
            b += ", icon=" + icon;
        }else {
            b += ", icon=null";
        }
        b += '}';

        return b;
    }
}
