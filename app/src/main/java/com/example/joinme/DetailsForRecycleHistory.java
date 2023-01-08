package com.example.joinme;

public class DetailsForRecycleHistory {
    private String category;
    private String location;
    private String date;
    private String time;
    private boolean is_happened;
    private String id;


    public DetailsForRecycleHistory(String category, String location, String date, String time, boolean is_happened, String id) {
        this.category = category;
        this.location = location;
        this.date = date;
        this.id = id;
        this.is_happened = is_happened;
        this.time = time;
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

    public String getTime() {
        return time;
    }

    public boolean isIs_happened() {
        return is_happened;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setIs_happened(boolean is_happened) {
        this.is_happened = is_happened;
    }
}
