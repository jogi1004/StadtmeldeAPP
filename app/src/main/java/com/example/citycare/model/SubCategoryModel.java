package com.example.citycare.model;

public class SubCategoryModel{
    private int maincategoryId;
    private String title;

    public SubCategoryModel(int maincategoryId, String title) {
        this.maincategoryId = maincategoryId;
        this.title = title;
    }

    public int getId() {
        return maincategoryId;
    }

    public void setId(int id) {
        this.maincategoryId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "SubCategoryModel{" +
                "id=" + maincategoryId +
                ", title='" + title + '\'' +
                '}';
    }
}
