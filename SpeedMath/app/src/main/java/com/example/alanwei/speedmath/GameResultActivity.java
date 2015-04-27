package com.example.alanwei.speedmath;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.content.Intent;
import android.view.*;

public class GameResultActivity extends ActionBarActivity {

    private String result;
    private TextView originalTime;
    private TextView penaltyTextView;
    private TextView finalResult;
    private int penaltyTime;
    private int milliseconds;
    private int minute;
    private int second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        setTitle("Result");

        result = getIntent().getStringExtra("originalTime");
        penaltyTime = getIntent().getIntExtra("penaltyTime", penaltyTime);
        milliseconds = getIntent().getIntExtra("milliseconds", milliseconds);
        minute = getIntent().getIntExtra("minute", minute);
        second = getIntent().getIntExtra("second", second);

        int mod = (second + penaltyTime) % 60;
        long a = (second + penaltyTime)/60;
        double i = Math.floor(a);

        second = mod;
        minute += i;

        //originalTime = (TextView) findViewById(R.id.original_time);
        penaltyTextView = (TextView) findViewById(R.id.penalty_time);
        finalResult = (TextView) findViewById(R.id.final_result);

        //originalTime.setText(result);
        penaltyTextView.setText("Penalty Time: " + penaltyTime + "s");
        finalResult.setText("Final Result: " + minute + ":"
                + String.format("%02d", second) + ":"
                + String.format("%03d", milliseconds));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_result, menu);
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

    public void displayMainPage(View view){
        Intent mainPage = new Intent(this, MainActivity.class);
        startActivity(mainPage);
    }

    public void displayNewGamePage(View view){
        Intent newGame = new Intent(this, NewGameActivity.class);
        startActivity(newGame);
    }
}
