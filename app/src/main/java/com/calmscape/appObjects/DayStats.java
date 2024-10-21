package com.calmscape.appObjects;

import java.io.Serializable;


/**
 * This class is DayStats, it is used as a placeholder for the info we have on the user's day, we store
 * data into these before saving them into the database or after we fetch them.
 */
public class DayStats implements Serializable {

    private String datum;  //date
    private String osjecaj;  //emotion
    public String diary_entry;  //diary

    /**
     * Simple DayStats constructor.
     */
    public DayStats(String datum, String osjecaj, String diary_entry) {

        this.datum = datum;
        this.osjecaj = osjecaj;
        this.diary_entry = diary_entry;

    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getOsjecaj() {
        return osjecaj;
    }

    public void setOsjecaj(String osjecaj) {
        this.osjecaj = osjecaj;
    }

    public String getDiary_entry() {
        return diary_entry;
    }

    public void setDiary_entry(String diary_entry) {
        this.diary_entry = diary_entry;
    }
}
