package com.calmscape.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.calmscape.appObjects.DayStats;
import com.calmscape.R;
import com.calmscape.helpClass.DatabaseHelper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * This is DiaryActivity class, similar to EmojiActivity, it records your diary and saves it
 * into the DayStat object that's being saved to the database in this class, takes you to Task_Checklist
 * activity.
 */

public class DiaryActivity extends AppCompatActivity {

    private EditText diary; //an EditText box in which you write your diary
    private DatabaseHelper dbHelper;  //the class that helps with database inputs

    /**
     * {@inheritDoc}
     * Every Activity has this method, it activates when the object of this class is made and
     * initializes objects of other classes and components in the way that we want.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diary);

        diary = findViewById(R.id.TextBoxDiary);  //initializing the EditBox
        dbHelper = new DatabaseHelper(this);  //initializing the database helper

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Activates when button "btn_task_continue" is pressed, firstly this method loads up a DayStat
     * object through Intent and then sets it's diary entry based on what you wrote down a as description
     * of your day.
     *
     * @param view  This parameter indicates that this component will be used from the xml file.
     *
     */

    public void proceed(View view){

        Intent intent = getIntent(); //gets the intent from EmojiActivity
        DayStats ds =(DayStats) intent.getSerializableExtra("dayStat");

        ds.setDiary_entry(diary.getText().toString());  //saves you diary entry into a DayStat object
        dbHelper.insertData(getDate(), ds.getOsjecaj(), ds.getDiary_entry()); //saving data into the database

        Intent i = new Intent(this, Task_Checklist.class);
        startActivity(i);
    }


    /**
     * Simple helping method that fetches us today's date and formats it.
     *
     */


    private String getDate(){

        LocalDate danasnjiDatum = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return danasnjiDatum.format(formatter);
    }
}