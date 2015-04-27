package com.example.alanwei.speedmath;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.*;

public class NewGameActivity extends ActionBarActivity {

    private Context context;
    private ListView listView;
    private ArrayAdapter<String> questionListAdapter;
    private QuestionListAdapter qla;
    private ArrayList<String> questionList;
    private TextView timerVal;
    private TextView penaltyVal;
    private long startTime = 0L;
    private Intent intent;
    private AlertDialog alert;

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    private Handler timerHandler = new Handler();
    private Handler textHandler = new Handler();
    private Handler finishHandler = new Handler();

    public ArrayList<Boolean> rightOrWrongs = new ArrayList<Boolean>();
    public int penaltyTime;
    public int minute;
    public int second;
    public int milliseconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        setTitle("Game");
        intent = new Intent(this, GameResultActivity.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Game Finish!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(intent);
                    }
                });
        alert = builder.create();

        // set listView
        listView = (ListView)findViewById(R.id.question_list);
        questionList = new ArrayList<String>();
        questionList = createQuestionList();
        String[] qL = questionList.toArray(new String[questionList.size()]);
        Boolean[] rw = rightOrWrongs.toArray(new Boolean[rightOrWrongs.size()]);
        qla = new QuestionListAdapter(this, qL, rw);
        listView.setAdapter(qla);

        // set Timer
        penaltyVal = (TextView) findViewById(R.id.penalty_time);
        timerVal = (TextView) findViewById(R.id.timer_val);
        startTime = SystemClock.uptimeMillis();
        timerHandler.postDelayed(updateTimerThread, 0);
        textHandler.postDelayed(updateTextThread, 0);
        finishHandler.postDelayed(updateFinishThread, 0);
    }

    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            second = (int) (updatedTime / 1000);
            minute = second / 60;
            second = second % 60;
            milliseconds = (int) (updatedTime % 1000);
            timerVal.setText("" + minute + ":"
                            + String.format("%02d", second) + ":"
                            + String.format("%03d", milliseconds));
            timerHandler.postDelayed(this, 0);
        }
    };

    private Runnable updateTextThread = new Runnable() {
        @Override
        public void run() {
            String ps = "Penalty Time: " + qla.penaltyTime + "s";
            penaltyVal.setText(ps);
            textHandler.postDelayed(this, 0);
        }
    };

    private Runnable updateFinishThread = new Runnable() {
        @Override
        public void run() {
            if (qla.finish == true){
                timeSwapBuff += timeInMilliseconds;
                timerHandler.removeCallbacks(updateTimerThread);
                String s;
                s = timerVal.getText().toString();
                penaltyTime = qla.penaltyTime;

                intent.putExtra("originalTime", s);
                intent.putExtra("penaltyTime", penaltyTime);
                intent.putExtra("minute", minute);
                intent.putExtra("second", second);
                intent.putExtra("milliseconds", milliseconds);

                alert.show();
                textHandler.removeCallbacks(updateTextThread);
                finishHandler.removeCallbacks(updateFinishThread);
            }

            finishHandler.postDelayed(this, 0);
        }
    };

    public void displayGameResultPage(View view){
        Intent intent = new Intent(this, GameResultActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.quit){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<String> createQuestionList() {
        Random r = new Random();
        ArrayList<Integer> firstNumber = new ArrayList<Integer>();
        ArrayList<Integer> secondNumber = new ArrayList<Integer>();
        String symbol_string = new String();
        ArrayList<String> symbol_strings = new ArrayList<String>();
        Boolean rightOrWrong = true;

        ArrayList<String> results = new ArrayList<String>();

        for (int i=0; i<20; i++){
            int i1 = r.nextInt(11);
            int i2 = r.nextInt(11);

            firstNumber.add(i1);
            secondNumber.add(i2);
        }

        for (int j=0; j<20; j++){
            int symbol = r.nextInt(3);
            if (symbol == 0){
                symbol_string = "+";
            }
            else if (symbol == 1){
                symbol_string = "-";
            }
            else if (symbol == 2){
                symbol_string = "*";
            }
            symbol_strings.add(symbol_string);
        }

        for (int k=0; k<20; k++){
            int correct = r.nextInt(2);
            if (correct == 0){
                rightOrWrong = true;
            }
            else if (correct == 1){
                rightOrWrong = false;
            }
            rightOrWrongs.add(rightOrWrong);
        }

        for (int l=0; l<20; l++){
            if (rightOrWrongs.get(l) == true) {
                switch (symbol_strings.get(l)){
                    case "+":
                        Integer total1 = firstNumber.get(l) + secondNumber.get(l);
                        results.add(firstNumber.get(l).toString() + " + " + secondNumber.get(l).toString() + " = " + total1.toString());
                        break;
                    case "-":
                        Integer total2 = firstNumber.get(l) - secondNumber.get(l);
                        results.add(firstNumber.get(l).toString() + " - " + secondNumber.get(l).toString() + " = " + total2.toString());
                        break;
                    case "*":
                        Integer total3 = firstNumber.get(l) * secondNumber.get(l);
                        results.add(firstNumber.get(l).toString() + " * " + secondNumber.get(l).toString() + " = " + total3.toString());
                        break;
                }
            }
            else{
                Random r1 = new Random();
                switch (symbol_strings.get(l)){
                    case "+":
                        Integer total4 = firstNumber.get(l) + secondNumber.get(l) - r1.nextInt(2) + 1;
                        results.add(firstNumber.get(l).toString() + " + " + secondNumber.get(l).toString() + " = " + total4.toString());
                        break;
                    case "-":
                        Integer total5 = firstNumber.get(l) - secondNumber.get(l) + r1.nextInt(2) + 1;
                        results.add(firstNumber.get(l).toString() + " - " + secondNumber.get(l).toString() + " = " + total5.toString());
                        break;
                    case "*":
                        Integer total6 = firstNumber.get(l) * secondNumber.get(l) + r1.nextInt(2) + 1;
                        results.add(firstNumber.get(l).toString() + " * " + secondNumber.get(l).toString() + " = " + total6.toString());
                        break;
                }
            }
        }

        return results;
    }
}
