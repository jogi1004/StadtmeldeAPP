package com.example.citycare.model;

import java.util.List;

public class MainCategoryModel {
    private int id;
    private String title;
    private List<SubCategoryModel> subCategorys;




    public MainCategoryModel(int id, String title) {
        this.id = id;
        this.title = title;
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

    public void setTitle(String title) {
        this.title = title;
    }
}
