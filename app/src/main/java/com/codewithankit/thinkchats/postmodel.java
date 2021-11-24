package com.codewithankit.thinkchats;

public class postmodel {
    String Status,image,profile,username,time;

    public postmodel() {

    }

    public postmodel(String status, String image, String profile, String username, String time) {
        Status = status;
        this.image = image;
        this.profile = profile;
        this.username = username;
        this.time = time;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
