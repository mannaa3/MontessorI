package com.example.Montessori;

public class posts_Model {
    public String uid;
    public String time;
    public String profileimage;
    public String postimage;
    public String fullname;
    public String description;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String   getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String data;
    public posts_Model(){

    }

    public posts_Model(String uid, String time, String profileimage, String postimage, String fullname, String description, String data) {
        this.uid = uid;
        this.time = time;
        this.profileimage = profileimage;
        this.postimage = postimage;
        this.fullname = fullname;
        this.description = description;
        this.data = data;
    }




}
