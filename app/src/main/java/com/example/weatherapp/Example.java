package com.example.weatherapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Example {

    @SerializedName("main")
    private Main main;

    @SerializedName("weather")
    private List<String> weather;

    public List<String> getWeather() {
        return weather;
    }

    public void setWeather(List<String> weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
