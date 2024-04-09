package com.example.citycare.model;

import java.util.List;

public class MainCategoryModel {
    private int id;
    private String title;
    private List<SubCategoryModel> subCategorys;
    private int icon;


    public MainCategoryModel(int id, String title, int icon) {
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
    public int getIcon() {
        return icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "MainCategoryModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subCategorys=" + subCategorys +
                '}';
    }
}
