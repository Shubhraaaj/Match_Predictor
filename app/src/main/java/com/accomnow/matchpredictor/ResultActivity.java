package com.accomnow.matchpredictor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    private TextView mResultTxt;
    private Button mReplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        int score = (int) getIntent().getIntExtra("runs", -1);
        int wickets = (int) getIntent().getIntExtra("wickets", -1);
        int balls = (int) getIntent().getIntExtra("balls", -1);
        boolean success = getIntent().getBooleanExtra("status", false);
        Log.d("PUT", "Score: " + score + " | Wickets: " + wickets + " | Balls: " + balls);
        mResultTxt = findViewById(R.id.result_status);
        mReplay = findViewById(R.id.replay_btn);
        if(success)
            mResultTxt.setText("Bengaluru won the match with \n" + (4-wickets) + " Wickets and " + (24-balls) + " Balls.");
        else
            mResultTxt.setText("Bengaluru lost the match with " + (4-wickets) + " Wickets and by " + (40-score) + " Runs.");
        mReplay.setOnClickListener(view -> {
            Intent mainIntent = new Intent(ResultActivity.this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
        });
    }
}