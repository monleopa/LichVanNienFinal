package com.example.user.calendarlunar;

/**
 * Created by User on 1/19/2018.
 */

public class CongViecCustom {
    public String day, month,year;
    public String timecv;
    public String titlecv;
    public String cv;
    public int id;

    public CongViecCustom() {
    }

    public CongViecCustom(int id, String day, String month, String year, String timecv, String titlecv, String cv) {
        this.id = id;
        this.day = day;
        this.month = month;
        this.year = year;
        this.timecv = timecv;
        this.titlecv = titlecv;
        this.cv = cv;
    }
}
