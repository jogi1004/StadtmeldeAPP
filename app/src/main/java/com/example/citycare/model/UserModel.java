package com.example.citycare.model;

import android.graphics.Bitmap;

public class UserModel {

    private int id;
    private String username;
    private String email;
    private Bitmap profilePicture;
    private Boolean notificationsEnabled;
    private int picID;

    public UserModel(int id, String username, String email, Bitmap profilePicture, Boolean notificationsEnabled) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
        this.notificationsEnabled = notificationsEnabled;
    }

    public UserModel(int id, String username, String email, Boolean notificationsEnabled, int picID) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.notificationsEnabled = notificationsEnabled;
        this.picID = picID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Boolean getNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(Boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public int getPicID() {
        return picID;
    }

    public void setPicID(int picID) {
        this.picID = picID;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", profilePicture=" + profilePicture +
                ", notificationsEnabled=" + notificationsEnabled +
                '}';
    }
}
