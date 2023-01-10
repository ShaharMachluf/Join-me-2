package com.example.joinme.Model;

public class DetailsForRecycleHistory {
    private String title;
    private String city;
    private String date;
    private String time;
    private boolean is_happened;
    private String gid;


    public DetailsForRecycleHistory(String category, String location, String date, String time, boolean is_happened, String id) {
        this.title = category;
        this.city = location;
        this.date = date;
        this.gid = id;
        this.is_happened = is_happened;
        this.time = time;
    }

    public String getCategory() {
        return title;
    }

    public void setCategory(String category) {
        this.title = category;
    }

    public String getLocation() {
        return city;
    }

    public void setLocation(String location) {
        this.city = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getId() {
        return gid;
    }

    public void setId(String id) {
        this.gid = id;
    }

    public String getTime() {
        return time;
    }

    public boolean is_happened() {
        return is_happened;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setIs_happened(boolean is_happened) {
        this.is_happened = is_happened;
    }
}
