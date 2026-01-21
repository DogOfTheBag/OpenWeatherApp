package com.example.openweatherapp.red;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherService {

    @GET("data/2.5/weather")
    Call<WeatherResponse>  getByCiudad(
            @Query("q") String ciudad,
            @Query("appid") String apiKey,
            @Query("units") String units,
            @Query("lang") String lang
    );
}
