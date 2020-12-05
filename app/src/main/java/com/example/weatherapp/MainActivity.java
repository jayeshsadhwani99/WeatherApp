package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView resultTextView;

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlconnection = null;
            try {
                url = new URL(urls[0]);
                urlconnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlconnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data != -1) {
                    char current = (char) data;
                    result += current;
                }
                return result;
            } catch(Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Could Not Find the weather :(", Toast.LENGTH_SHORT).show();
                Log.i("Buton", "Click");
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("Weather Content: ", weatherInfo);
                JSONArray arr = new JSONArray(weatherInfo);

                String message = "";

                for(int i = 0; i < arr.length(); i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);

                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");

                    if(!main.equals("") && description.equals("")) {
                        message += main + ":" + description + "\r\n";
                    }
                }
                Log.i("Buton", "Click");

                if(!message.equals("")) {
                    resultTextView.setText(message);
                } else {
                    Toast.makeText(getApplicationContext(),"Could Not Find the weather :(", Toast.LENGTH_SHORT).show();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getWeather(View view) {

        Log.i("Buton", "Click");
        try {
            DownloadTask task = new DownloadTask();
            String encodedcityName = URLEncoder.encode(editText.getText().toString(), "UTF-8");
            task.execute("https://samples.openweathermap.org/data/2.5/weather?q=" + encodedcityName + "&appid=439d4b804bc8187953eb36d2a8c26a02");

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } catch(Exception e) {
            e.printStackTrace();
            Log.i("Buton", "Click");
            Toast.makeText(getApplicationContext(),"Could Not Find the weather :(", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        resultTextView = findViewById(R.id.resultsTextView);
    }
}
