package com.calmscape.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.calmscape.appObjects.DayStats;
import com.calmscape.helpClass.DatabaseHelper;
import com.calmscape.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * This is MainActivity class, it opens up everytime you open the app and takes you
 * either on EmojiActivity or CalendarActivity and also it creates a database if it
 * doesn't already exist.
 */

public class MainActivity extends AppCompatActivity {


    /**
     * {@inheritDoc}
     * Every Activity has this method, it activates when the object of this class is made.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Activates when button "newDayButton" is pressed, based on the facts if database exists and if
     * already had an entry today, decides where to take you. If needed this method will create a
     * database and will create an empty DayStats object.
     *
     * @param v  This parameter indicates that this component will be used from the xml file.
     *
     */
    public void start(View v){

        try (DatabaseHelper dbHelper = new DatabaseHelper(this)) {

            if (!(dbHelper.checkDatabaseExists(this))) {

                SQLiteDatabase db = dbHelper.getWritableDatabase(); //this creates the database if it doesn't exist already

                DayStats ds = new DayStats(null,null,null);
                Intent i = new Intent(this, EmojiActivity.class);
                i.putExtra("dayStat", ds);  //with Intent we carry this object onto the next Activity for adjustments
                startActivity(i);

            } else {

                LocalDate today = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedDate = today.format(formatter); //getting today's date for an entry check

                if (dbHelper.getDayStatsByDate(formattedDate).getOsjecaj() == null || dbHelper.getDayStatsByDate(formattedDate).getDiary_entry() == null){  //checks if there were any entries today

                    DayStats ds = new DayStats(null,null,null);
                    Intent i = new Intent(this, EmojiActivity.class);
                    i.putExtra("dayStat", ds);  //with Intent we carry this object onto the next Activity for adjustments
                    startActivity(i);

                }else {

                    Intent i = new Intent(this, CalendarActivity.class);
                    startActivity(i);

                }
            }
        }
    }
}