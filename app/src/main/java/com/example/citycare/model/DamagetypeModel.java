package com.example.citycare.model;

import android.graphics.drawable.Drawable;

public class DamagetypeModel {
    String damagetype;
    int icon;

    public DamagetypeModel(String damagetype, int icon){
        this.icon = icon;
        this.damagetype = damagetype;
    }

    public String getDamagetype() {
        return damagetype;
    }

    public int getIcon() {
        return icon;
    }
}
