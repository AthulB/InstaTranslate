package com.example.athul.instatranslate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Athul on 10-03-2018.
 */

public class NewActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        textView = (TextView) findViewById(R.id.textView);
        String text;
        text = getIntent().getExtras().getString("data");
        textView.setText(text);
    }
}
