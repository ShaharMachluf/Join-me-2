package com.example.joinme.Model;

public class Contact {
    private String title;
    private String city;
    private String date;
    private String id;
    private int max_participants;
    private int num_of_participant;

    public Contact(String category, String location, String date, String id, int max_participants, int num_of_participant) {
        this.title = category;
        this.city = location;
        this.date = date;
        this.id = id;
        this.max_participants = max_participants;
        this.num_of_participant = num_of_participant;
    }


    public String getCategory() {
        return title;
    }

    public void setCategory(String category) {
        this.title = category;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMax_participants() {
        return max_participants;
    }

    public void setMax_participants(int max_participants) {
        this.max_participants = max_participants;
    }

    public int getNum_of_participant() {
        return num_of_participant;
    }
}