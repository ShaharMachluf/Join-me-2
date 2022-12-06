package com.example.joinme;

public class Contact {
    private String category;
    private String location;
    private String date;

    public Contact(String category, String location, String date) {
        this.category = category;
        this.location = location;
        this.date = date;
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


}