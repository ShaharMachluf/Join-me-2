package com.example.joinme.Model;


public class Logic {

    public boolean flag = true;
    public Logic() {
    }

    //if the date pass return false
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
