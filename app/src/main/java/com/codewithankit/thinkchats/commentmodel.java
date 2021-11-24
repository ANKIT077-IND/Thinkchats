package com.codewithankit.thinkchats;

public class commentmodel {
    String name,time,image,comment;

    commentmodel(){

    }

    public commentmodel(String name, String time, String image, String comment) {
        this.name = name;
        this.time = time;
        this.image = image;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
