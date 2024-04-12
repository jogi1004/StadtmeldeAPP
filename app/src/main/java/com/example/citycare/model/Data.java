package com.example.citycare.model;

public class Data {
    private byte[] profilePicture;

    public Data(byte[] data) {
        this.profilePicture = data;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }
}
