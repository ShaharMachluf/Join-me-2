package com.example.joinme.Model;

import java.util.ArrayList;

public class User{
    public String getUid() {
        return uid;
    }

    private String uid;
    private String name;
    private String phone;
    private String mail;
    private String birth_date;
    private ArrayList<String> my_groups; // list of all the groups that this user is head of.

    public ArrayList<String> getGroups_I_joined() {
        return groups_I_joined;
    }

    public void setGroups_I_joined(ArrayList<String> groups_I_joined) {
        this.groups_I_joined = groups_I_joined;
    }

    private ArrayList<String> groups_I_joined; // list of all the groups that this user participated in.
    private int num_of_reports; //number of reports on this user.
    private int success_creating_groups; //number of groups that success and this user is the head of them.

    public User(){}

    public User(String uid, String name, String phone, String mail, String birth_date) {
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.birth_date = birth_date;
        this.uid = uid;
        this.my_groups = new ArrayList<>();
        this.groups_I_joined = new ArrayList<>();
        this.num_of_reports = 0;
        this.success_creating_groups = 0;
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

    public ArrayList<String> getMy_groups() {
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

    public void setMy_groups(ArrayList<String> my_groups) {
        this.my_groups = my_groups;
    }

    public void setNum_of_reports(int num_of_reports) {
        this.num_of_reports = num_of_reports;
    }

    public int getSuccess_creating_groups() {
        return success_creating_groups;
    }

    public void setSuccess_creating_groups(int success_creating_groups) {
        success_creating_groups = success_creating_groups;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public void addGroup(String gid){
        this.my_groups.add(gid);
    }

    public void addGroupIJoined(String gid){
        this.groups_I_joined.add(gid);
    }
}
