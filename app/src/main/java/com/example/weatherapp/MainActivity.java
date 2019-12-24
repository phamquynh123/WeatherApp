package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView cityName;
    Button searchButton;
    TextView result;
    ImageView imageView;
 class Weather extends AsyncTask<String,Void,String> { //first String means URL is in String , void means nothing, third string means return "String"


     @Override
     protected String doInBackground(String... address) {
        //CHeck URL is valid or not . String means multiple address can be send as array
         try {
             URL url = new URL(address[0]);
             HttpURLConnection  connection = (HttpURLConnection) url.openConnection();
             connection.connect();

             //retrieve data from url
             InputStream is = connection.getInputStream();
             InputStreamReader isr = new InputStreamReader(is);

             // Retrieve data and return it as String
                int data = isr.read();
                String content = "";
                char ch;
                while (data != -1){
                    ch = (char) data;
                    content = content + ch ;
                    data = isr.read();
                }

                return content;
         } catch (MalformedURLException e) {

            e.printStackTrace();
//             result.setText("Thành phố không tồn tại");
         } catch (IOException e) {

             e.printStackTrace();

         }
         return null;
     }
 }


 public void search (View view){
     cityName = findViewById(R.id.cityName);
     searchButton = findViewById(R.id.searchButton);
     result = findViewById(R.id.result);
     imageView = (ImageView) findViewById(R.id.imageView);

     String cName = cityName.getText().toString(); // cName ~ Hanoi ( name city )
     String content;
     Weather weather = new Weather();
     try {
         content = weather.execute("https://openweathermap.org/data/2.5/weather?q=" +cName+ "&appid=b6907d289e10d714a6e88b30761fae22").get();
         //Check data retrieve successfully or not

         Log.i(  "content" , content);

         //JSOn
         JSONObject jsonObject = new JSONObject(content);
         String weatherData = jsonObject.getString("weather");
         String mainTemperature = jsonObject.getString("main"); // it's properties of main:{temp:...}
         Log.i("weatherData", weatherData);

         //weather data is in Array
         JSONArray array = new JSONArray(weatherData);

         String main ="";
         String description ="";
         String temperature ="";
         String humidity = "";
         String temp_min ="";
         String temp_max ="";

         for (int i = 0; i < array.length();i++){
             JSONObject weatherPart = array.getJSONObject(i);
//             Log.i("weatherPart", String.valueOf(weatherPart));
             main = weatherPart.getString("main");
             description = weatherPart.getString("description");

         }

         JSONObject mainPart = new JSONObject(mainTemperature);
         temperature = mainPart.getString("temp"); //name: tepmp ( name in api json )
         humidity = mainPart.getString("humidity"); // get humidity from api
         temp_min = mainPart.getString("temp_min"); // get temp_min from api
         temp_max = mainPart.getString("temp_max");// get temp_max from api
//         Log.i("main",main);
//         Log.i("description",description);

         // show result api to screen
         String resultText = " - Trạng thái : " + main + "\n - Mô tả : " + description
                 + "\n - Nhiệt độ hiện tại : " + temperature +" độ C" +"\n - Độ ẩm : " + humidity
                 + "\n - Nhiệt độ thấp nhất : " + temp_min + " độ C" + "\n - Nhiệt độ cao nhất : "
                 + temp_max + " độ C";
         Toast.makeText(MainActivity.this, jsonObject.getString("cod"), Toast.LENGTH_LONG).show();
//         if (jsonObject.getString("cod").equals("200")) {
             result.setText(resultText);
             if(main.equals("Mist")) {
                 imageView.setImageResource(R.drawable.cloud4);
             } if(main.equals("Clear")) {
             imageView.setImageResource(R.drawable.cloud3);
             } if(main.equals("Clouds")) {
                 imageView.setImageResource(R.drawable.cloud2);
             } else {
                 imageView.setImageResource(R.drawable.cloud1);
             }
//         } else {
//             result.setText("Thành phố bạn search không tồn tại.");
//
//         }

//         Toast.makeText(MainActivity.this, main, Toast.LENGTH_LONG).show();
     } catch (Exception e) {
         e.printStackTrace();
     }
 }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
