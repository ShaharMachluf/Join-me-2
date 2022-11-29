package com.example.joinme;


public class FirebaseBaseModel {
    protected DatabaseReference myRef;

    public FirebaseBaseModel(){myRef = FirebaseDatabase.get}
}
