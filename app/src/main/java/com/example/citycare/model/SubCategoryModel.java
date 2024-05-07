package com.example.citycare.model;

public class SubCategoryModel{
    private int mainCategoryId;
    private String title;

    public SubCategoryModel(int mainCategoryId, String title) {
        this.mainCategoryId = mainCategoryId;
        this.title = title;
    }

    public int getId() {
        return mainCategoryId;
    }

    public void setId(int id) {this.mainCategoryId = mainCategoryId;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "SubCategoryModel{" +
                "id=" + mainCategoryId +
                ", title='" + title + '\'' +
                '}';
    }
}
