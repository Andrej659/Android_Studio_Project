package com.calmscape.activities;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;

import java.text.SimpleDateFormat;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.calmscape.BuildConfig;
import com.calmscape.R;
import com.calmscape.appObjects.DayStats;
import com.calmscape.helpClass.DatabaseHelper;
import com.calmscape.helpClass.FetchQuoteTask;

import java.util.Calendar;
import java.util.Locale;

/**
 * This is CalendarActivity class, this class makes it possible to click on any date in the calendar,
 * based on that get a DayStat object from the database and list all the data it has, if it has no
 * data it will provide a hint. Also, this class uses a FetchQuoteTask class instance to initialize a
 * FinalActivity with a quote already set up.
 */

public class CalendarActivity extends AppCompatActivity {

    private EditText diaryView;  //an instance of an EditText, displays your diary
    private Calendar calendar;  //this is an actual instance of a Calendar
    private DayStats dayStats;  //an instance of DayStats, carrying out stats
    private DatabaseHelper dbHelper;  //helps w databases
    private TextView feelingView;  //just an instance of a TextView, displays info on emotions
    private static final String API_URL = "https://api.api-ninjas.com/v1/quotes?category=inspirational";  //this is where we get our quotes from

    /**
     * {@inheritDoc}
     * Every Activity has this method, it activates when the object of this class is made and
     * initializes objects of other classes and components in the way that we want.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendar);

        feelingView = findViewById(R.id.feelingTextView);  //setting up components
        diaryView = findViewById(R.id.diaryView);
        CalendarView calendarView = findViewById(R.id.calendarView); //an instance of a CalendarView, this is used just to view a calendar and use it, unlike Calendar
        calendar = Calendar.getInstance();  //creates an instance of a calendar
        dayStats = new DayStats(null,null,null);
        dbHelper = new DatabaseHelper(this);
        diaryEntry(getTextDate());  //this selects today's date in the calendar and prints info

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {  //this listener waits until you change the date on the calendar and then does something
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);  //based on what you clicked the year, month and the day are selected
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String date = getTextDate();  //this gets us today's date from the selected day on calendar in String type
                diaryEntry(date);  //this displays info from the selected date onto the TextView and EditBox
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Activates when button "btn_tasks" is pressed, saves the current data and returns us to
     * Task_Checklist activity.
     *
     * @param view  This parameter indicates that this component will be used from the xml file.
     *
     */
    public void returnToTasks(View view){

        dayStats.setDiary_entry(diaryView.getText().toString());
        dayStats.setDatum(getTextDate());
        dbHelper.updateData(dayStats.getDatum(), dayStats.getOsjecaj(), dayStats.getDiary_entry());

        Intent i = new Intent(this, Task_Checklist.class);
        startActivity(i);

    }

    /**
     * Activates when button "btn_finish" is pressed, saves the current data and proceeds to
     * FinalActivity, also fetches a quote from a Quote API and sends it to next activity.
     *
     * @param view  This parameter indicates that this component will be used from the xml file.
     *
     */

    public void finishTheForm(View view){

        dayStats.setDiary_entry(diaryView.getText().toString());
        dayStats.setDatum(getTextDate());
        dbHelper.updateData(dayStats.getDatum(), dayStats.getOsjecaj(), dayStats.getDiary_entry());

        new FetchQuoteTask(this, API_URL, BuildConfig.API_KEY).execute();  //creating an instance of FetchQuoteTask which helps us with getting quotes, it's method execute has a build in startActivity() that starts a FinalActivity instance
    }


    /**
     * This method firstly fetches us data from the database based on the date it received in parameters,
     * after that checks if the data is null, if it's not the data is printed out if it is then the hint is
     * printed out.
     *
     * @param date  This parameter is a String representation of a date.
     *
     */
    private void diaryEntry(String date){

        dayStats = dbHelper.getDayStatsByDate(date); //getting the data from the database based on the date

        if (dayStats.getOsjecaj() == null){  //this prevents you from editing the days that have no entries

            diaryView.setEnabled(false);

        }else {

            diaryView.setEnabled(true);

        }

        if(dayStats.getOsjecaj() == null){

            feelingView.setText("");
            feelingView.setHint("Nothing is saved about how u felt that day.");

        }else {

            String feel = dayStats.getOsjecaj();
            feelingView.setText("You felt " + feel + " that day!");

        }

        if(dayStats.diary_entry == null || dayStats.diary_entry.isEmpty()){

            diaryView.setText("");
            diaryView.setHint("Nothing is written about that day. ");

        }else {

            String dairy = dayStats.getDiary_entry();
            diaryView.setText(dairy);
        }

    }

    /**
     * This is a helping method that gets us the date that is selected in the calendar.
     *
     * @return returns the date selected in the calendar in string type
     */
    private String getTextDate() {

        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

         return sdf.format(calendar.getTime());
    }
}