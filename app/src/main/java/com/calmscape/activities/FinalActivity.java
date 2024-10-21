package com.calmscape.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.calmscape.R;

import com.calmscape.helpClass.FetchQuoteTask;

/**
 * This is FinalActivity class, this is the final activity that is started, from a Quote API a quote is
 * fetched and printed out on this activity.
 */

public class FinalActivity extends AppCompatActivity {

    TextView motivation;  //a TextView in which a quote is shown

    /**
     * {@inheritDoc}
     * Every Activity has this method, it activates when the object of this class is made and
     * initializes objects of other classes and components in the way that we want.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_final);

        motivation = findViewById(R.id.textView_motivation);

        Intent intent = getIntent();
        if (intent != null) {

            String citat = intent.getStringExtra("citat");  //through Intent we get a quote that was already loaded in last activity
            motivation.setText(citat);

        } else {

        }

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
    }

    /**
     * Activates when button "btn_endApp" is pressed, turns the app off.
     *
     * @param view  This parameter indicates that this component will be used from the xml file.
     *
     */
    public void endApp(View view){
        finishAffinity();
    }
}