package com.example.stormy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CurrentWeather {

    private String locationLabel;
    private String icon;
    private long time;
    private double temperature;
    private double humidity;
    private double precipChance;
    private String summary;
    private String timezone;

    public CurrentWeather() {
    }

    public CurrentWeather(String locationLabel, String icon, long time,
                          double temperature, double humidity, double precipChance, String summary, String timezone) {
        this.locationLabel = locationLabel;
        this.icon = icon;
        this.time = time;
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipChance = precipChance;
        this.summary = summary;
        this.timezone = timezone;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLocationLabel() {
        return locationLabel;
    }

    public void setLocationLabel(String locationLabel) {
        this.locationLabel = locationLabel;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getIconID(){
        int iconID = R.drawable.clear_day;
        switch (icon){
            case "clear-day" : iconID = R.drawable.clear_day;
            break;

            case "clear-night" : iconID = R.drawable.clear_night;
            break;

            case "rain" : iconID = R.drawable.rain;
            break;

            case "snow" : iconID = R.drawable.snow;
            break;

            case "sleet" : iconID = R.drawable.sleet;
            break;

            case "wind" : iconID = R.drawable.wind;
            break;

            case "fog" : iconID = R.drawable.fog;
            break;

            case "partly-cloudy-day" : iconID = R.drawable.partly_cloudy;
            break;

            case "partly-cloudy-night" : iconID = R.drawable.cloudy_night;
            break;
        }
        return iconID;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getFormattedTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        Date date = new Date(time*1000);
        return simpleDateFormat.format(date);
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPrecipChance() {
        return precipChance;
    }

    public void setPrecipChance(double precipChance) {
        this.precipChance = precipChance;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
