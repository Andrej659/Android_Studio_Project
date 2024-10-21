package com.calmscape.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.calmscape.appObjects.DayStats;
import com.calmscape.R;

/**
 * This is EmojiActivity class, this class is used for tracking of how you felt on a certain day.
 */


public class EmojiActivity extends AppCompatActivity {

    /**
     * {@inheritDoc}
     * Every Activity has this method, it activates when the object of this class is made.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_emoji);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Depending on which button is pressed on activity_emoji.xml, this method take the tag from the
     * button and saves that as info on how you felt on this day.
     *
     * @param view This parameter indicates that this component will be used from the xml file.
     *
     */

    public void emojiClicked(View view) {

        Intent intent = getIntent();
        DayStats ds =(DayStats) intent.getSerializableExtra("dayStat"); //getting a DayStat object through Intent
        String emotion = (String) view.getTag();  //getting an emotion from the button tag

        if (ds != null){
            ds.setOsjecaj(emotion);  //saving an emotion into DayStat object
        }else {
            // can't happen
        }


        Intent i = new Intent(this, DiaryActivity.class);
        i.putExtra("dayStat", ds);  //carrying over the DayStat object with an emotion set onto the new activity
        startActivity(i);

    }
}
