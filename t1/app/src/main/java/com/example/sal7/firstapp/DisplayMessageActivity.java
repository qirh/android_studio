package com.example.sal7.firstapp;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.content.Intent;
import android.view.ViewGroup;
import android.os.Bundle;

public class DisplayMessageActivity extends AppCompatActivity {


    private String TAG = "Tag example!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Logging onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_message);
        layout.addView(textView);
    }

}

