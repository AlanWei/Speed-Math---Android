package com.example.alanwei.speedmath;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.view.*;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GameResultActivity extends ActionBarActivity {

    private String result;
    private TextView originalTime;
    private TextView penaltyTextView;
    private TextView finalResult;
    private int penaltyTime;
    private int milliseconds;
    private int minute;
    private int second;
    private String playerName;
    private int rightAnswer;
    List<HighScore> hs = new ArrayList<>();
    private Integer[] times = new Integer[10];
    private int highestTime;

    private HighScoreDataSource hSource;
    private TextView t;
    private Player player;
    private List<Equation> equations;

    private boolean[] corrects = new boolean[20];
    private boolean[] answers = new boolean[20];
    private String[] es = new String[20];
    private static JSONArray eqs = new JSONArray();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        setTitle("Result");

        hSource = new HighScoreDataSource(this);
        result = getIntent().getStringExtra("originalTime");
        penaltyTime = getIntent().getIntExtra("penaltyTime", penaltyTime);
        milliseconds = getIntent().getIntExtra("milliseconds", milliseconds);
        minute = getIntent().getIntExtra("minute", minute);
        second = getIntent().getIntExtra("second", second);
        rightAnswer = getIntent().getIntExtra("rightAnswer", rightAnswer);
        corrects = getIntent().getBooleanArrayExtra("corrects");
        answers = getIntent().getBooleanArrayExtra("answers");
        es = getIntent().getStringArrayExtra("equations");

        equations = new ArrayList<>();

        for (int i=0; i<20; i++){
            Equation e = new Equation();
            e.setCorrect(corrects[i]);
            e.setAnswer(answers[i]);
            e.setEquation(es[i]);
            e.setEquationID(i);

            equations.add(e);
        }

        for (Equation e : equations){
            try {
                JSONObject styleJSON = new JSONObject();
                styleJSON.put("equationId", Integer.toString(e.getEquationID()));
                styleJSON.put("equation", e.getEquation());
                styleJSON.put("answer", Boolean.toString(e.isAnswer()));
                styleJSON.put("correct", Boolean.toString(e.isCorrect()));
                eqs.put(styleJSON);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        int mod = (second + penaltyTime) % 60;
        long a = (second + penaltyTime) / 60;
        double i = Math.floor(a);

        second = mod;
        minute += i;

        penaltyTextView = (TextView) findViewById(R.id.penalty_time);
        finalResult = (TextView) findViewById(R.id.final_result);

        penaltyTextView.setText("Penalty Time: " + penaltyTime + "s");
        finalResult.setText("Final Result: " + minute + ":"
                + String.format("%02d", second) + ":"
                + String.format("%03d", milliseconds));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Enter your name: ");
        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                playerName = input.getText().toString();

                // write to local DB
                hSource.open();

                Integer score = rightAnswer * 10;
                Integer time = minute * 60 + second;
                hSource.createHighScore(playerName, score, time);

                hs = hSource.getAllHighScores();

                for (int i = 0; i < 10; i++) {
                    times[i] = hs.get(i).getTime();
                }

                highestTime = times[9];

                if (time < highestTime) {
                    t = (TextView) findViewById(R.id.achieve_highscore);

                    t.setVisibility(View.VISIBLE);
                }

                hSource.close();

                // Sync to server

                if (isConnected()) {
                    HttpClient client = new DefaultHttpClient();

                    String URL = "http://tele303-backend-mg.appspot.com/rest/players";

                    String URLGame = "http://tele303-backend-mg.appspot.com/rest/players/" + playerName + "/games";

                    Log.i("url", URLGame);

                    new HttpAsyncTask().execute(URL);
                    new HttpAsyncTaskGame().execute(URLGame);
                } else {
                    Log.i("", "Not connected");
                }


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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

        if (id == R.id.quit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void displayMainPage(View view) {
        Intent mainPage = new Intent(this, MainActivity.class);
        startActivity(mainPage);
    }

    public void displayNewGamePage(View view) {
        Intent newGame = new Intent(this, NewGameActivity.class);
        startActivity(newGame);
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public static String POSTPlayer(String url, Player player) {
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name", player.getName());
            jsonObject.accumulate("userName", player.getUserName());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else {
                result = "Did not work!";
            }


        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    public static String POSTGame(String url, Game game) {
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("date", game.getDate());
            jsonObject.accumulate("score", game.getScore());
            jsonObject.accumulate("timeInSeconds", game.getTimeInSeconds());
            jsonObject.put("equations", eqs);


            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else {
                result = "Did not work!";
            }


        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }



    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            player = new Player();
            player.setName(playerName);
            player.setUserName(playerName);

            return POSTPlayer(urls[0], player);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            Log.i("result", result);

        }
    }

    private class HttpAsyncTaskGame extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {

            Game game = new Game();
            long date = System.currentTimeMillis();
            game.setDate(date);
            game.setEquations(equations);
            game.setScore(rightAnswer * 10);
            game.setTimeInSeconds(minute * 60 + second);

            return POSTGame(urls[0], game);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            Log.i("result", result);

        }

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}