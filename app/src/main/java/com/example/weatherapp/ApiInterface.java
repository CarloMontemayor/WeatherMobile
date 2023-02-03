package com.example.weatherapp;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("weather?appid=4f0098ba3769fd59edc8002046f89336&units=metric")
    Call<ResponseBody> getWeatherData(@Query("q") String name);

}
