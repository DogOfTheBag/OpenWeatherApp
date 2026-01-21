package com.example.openweatherapp.red;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeatherClient {
    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static OpenWeatherService service;

    public static OpenWeatherService getService(){
        if(service == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(OpenWeatherService.class);
        }
        return service;

    }
}
