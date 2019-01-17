package com.example.webq.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class BlankActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);

        textView = findViewById(R.id.text);
        String s= getIntent().getStringExtra("testkey");
        textView.setText(s);
    }
}
