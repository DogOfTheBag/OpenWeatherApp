package com.example.openweatherapp.red;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*La clase que hicimos de Itunes en el ejercicio del Retrofit está dividida aquí en 3 clases.
* En primer lugar, tenemos el Cliente, que basicamente actua como el creador del objeto retrofit
* De esta forma cogemos la url de la api a la que le vamos a coger la info, y le metemos un conversor de JSON
* como puede ser Gson*/
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
