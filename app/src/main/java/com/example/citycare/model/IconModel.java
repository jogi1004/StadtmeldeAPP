package com.example.citycare.model;

import android.graphics.Bitmap;

public class IconModel {
    private Integer id;
    private Bitmap icon;

    public IconModel(Integer id, Bitmap icon) {
        this.id = id;
        this.icon = icon;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
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
                ", icon=" + icon +
                '}';
    }
}
