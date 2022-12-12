package com.example.joinme;

public class Logic {

    public Logic() {
    }

    public boolean checkDate(int todayYear, int todayMonth, int todayDay, int groupDateYear, int groupDateMonth, int groupDateDay){
        if(todayYear > groupDateYear){
            return false;
        }
        if(todayYear == groupDateYear){
            if(todayMonth > groupDateMonth){
                return false;
            }
            if(todayMonth == groupDateMonth){
                return todayDay <= groupDateDay;
            }
        }
        return true;
    }
}
