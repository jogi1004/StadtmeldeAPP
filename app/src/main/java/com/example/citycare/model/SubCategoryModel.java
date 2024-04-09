package com.example.citycare.model;

public class SubCategoryModel{
    private int id;
    private String title;

    public SubCategoryModel(int id, String title) {
        this.id = id;
        this.title = title;
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

    @Override
    public String toString() {
        return "SubCategoryModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
