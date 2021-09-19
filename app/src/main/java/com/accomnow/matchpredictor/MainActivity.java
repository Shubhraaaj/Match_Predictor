package com.accomnow.matchpredictor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private TextView mScoreBoardText,mRunsText, mBatsmanText, mRunnerText;
    private Button mBowlBtn;
    private int score, wickets, currentRuns, batmanNo, runnerNo, balls;
    private ArrayList<String> playersList = new ArrayList<>(); //Stores names of players
    private ArrayList<HashMap<Integer, Integer>> scoreProbMapList= new ArrayList<>(); //Scores probability list
    /*private HashMap<String, Integer> eachPlayerScoreMap = new HashMap<>();*/
    private static int TARGET = 40; //Target
    /*private Random r;*/
    private ArrayList<HashMap<Integer, Integer>> arrListHash = new ArrayList<>();
    private int batsmanScore, runnerScore;
    private ArrayList<ArrayList<Integer>> probsList = new ArrayList<>();
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

        batsmanScore=runnerScore=0;

        //default values
        batmanNo=score=currentRuns=balls=wickets=0;
        runnerNo=1;
        /*threshold=2;*/
        /*r = new Random();*/
        String[] scoreProbablity = getResources().getStringArray(R.array.scoring_probability);
        playersList.addAll(Arrays.asList(getResources().getStringArray(R.array.players_name)));

        /*for(String s: playersList)
            eachPlayerScoreMap.put(s, 0);*/

        for(int i=0;i<playersList.size();i++){
            HashMap<Integer, Integer> scoringPattern = new HashMap<>();
            String[] sProbs = scoreProbablity[i].split("%");
            //Storing probabilities in hashmap depending on index for each player
            for(int j=0; j<sProbs.length; j++)
                scoringPattern.put(j, Integer.parseInt(sProbs[j]));
            //Adding all the hashmaps into list for easier access
            scoreProbMapList.add(scoringPattern);
        }
        mBatsmanText.setText(playersList.get(batmanNo) + " (" + batsmanScore + ")*");
        mRunnerText.setText(playersList.get(runnerNo) + " (" + runnerScore + ")");
        generateProbableRuns();
        /*mBatsmanText.setText(playersList.get(batmanNo) + " (" + eachPlayerScoreMap.get(playersList.get(batmanNo)) + ")*");
        mRunnerText.setText(playersList.get(runnerNo) + " (" + eachPlayerScoreMap.get(playersList.get(runnerNo)) + ")");*/
    }

    private void generateProbableRuns() {
        for (int i=0; i<playersList.size();i++){
            ArrayList<Integer> xpress = new ArrayList<>(scoreProbMapList.get(i).values());
            ArrayList<Integer> probs = new ArrayList<>(scoreProbMapList.get(i).values());
            Collections.sort(probs, Collections.reverseOrder());
            HashMap<Integer, Integer> scoreMap = new HashMap<>();
            scoreMap.put(probs.get(0), xpress.indexOf(probs.get(0)));
            for (int j = 1; j < probs.size(); j++) {
                probs.set(j, probs.get(j - 1) + probs.get(j));
                if (xpress.indexOf(probs.get(j) - probs.get(j - 1)) == 0 && (scoreMap.containsValue(0)))
                    scoreMap.put(probs.get(j), 10);
                else
                    scoreMap.put(probs.get(j), xpress.indexOf(probs.get(j) - probs.get(j - 1)));
            }
            probsList.add(probs);
            arrListHash.add(scoreMap);
        }
    }

    public void generateRuns(){
        /*ArrayList<Integer> xpress = new ArrayList<>(scoreProbMapList.get(batmanNo).values());
        ArrayList<Integer> probs = new ArrayList<>(scoreProbMapList.get(batmanNo).values());

        Collections.sort(probs, Collections.reverseOrder());
        HashMap<Integer, Integer> scoreMap = new HashMap<>();
        scoreMap.put(probs.get(0), xpress.indexOf(probs.get(0)));
        for(int i=1;i<probs.size();i++) {
            probs.set(i, probs.get(i - 1) + probs.get(i));
            if(xpress.indexOf(probs.get(i)-probs.get(i-1))==0 && (scoreMap.containsValue(0)))
                scoreMap.put(probs.get(i), 10);
            else
                scoreMap.put(probs.get(i), xpress.indexOf(probs.get(i)-probs.get(i-1)));
        }*/
        balls++;
        int random = (int)Math.ceil(Math.random()*101);
        Log.d("PUT", ""+random);
        if(random<=probsList.get(batmanNo).get(0)) //First
            currentRuns=arrListHash.get(batmanNo).get(probsList.get(batmanNo).get(0));

        else if(probsList.get(batmanNo).get(0)<random&&random<=probsList.get(batmanNo).get(1)) //Second
            currentRuns=arrListHash.get(batmanNo).get(probsList.get(batmanNo).get(1));

        else if(probsList.get(batmanNo).get(1)<random&&random<=probsList.get(batmanNo).get(2)) //Third
            currentRuns=arrListHash.get(batmanNo).get(probsList.get(batmanNo).get(2));

        else if(probsList.get(batmanNo).get(2)<random&&random<=probsList.get(batmanNo).get(3)) //Fourth
            currentRuns=arrListHash.get(batmanNo).get(probsList.get(batmanNo).get(3));

        else if(probsList.get(batmanNo).get(3)<random&&random<=probsList.get(batmanNo).get(4)) //Fifth
            currentRuns=arrListHash.get(batmanNo).get(probsList.get(batmanNo).get(4));

        else if(probsList.get(batmanNo).get(4)<random&&random<=probsList.get(batmanNo).get(5)) //Sixth
            currentRuns=arrListHash.get(batmanNo).get(probsList.get(batmanNo).get(5));

        else if(probsList.get(batmanNo).get(5)<random&&random<=probsList.get(batmanNo).get(6)) //Seventh
            currentRuns=arrListHash.get(batmanNo).get(probsList.get(batmanNo).get(6));

        else //Eight
            currentRuns=arrListHash.get(batmanNo).get(probsList.get(batmanNo).get(7));

        if(currentRuns<=6)
        {
            batsmanScore+=currentRuns;
            /*eachPlayerScoreMap.put(playersList.get(batmanNo), eachPlayerScoreMap.get(playersList.get(batmanNo))+currentRuns);*/
            score+=currentRuns;
            mRunsText.setText(String.valueOf(currentRuns));
            mBatsmanText.setText(playersList.get(batmanNo) + " (" + batsmanScore + ")*");
            /*mBatsmanText.setText(playersList.get(batmanNo) + " (" + eachPlayerScoreMap.get(playersList.get(batmanNo)) + ")*");*/
            if(score>=TARGET)
                result(true);
            if(currentRuns%2!=0&&runnerNo!=-1){
                int temp = batmanNo;
                batmanNo = runnerNo;
                runnerNo = temp;
                temp = batsmanScore;
                batsmanScore = runnerScore;
                runnerScore = temp;
                mBatsmanText.setText(playersList.get(batmanNo) + " (" + batsmanScore + ")*");
                mRunnerText.setText(playersList.get(runnerNo) + " (" + runnerScore + ")");
                /*mBatsmanText.setText(playersList.get(batmanNo) + " (" + eachPlayerScoreMap.get(playersList.get(batmanNo)) + ")*");
                mRunnerText.setText(playersList.get(runnerNo) + " (" + eachPlayerScoreMap.get(playersList.get(runnerNo)) + ")");*/
            }
        }
        else{
            //OUT
            mRunsText.setText("OUT");
            if(wickets++==4)
                result(false);
            else
            {
                batmanNo=(Math.max(batmanNo, runnerNo))+1;
                batsmanScore=0;
                if(batmanNo==4){
                    //Only one batsman left
                    batmanNo=runnerNo;
                    batsmanScore = runnerScore;
                    runnerNo=-1;
                    runnerScore=0;
                    mRunnerText.setVisibility(View.GONE);
                }
                mBatsmanText.setText(playersList.get(batmanNo) + " (" + batsmanScore + ")*");
                /*mBatsmanText.setText(playersList.get(batmanNo) + " (" + eachPlayerScoreMap.get(playersList.get(batmanNo)) + ")*");*/
            }
        }
        mScoreBoardText.setText(score + " - " + wickets + "\n" + balls/6 + "." + balls%6);
        if(balls%6==0&&batmanNo!=4&&runnerNo!=-1){
            int temp = batmanNo;
            batmanNo = runnerNo;
            runnerNo = temp;
            temp = batsmanScore;
            batsmanScore = runnerScore;
            runnerScore = temp;
            mBatsmanText.setText(playersList.get(batmanNo) + " (" + batsmanScore + ")*");
            mRunnerText.setText(playersList.get(runnerNo) + " (" + runnerScore + ")");
            /*mBatsmanText.setText(playersList.get(batmanNo) + " (" + eachPlayerScoreMap.get(playersList.get(batmanNo)) + ")*");
            mRunnerText.setText(playersList.get(runnerNo) + " (" + eachPlayerScoreMap.get(playersList.get(runnerNo)) + ")");*/
        }
        if(balls==24&&score<40)
            result(false);
    }

    private void result(boolean b) {
        Intent resultIntent = new Intent(this, ResultActivity.class);
        resultIntent.putExtra("status", b);
        resultIntent.putExtra("runs", score);
        resultIntent.putExtra("wickets", wickets);
        resultIntent.putExtra("balls", balls);
        startActivity(resultIntent);
    }
}