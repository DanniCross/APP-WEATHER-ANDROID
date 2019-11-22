package com.jose.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextInputEditText CityName;
    TextInputEditText ISO;
    ImageView img;
    TextView current;
    TextView min;
    TextView max;
    TextView City;
    TextView Desc;
    TextView Press;
    TextView Hum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CityName = (TextInputEditText) findViewById(R.id.CityName);
        ISO = (TextInputEditText) findViewById(R.id.ISO);
        img = (ImageView) findViewById(R.id.WeatherIcon);
        current = (TextView) findViewById(R.id.CurrentL);
        min = (TextView) findViewById(R.id.MinL);
        max = (TextView) findViewById(R.id.MaxL);
        City = (TextView) findViewById(R.id.CityN);
        Desc = (TextView) findViewById(R.id.descL);
        Press = (TextView) findViewById(R.id.PresL);
        Hum = (TextView) findViewById(R.id.HumL);

    }

    @SuppressLint("ClickableViewAccessibility")
    public void getData(View v) {
        String key = "cfca7735f342b05700ea79279834d8ea";
        String path = "https://api.openweathermap.org/data/2.5/weather/?q="+ CityName.getText() + "," + ISO.getText() + "&APPID=" + key + "&units=metric&lang=es";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        HttpURLConnection conn;

        try {
            url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            String json = "";

            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            json = response.toString();

            JSONObject jsonObject = new JSONObject(json);
            JSONObject main = new JSONObject(jsonObject.get("main").toString());
            JSONObject sys = new JSONObject(jsonObject.get("sys").toString());
            JSONArray weather = new JSONArray(jsonObject.get("weather").toString());
            CityName.setText("");
            ISO.setText("");
            CityName.setFocusable(false);
            ISO.setFocusable(false);
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            current.setText(main.get("temp").toString());
            min.setText(main.get("temp_min").toString());
            max.setText(main.get("temp_max").toString());
            String city = jsonObject.get("name").toString() + "," + sys.get("country");
            City.setText(city);
            Desc.setText(new JSONObject(weather.get(0).toString()).get("description").toString());
            Press.setText(main.get("pressure").toString());
            Hum.setText(main.get("humidity").toString());
            Picasso.with(this)
                    .load("https://openweathermap.org/img/w/" + new JSONObject(weather.get(0).toString()).get("icon").toString() + ".png")
                    .error(R.mipmap.ic_launcher)
                    .fit()
                    .centerInside()
                    .into(img);

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CityName.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CityName.setFocusableInTouchMode(true);
                return false;
            }
        });

        ISO.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ISO.setFocusableInTouchMode(true);
                return false;
            }
        });

    }

}
