package com.saadm.myanimelist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class PlanToWatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_to_watch);

        Toast.makeText(this, "Hello There!", Toast.LENGTH_SHORT).show();
    }
}