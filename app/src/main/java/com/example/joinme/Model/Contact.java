package com.example.joinme.Model;

public class Contact {
    private String category;
    private String location;
    private String date;
    private String id;

    public Contact(String category, String location, String date, String id) {
        this.category = category;
        this.location = location;
        this.date = date;
        this.id = id;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
}