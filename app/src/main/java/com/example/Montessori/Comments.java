package com.example.Montessori;

public class Comments {
    public String comment,data,time,username;
    public Comments(){}

    public Comments(String comment, String data, String time, String username) {
        this.comment = comment;
        this.data = data;
        this.time = time;
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
