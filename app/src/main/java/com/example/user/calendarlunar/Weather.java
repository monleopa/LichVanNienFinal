package com.example.user.calendarlunar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Weather extends AppCompatActivity {

    EditText edtCity;
    Button btnSearch, btnNextDay;
    TextView txtCountry, txtCity, txtTemp, txtNote, txtDoAm, txtMay, txtWind, txtDateTime;
    ImageView imgWeather;
    ImageButton btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        init();

        getCurrentWeatherData("HaNoi");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = edtCity.getText().toString();

                getCurrentWeatherData(city);

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Weather.this,CalendarLunar.class);

                startActivity(intent);
            }
        });

    }

    private void getCurrentWeatherData(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(Weather.this);
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=e9c3cb6b23238b9214a2191977afa884";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            // set city
                            String day = jsonObject.getString("dt");
                            String nameCity = jsonObject.getString("name");
                            txtCity.setText(nameCity);

                            // set date
                            long date2 = Long.valueOf(day);
                            Date date = new Date(date2 * 1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd HH:mm:ss");
                            String Day = simpleDateFormat.format(date);
                            txtDateTime.setText("Updated: "+Day);

                            // set weather, image, note
                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String stt = jsonObjectWeather.getString("main");
                            String icon = jsonObjectWeather.getString("icon");

                            Picasso.with(Weather.this).load("http://openweathermap.org/img/w/"+icon+".png").into(imgWeather);
                            txtNote.setText(stt);

                            // set nhiet do, do am
                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String temperature = jsonObjectMain.getString("temp");
                            String doam = jsonObjectMain.getString("humidity");
                            // Doi nhiet do
                            Double temp = Double.valueOf(temperature);
                            String Temp = String.valueOf(temp.intValue());
                            txtTemp.setText(temperature+"°C");
                            txtDoAm.setText(doam+"%");

                            // set Gio
                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String wind = jsonObjectWind.getString("speed");
                            txtWind.setText(wind+"m/s");

                            // set May
                            JSONObject jsonObject1Clound = jsonObject.getJSONObject("clouds");
                            String cloud = jsonObject1Clound.getString("all");
                            txtMay.setText(cloud+"%");

                            // set Country
                            JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                            String country = jsonObjectSys.getString("country");
                            txtCountry.setText(country);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue.add(stringRequest);
    }

    private void init(){
        edtCity = findViewById(R.id.edtCity);
        btnSearch = findViewById(R.id.btnSearch);
        btnNextDay = findViewById(R.id.btnNextDay);
        txtCountry = findViewById(R.id.txtCountry);
        txtCity = findViewById(R.id.txtCity);
        txtTemp = findViewById(R.id.txtTemp);
        txtNote = findViewById(R.id.txtNote);
        txtDoAm = findViewById(R.id.txtDoAm);
        txtMay = findViewById(R.id.txtMay);
        txtDateTime = findViewById(R.id.txtDateTime);
        txtWind = findViewById(R.id.txtWind);
        imgWeather = findViewById(R.id.imgWeather);
        btnBack = findViewById(R.id.btnBack);
    }
}
