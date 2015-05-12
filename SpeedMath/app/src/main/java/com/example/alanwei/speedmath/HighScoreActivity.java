package com.example.alanwei.speedmath;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class HighScoreActivity extends ActionBarActivity {

    private ListView listRank;
    private ListView listName;
    private ListView listHighScore;
    private ListView listTime;

    private HighScoreDataSource hSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        setTitle("High Score List");

        hSource = new HighScoreDataSource(this);
        hSource.open();
        List<HighScore> hs = new ArrayList<>();
        hs = hSource.getAllHighScores();
        hSource.close();

        // find listView
        listRank = (ListView) findViewById(R.id.listView_rank);
        listName = (ListView) findViewById(R.id.listView_name);
        listHighScore = (ListView) findViewById(R.id.listView_highScore);
        listTime = (ListView) findViewById(R.id.listView_time);

        // rank listView
        String[] ranks = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        ArrayAdapter<String> adapterRank = new ArrayAdapter<String>(this, R.layout.listview_item, R.id.textView7, ranks);
        listRank.setAdapter(adapterRank);

        String[] names = new String[10];
        String[] highScores = new String[10];
        String[] times = new String[10];

        for (int i = 0; i < 10; i++) {
            names[i] = hs.get(i).getName();
            highScores[i] = Integer.toString(hs.get(i).getHighscore());
            times[i] = Integer.toString(hs.get(i).getTime());
        }

        ArrayAdapter<String> adapterName = new ArrayAdapter<String>(this, R.layout.listview_item, R.id.textView7, names);
        ArrayAdapter<String> adapterHighScores = new ArrayAdapter<String>(this, R.layout.listview_item, R.id.textView7, highScores);
        ArrayAdapter<String> adapterTime = new ArrayAdapter<String>(this, R.layout.listview_item, R.id.textView7, times);

        listName.setAdapter(adapterName);
        listHighScore.setAdapter(adapterHighScores);
        listTime.setAdapter(adapterTime);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_high_score, menu);
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

        return super.onOptionsItemSelected(item);
    }

    public void showPlayers(View view){
        Intent intent = new Intent(this, PlayersOnServer.class);
        startActivity(intent);
    }
}
