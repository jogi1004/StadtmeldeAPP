package com.example.citycare.model;

import android.graphics.Bitmap;

public class IconModel {
    private int id;
    private String name;
    private Bitmap icon;

    public IconModel(int id, String name, Bitmap icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "IconModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon=" + icon +
                '}';
    }
}
