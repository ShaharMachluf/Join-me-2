package com.example.joinme.Model;

import java.util.ArrayList;

public class Group {
    private String title; //the category of this group.
    private int min_participants; //minimum participant that the group need.
    private int max_participants; //maximum participant that the group need.
    private int num_of_participant; //the number of participants in this group.
    private String city; //the location in this group. maybe add street and number, or change to location.
    private String time; //the time of this meeting
    private String date; // the date of this meeting
    private boolean is_happened; //the group success?
    private ArrayList<String> participants; //list of all the participants in this group.
    private String head_of_group_uid; //the user that create this group.

    public Group(){}

    public Group(String title, String city, String time, String date, String head, int min_participants, int max_participants) {
        this.title = title;
        this.num_of_participant = 0;
        this.city = city;
        this.time = time;
        this.date = date;
        this.is_happened = false;
        this.participants = new ArrayList<>();
        this.head_of_group_uid = head;
        this.min_participants = min_participants;
        this.max_participants = max_participants;
    }

    public Group(String title, String city, String time, String date, String head, int num_of_participant){
        this.title = title;
        this.num_of_participant = num_of_participant;
        this.city = city;
        this.time = time;
        this.date = date;
        this.is_happened = false;
        this.participants = new ArrayList<>();
        this.head_of_group_uid = head;
        this.min_participants = 0;
        this.max_participants = 10;
    }


    public String getTitle() {
        return title;
    }

    public int getNum_of_participant() {
        return num_of_participant;
    }

    public String getCity() {
        return city;
    }

    public String getTime() {
        return time;
    }

    public boolean isIs_happened() {
        return is_happened;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNum_of_participant(int num_of_participant) {
        this.num_of_participant = num_of_participant;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setIs_happened(boolean is_happened) {
        this.is_happened = is_happened;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
    }

    public String getHead_of_group() {
        return head_of_group_uid;
    }

    public void setHead_of_group(String head_of_group) {
        this.head_of_group_uid = head_of_group;
    }

    public int getMin_participants() {
        return min_participants;
    }

    public int getMax_participants() {
        return max_participants;
    }

    public void setMin_participants(int min_participants) {
        this.min_participants = min_participants;
    }

    public void setMax_participants(int max_participants) {
        this.max_participants = max_participants;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void addParticipant(String uid){
        this.participants.add(uid);
        this.num_of_participant += 1;
    }
}
