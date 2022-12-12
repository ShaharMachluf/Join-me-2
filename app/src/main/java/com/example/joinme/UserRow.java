package com.example.joinme;

public class UserRow {
    String name;
    String mail;

    public UserRow(String name, String mail) {
        this.name = name;
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }
}
