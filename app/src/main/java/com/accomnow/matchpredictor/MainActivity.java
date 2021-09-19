package com.accomnow.matchpredictor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView mScoreBoardText,mRunsText, mBatsmanText, mRunnerText;
    private Button mBowlBtn;
    private int score, wickets, currentRuns, batmanNo, runnerNo, balls, threshold;
    private ArrayList<String> playersList = new ArrayList<>();
    private ArrayList<HashMap<Integer, Integer>> scoreProbMapList= new ArrayList<>();
    private HashMap<String, Integer> eachPlayerScoreMap = new HashMap<>();
    private static int TARGET = 40;
    private Random r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View Binding
        mScoreBoardText=findViewById(R.id.main_scrbrd_txt);
        mRunsText=findViewById(R.id.main_runs_txt);
        mBatsmanText=findViewById(R.id.main_btsman_txt);
        mRunnerText=findViewById(R.id.main_rnr_txt);
        mBowlBtn=findViewById(R.id.main_bwl_btn);
        mBowlBtn.setOnClickListener(view -> generateRuns());
        bindPlayersNRuns();
    }

    private void bindPlayersNRuns() {
        //default values
        batmanNo=score=currentRuns=balls=wickets=0;
        runnerNo=1;
        threshold=2;
        r = new Random();
        String[] scoreProbablity = getResources().getStringArray(R.array.scoring_probability);
        playersList.addAll(Arrays.asList(getResources().getStringArray(R.array.players_name)));

        for(String s: playersList)
            eachPlayerScoreMap.put(s, 0);

        for(int i=0;i<playersList.size();i++){
            HashMap<Integer, Integer> scoringPattern = new HashMap<>();
            String[] sProbs = scoreProbablity[i].split("%");
            for(int j=0; j<sProbs.length; j++)
                scoringPattern.put(j, Integer.parseInt(sProbs[j]));
            scoreProbMapList.add(scoringPattern);
        }
        mBatsmanText.setText(playersList.get(batmanNo) + " (" + eachPlayerScoreMap.get(playersList.get(batmanNo)) + ")*");
        mRunnerText.setText(playersList.get(runnerNo) + " (" + eachPlayerScoreMap.get(playersList.get(runnerNo)) + ")");
    }

    public void generateRuns(){
        balls++;
        ArrayList<Integer> xpress = new ArrayList<>(scoreProbMapList.get(batmanNo).values());
        ArrayList<Integer> probs = new ArrayList<>(scoreProbMapList.get(batmanNo).values());
        Collections.sort(probs, Collections.reverseOrder());
        HashMap<Integer, Integer> scoreMap = new HashMap<>();
        scoreMap.put(probs.get(0), xpress.indexOf(probs.get(0)));

        for(int i=1;i<probs.size();i++) {
            probs.set(i, probs.get(i - 1) + probs.get(i));
            if(xpress.indexOf(probs.get(i)-probs.get(i-1))==0&&
                    (scoreMap.containsValue(0))){
                scoreMap.put(probs.get(i), 10);
            }
            else
                scoreMap.put(probs.get(i), xpress.indexOf(probs.get(i)-probs.get(i-1)));
        }

        int x;
        int random = r.nextInt(101);
        if(random<=probs.get(0)) //First
            currentRuns=scoreMap.get(probs.get(0));
        else if(probs.get(0)<random&&random<=probs.get(1)) //Second
            currentRuns=scoreMap.get(probs.get(1));
        else if(probs.get(1)<random&&random<=probs.get(2)) //Third
            currentRuns=scoreMap.get(probs.get(2));
        else if(probs.get(2)<random&&random<=probs.get(3)) //Fourth
            currentRuns=scoreMap.get(probs.get(3));
        else if(probs.get(3)<random&&random<=probs.get(4)) //Fifth
            currentRuns=scoreMap.get(probs.get(4));
        else if(probs.get(4)<random&&random<=probs.get(5)) //Sixth
            currentRuns=scoreMap.get(probs.get(5));
        else if(probs.get(5)<random&&random<=probs.get(6)) //Seventh
            currentRuns=scoreMap.get(probs.get(6));
        else //Eight
            currentRuns=scoreMap.get(probs.get(7));
        if(currentRuns<=6)
        {
            eachPlayerScoreMap.put(playersList.get(batmanNo), eachPlayerScoreMap.get(playersList.get(batmanNo))+currentRuns);
            score+=currentRuns;
            mRunsText.setText(String.valueOf(currentRuns));
            mBatsmanText.setText(playersList.get(batmanNo) + " (" + eachPlayerScoreMap.get(playersList.get(batmanNo)) + ")*");
            if(score>=TARGET)
                generateDialog(true);

            if(currentRuns%2!=0&&runnerNo!=-1){
                int temp = batmanNo;
                batmanNo = runnerNo;
                runnerNo = temp;
                mBatsmanText.setText(playersList.get(batmanNo) + " (" + eachPlayerScoreMap.get(playersList.get(batmanNo)) + ")*");
                mRunnerText.setText(playersList.get(runnerNo) + " (" + eachPlayerScoreMap.get(playersList.get(runnerNo)) + ")");
            }
        }
        else{
            //OUT
            mRunsText.setText("OUT");
            wickets++;
            if(wickets==4)
                generateDialog(false);
            else
            {
                batmanNo=(Math.max(batmanNo, runnerNo))+1;
                if(batmanNo==4){
                    //Only one batsman left
                    batmanNo=runnerNo;
                    runnerNo=-1;
                    mRunnerText.setVisibility(View.GONE);
                }
                mBatsmanText.setText(playersList.get(batmanNo) + " (" + eachPlayerScoreMap.get(playersList.get(batmanNo)) + ")*");
            }
            Log.d("WICKS: ", wickets + ", " + batmanNo);
        }
        mScoreBoardText.setText(score + " - " + wickets + "\n" + balls/6 + "." + balls%6);
        if(balls%6==0&&batmanNo!=4&&runnerNo!=-1){
            int temp = batmanNo;
            batmanNo = runnerNo;
            runnerNo = temp;
            mBatsmanText.setText(playersList.get(batmanNo) + " (" + eachPlayerScoreMap.get(playersList.get(batmanNo)) + ")*");
            mRunnerText.setText(playersList.get(runnerNo) + " (" + eachPlayerScoreMap.get(playersList.get(runnerNo)) + ")");
        }
        if(balls==24)
            generateDialog(false);
    }

    private void generateDialog(boolean b) {
        Intent resultIntent = new Intent(this, ResultActivity.class);
        resultIntent.putExtra("status", b);
        resultIntent.putExtra("runs", score);
        resultIntent.putExtra("wickets", wickets);
        resultIntent.putExtra("balls", balls);
        startActivity(resultIntent);
    }
}