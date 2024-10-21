package com.calmscape.helpClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.calmscape.appObjects.DayStats;

/**
 * This is DatabaseHelper class, this class is used as a helper with databases, it saves and loads the
 * data when necessary.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CalmScapeDB.db";  //name of the database
    private static final int DATABASE_VERSION = 1;  //version of the database


    /**
     * Simple DatabaseHelper constructor
     *
     * @param context Context is an activity in which this method is being used
     */
    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // SQL upit za stvaranje tablice s određenim stupcima
        String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " +
                "CalmScape_Tablica" +
                " (datum TEXT PRIMARY KEY, " +
                "osjecaj TEXT, " +
                "diary_entry TEXT)";

        // Izvršavanje SQL upita za stvaranje tablice
        db.execSQL(CREATE_TABLE_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS CalmScape_Tablica");
        onCreate(db);

    }

    /**
     * This method is only used to check if you have the database already on your device.
     *
     * @param context Context is an activity in which this method is being used
     * @return true or false based on the existence of the database
     */
    public boolean checkDatabaseExists(Context context) {

        SQLiteDatabase checkDB = null;
        try {

            checkDB = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).getPath(), null, SQLiteDatabase.OPEN_READONLY);
            checkDB.close();

        } catch (Exception e) {

            System.out.println("Stvara se nova baza podataka....");

        }
        return checkDB != null;

    }

    /**
     * This method inserts data into the database.
     *
     * @param datum since datum is an primary key in the database wee need it to decide where to save and insert data
     * @param osjecaj data that needs to be added into database, we get it from EmojiActivity
     * @param diary_entry data that needs to be added into database, we get it from DiaryActivity or CalendarActivity
     */
    public void insertData(String datum, String osjecaj, String diary_entry) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("datum", datum);
        values.put("osjecaj", osjecaj);
        values.put("diary_entry", diary_entry);

        long newRowId = db.insert("CalmScape_Tablica", null, values);

        if (newRowId == -1) {
            System.out.println("Error with saving data");
        } else {
            System.out.println("Data saved with row id: " + newRowId);
        }

    }

    /**
     * This method receives a date in string type and based on that fetches us the data from the table
     * and stores it in a DayStat object.
     *
     * @param datum since datum is an primary key in the database wee need it to decide where to save and insert data
     * @return a DayStats object with the data about the selected date
     */
    public DayStats getDayStatsByDate(String datum) {

        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {"datum", "osjecaj", "diary_entry"};  // Kolone za povrat
        String selection = "datum = ?";  // WHERE klauzula
        String[] selectionArgs = { datum };  // Vrijednosti za WHERE klauzulu

        Cursor cursor = db.query(
                "CalmScape_Tablica",   // Tablica za pretraživanje
                columns,               // Kolone za povrat
                selection,             // WHERE klauzula
                selectionArgs,         // Vrijednosti za WHERE klauzulu
                null,                  // Grupiranje redaka
                null,                  // Filtriranje grupa redaka
                null                   // Redoslijed sortiranja
        );

        DayStats dayStats = new DayStats(null, null, null);
        if (cursor != null && cursor.moveToFirst()) {  //this goes through all the rows in the table

            // Uzimanje podataka iz retka
            String foundDatum = cursor.getString(cursor.getColumnIndexOrThrow("datum"));
            String osjecaj = cursor.getString(cursor.getColumnIndexOrThrow("osjecaj"));
            String diaryEntry = cursor.getString(cursor.getColumnIndexOrThrow("diary_entry"));

            // Kreiranje DayStats objekta s pronađenim podacima
            dayStats = new DayStats(foundDatum, osjecaj, diaryEntry);
            cursor.close();
        }

        return dayStats;
    }


    /**
     * This method updates the data from the database.
     *
     * @param datum since datum is an primary key in the database wee need it to decide where to save and insert data
     * @param newOsjecaj updated data that needs to be added into database
     * @param newDiaryEntry updated data that needs to be added into database
     */
    public void updateData(String datum, String newOsjecaj, String newDiaryEntry) {

        SQLiteDatabase db = this.getWritableDatabase();  //opens up a database
        ContentValues values = new ContentValues();  //this object is used for storing values
        values.put("osjecaj", newOsjecaj);
        values.put("diary_entry", newDiaryEntry);

        String selection = "datum = ?";
        String[] selectionArgs = { datum };

        int count = db.update(
                "CalmScape_Tablica",
                values,
                selection,
                selectionArgs);

        if (count == 0) {
            System.out.println("No rows updated");
        } else {
            System.out.println("Rows updated: " + count);
        }

    }
}

