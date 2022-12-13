package com.example.joinme;

public class UserRow {
    String name;
    String mail;
    String uid;

    public UserRow(String name, String mail, String uid) {
        this.name = name;
        this.mail = mail;
        this.uid = uid;
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
}
