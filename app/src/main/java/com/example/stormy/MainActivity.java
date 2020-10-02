package com.example.stormy;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.stormy.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private CurrentWeather currentWeather;
    private double longitude = 26.7465;
    private double lattitude = 94.2026;
    private ImageView refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //boolean enabled =


        getForecast(longitude,lattitude);

    }

    private void getForecast(double longitude,double latitude) {
        ActivityMainBinding binding = DataBindingUtil.setContentView(MainActivity.this,R.layout.activity_main);

        refreshButton = (ImageView)findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Before");
                getForecast(longitude,lattitude);
                Log.d(TAG, "onClick: After");
            }
        });

        final String key = "fdd8a74e31e7d7487bf195cc05d9f5c8";
        //26.7465° N, 94.2026° E
        String forecastURL = "https://api.darksky.net/forecast/"+key+"/"+longitude+","+latitude+
                "?units=si&exclude=minutely,hourly,daily,alerts,flags";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(forecastURL)
                .build();

        if (isNetworkAvailable()){

            Call call = client.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                        String responseBody = response.body().string();
                        if (response.isSuccessful()){
                            Log.v(TAG, "onResponse: "+responseBody);
                            currentWeather = getCurrentWeather(responseBody);
                            //String locationLabel, String icon, long time,
                            //double temperature, double humidity, double precipChance, String summary, String timezone
                            CurrentWeather displayWeather = new CurrentWeather(currentWeather.getLocationLabel(),
                                    currentWeather.getIcon(),
                                    currentWeather.getTime(),
                                    currentWeather.getTemperature(),
                                    currentWeather.getHumidity(),
                                    currentWeather.getPrecipChance(),
                                    currentWeather.getSummary(),
                                    currentWeather.getTimezone());
                            binding.setWeather(displayWeather);
                        }else{
                            showErrAlert();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Response Exception Caught: ",e);
                    }
                    catch (JSONException e){
                        Log.e(TAG, "onResponse: JSON Exception", e);
                    }
                }
            });

        }else{
            showErrAlert();
        }
    }

    private CurrentWeather getCurrentWeather(String jsonData) throws JSONException {
       // Log.v(TAG,"getCurrentWeather "+jsonData);
        JSONObject weather = new JSONObject(jsonData);
        String timeZone = weather.getString("timezone");
        Log.v(TAG, timeZone);

        CurrentWeather currentWeather = new CurrentWeather();

        JSONObject currently = weather.getJSONObject("currently");

        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setLocationLabel("Test Location");
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTimezone(weather.getString("timezone"));
        currentWeather.setTime(currently.getLong("time"));

        String formattedTime = currentWeather.getFormattedTime();
        Log.v(TAG, "getCurrentWeather: formattedTime"+formattedTime);

        return currentWeather;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() ? true : false;
    }

    private void showErrAlert() {
        AlertDialogue alertDialogue = new AlertDialogue();
        alertDialogue.show(getSupportFragmentManager(),"errror dialog");
    }
}
