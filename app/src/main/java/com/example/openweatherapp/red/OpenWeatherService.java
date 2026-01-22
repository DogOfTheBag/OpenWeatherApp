package com.example.openweatherapp.red;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*La segunda parte de la clase que hicimos en itunes, basicamente en el Service guardamos las llamadas http
* que vamos a hacer. Usamos la anotacion Get del protocolo HTTP, le pasamos la ruta donde vamos a coger la info
* y tenemos después las querys a las que le pedimos los siguientes parametros:
* El nombre de la ciudad, la key de la API que nos han dado al registrarnos, las unidades que usaremos para medir
* y el idioma que queramos usar*/
public interface OpenWeatherService {

    @GET("data/2.5/weather")
    Call<WeatherResponse>  getByCiudad(
            @Query("q") String ciudad,
            @Query("appid") String apiKey,
            @Query("units") String units,
            @Query("lang") String lang
    );
}
