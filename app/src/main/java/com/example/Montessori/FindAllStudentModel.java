package com.example.Montessori;

public class FindAllStudentModel {
    public String  profileimage;
    public String fullname;
    public String staus;



    public String uid;
    public FindAllStudentModel(){}
    public FindAllStudentModel(String profileimage, String fullname, String staus, String uid) {
        this.profileimage = profileimage;
        this.fullname = fullname;
        this.staus = staus;
        this.uid = uid;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getStaus() {
        return staus;
    }

    public void setStaus(String staus) {
        this.staus = staus;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
