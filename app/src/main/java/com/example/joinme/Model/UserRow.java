package com.example.joinme.Model;

public class UserRow {
    String name;
    String mail;
    String uid;
    String phone;

    public UserRow(String name, String mail, String uid, String phone) {
        this.name = name;
        this.mail = mail;
        this.uid = uid;
        this.phone = phone;
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
}
