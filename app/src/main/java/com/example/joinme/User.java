package com.example.joinme;

import android.text.format.DateFormat;

import java.util.ArrayList;

public class User {
    private String name;
    private String phone; //todo: check if android studio have phone
    private String mail; //todo: check if android studio have Email
    private DateFormat birth_date;
    private ArrayList<Group> my_groups; // list of all the groups that this user participated in.
    private int num_of_reports; //number of reports on this user.
    private int Success_creating_groups; //number of groups that success and this user is the head of them.

    public User(String name, String phone, String mail, DateFormat birth_date) {
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.birth_date = birth_date;
        this.my_groups = new ArrayList<>();
        this.num_of_reports = 0;
        this.Success_creating_groups = 0;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

    public ArrayList<Group> getMy_groups() {
        return my_groups;
    }

    public int getNum_of_reports() {
        return num_of_reports;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setMy_groups(ArrayList<Group> my_groups) {
        this.my_groups = my_groups;
    }

    public void setNum_of_reports(int num_of_reports) {
        this.num_of_reports = num_of_reports;
    }

    public int getSuccess_creating_groups() {
        return Success_creating_groups;
    }

    public void setSuccess_creating_groups(int success_creating_groups) {
        Success_creating_groups = success_creating_groups;
    }

    public DateFormat getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(DateFormat birth_date) {
        this.birth_date = birth_date;
    }
}
