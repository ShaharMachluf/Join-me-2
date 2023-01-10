package com.example.joinme.Model;

public class UserRow {
    String name;
    String mail;
    String uid;
    String phone;
    int num_of_reports;

    public UserRow(String name, String mail, String uid, String phone, int num_of_reports) {
        this.name = name;
        this.mail = mail;
        this.uid = uid;
        this.phone = phone;
        this.num_of_reports = num_of_reports;
    }

    public UserRow(String name, String mail, String uid) {
        this.name = name;
        this.mail = mail;
        this.uid = uid;
        this.phone = "";
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getUid() {
        return uid;
    }

    public String getPhone() {
        return phone;
    }

    public int getNum_of_reports() {
        return num_of_reports;
    }
}
