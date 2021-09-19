package com.accomnow.matchpredictor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
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
    private int score, wickets, currentRuns, batmanNo, runnerNo, balls, out;
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
        batmanNo=score=currentRuns=wickets=balls=0;
        runnerNo=1;
        out=2;
        r = new Random();
        String[] scoreProbablity = getResources().getStringArray(R.array.scoring_probability);
        playersList.addAll(Arrays.asList(getResources().getStringArray(R.array.players_name)));
        for(String s: playersList)
            eachPlayerScoreMap.put(s, 0);
        for(int i=0;i<playersList.size();i++){
            HashMap<Integer, Integer> scoringPattern = new HashMap<>();
            String[] sProbs = scoreProbablity[i].split("%");
            for(int j=0; j<sProbs.length; j++) {
                scoringPattern.put(Integer.parseInt(sProbs[j]), j);
                if(scoringPattern.size()==8)
                {
                    scoreProbMapList.add(scoringPattern);
                }
            }
        }
        mBatsmanText.setText(playersList.get(batmanNo) + " (" + 0 + ")");
        mRunnerText.setText(playersList.get(runnerNo) + " (" + 0 + ")");
    }

    public void generateRuns(){
        balls++;
        int random = r.nextInt(101);

        ArrayList<Integer> xpress = new ArrayList<>(scoreProbMapList.get(batmanNo).keySet());
        //All keys are put in probs
        ArrayList<Integer> probs = new ArrayList<>(scoreProbMapList.get(batmanNo).keySet());

        Collections.sort(probs, Collections.reverseOrder());

        for(int i=1;i<probs.size();i++)
            probs.set(i, (probs.get(i-1)+probs.get(i)));

        if(random<=probs.get(0))
            currentRuns = scoreProbMapList.get(batmanNo).get(xpress.get(0));
        else if(probs.get(0)<random&&random<=probs.get(1))
            currentRuns = scoreProbMapList.get(batmanNo).get(xpress.get(1));
        else if(probs.get(1)<random&&random<=probs.get(2))
            currentRuns = scoreProbMapList.get(batmanNo).get(xpress.get(2));
        else if(probs.get(2)<random&&random<=probs.get(3))
            currentRuns = scoreProbMapList.get(batmanNo).get(xpress.get(3));
        else if(probs.get(3)<random&&random<=probs.get(4))
            currentRuns = scoreProbMapList.get(batmanNo).get(xpress.get(4));
        else if(probs.get(4)<random&&random<=probs.get(5))
            currentRuns = scoreProbMapList.get(batmanNo).get(xpress.get(5));
        else if(probs.get(5)<random&&random<=probs.get(6))
            currentRuns = scoreProbMapList.get(batmanNo).get(xpress.get(6));
        else
            currentRuns = scoreProbMapList.get(batmanNo).get(xpress.get(7));

        if(currentRuns<=6)
        {
            eachPlayerScoreMap.put(playersList.get(batmanNo), eachPlayerScoreMap.get(playersList.get(batmanNo))+currentRuns);
            score+=currentRuns;
            mRunsText.setText(String.valueOf(currentRuns));
            mBatsmanText.setText(playersList.get(batmanNo) + " (" + eachPlayerScoreMap.get(playersList.get(batmanNo)) + ")");
            if(score>=TARGET)
                generateDialog(true);

            if(currentRuns%2!=0){
                int temp = batmanNo;
                batmanNo = runnerNo;
                runnerNo = temp;
                mBatsmanText.setText(playersList.get(batmanNo) + " (" + eachPlayerScoreMap.get(playersList.get(batmanNo)) + ")");
                mRunnerText.setText(playersList.get(runnerNo) + " (" + eachPlayerScoreMap.get(playersList.get(runnerNo)) + ")");
            }
        }
        else{
            //OUT
            mRunsText.setText("OUT");
            wickets++;
            if(wickets==4)
                generateDialog(false);

            batmanNo=out++;
            mBatsmanText.setText(playersList.get(batmanNo) + " (" + eachPlayerScoreMap.get(playersList.get(batmanNo)) + ")");
        }
        mScoreBoardText.setText(score + " - " + wickets + "\n" + balls/6 + "." + balls%6);
        if(balls%6==0){
            int temp = batmanNo;
            batmanNo = runnerNo;
            runnerNo = temp;
            mBatsmanText.setText(playersList.get(batmanNo) + " (" + eachPlayerScoreMap.get(playersList.get(batmanNo)) + ")");
            mRunnerText.setText(playersList.get(runnerNo) + " (" + eachPlayerScoreMap.get(playersList.get(runnerNo)) + ")");
        }
    }

    private void generateDialog(boolean b) {
        String text;
        if(b)
            text="BANGALORE WINS";
        else
            text="BANGALORE LOSES";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(text).setPositiveButton("Replay", (dialog, id) -> {
                    //REPLAY MATCH
                });
        builder.create();
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class AsyncTaskExample extends AsyncTask<String, String, ArrayList<HashMap<Integer, Integer>>> {
        @Override
        protected void onPreExecute(){

        }

        @Override
        protected ArrayList<HashMap<Integer, Integer>> doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<Integer, Integer>> mapList) {
            super.onPostExecute(mapList);

        }
    }

}