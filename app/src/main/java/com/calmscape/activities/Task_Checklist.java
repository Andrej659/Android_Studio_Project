package com.calmscape.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.calmscape.helpClass.CSVHandler;
import com.calmscape.R;

import java.util.List;

/**
 * This is Task_Checklist class, this class manages your everyday tasks, it adds, removes and lists your tasks
 * from a csv file using a helping class CSVHandler, takes you to CalendarActivity.
 */

public class Task_Checklist extends AppCompatActivity {

    private EditText textBox_task_input;  //EditBox in which you write your tasks
    private LinearLayout task_list;  //this LinearLayout is used as a placeholder for the list of tasks

    /**
     * {@inheritDoc}
     * Every Activity has this method, it activates when the object of this class is made and
     * initializes objects of other classes and components in the way that we want.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_checklist);

        textBox_task_input = findViewById(R.id.textBox_task_input);  //initializing EditBox
        task_list = findViewById(R.id.textView_task_list);  //initializing LinearLayout
        printTasks();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * This method gets the current data from the csv file, edits them how we want and prints it in the LinearLayout.
     */

    public void printTasks(){

        List<String> tasks = CSVHandler.loadFromCSV(this, "tasks.csv");  //getting tasks from csv into a List of Strings

        for (String task : tasks) {  //going through all the tasks from the list

            CheckBox checkBox = new CheckBox(this);  //initializing a checkbox with our tasks
            checkBox.setText(task);
            checkBox.setBackgroundResource(R.drawable.textview_border);
            checkBox.setTextSize(26);
            checkBox.setId(View.generateViewId());

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {  //this is an EventListener which removes the task from the list if you mark something as done
                if (isChecked) {
                    task_list.removeView(buttonView);
                    CSVHandler.deleteItemFromCSV(this,"tasks.csv", task);
                }
            });

            task_list.addView(checkBox); // adds the checkbox to the linear layout
        }

    }

    /**
     * This method adds tasks to the csv file, but only if something is written in the text input field,
     * if not it writes out a hint.
     */
    public void addTask(View view){

        String st = textBox_task_input.getText().toString();

        if (st.isEmpty()){

            textBox_task_input.setHint("Write a task for yourself");

        }else{

            CSVHandler.saveToCSV(this,"tasks.csv", st);
            task_list.removeAllViews();
            printTasks();

        }
    }

    /**
     * This method just starts an CalendarActivity.
     */
    public void proceed(View view){

        Intent i = new Intent(this, CalendarActivity.class);
        startActivity(i);

    }
}