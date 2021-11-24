package com.codewithankit.thinkchats;

public class messagemodel {
    String date,message,posttime,from;

    public messagemodel() {
    }

    public messagemodel(String date, String message, String posttime, String from) {
        this.date = date;
        this.message = message;
        this.posttime = posttime;
        this.from = from;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPosttime() {
        return posttime;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
