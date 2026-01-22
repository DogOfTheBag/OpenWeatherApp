package com.example.openweatherapp.red;

import java.util.List;

/*La tercera parte de la clase de itunes, el response es una clase POJO que basicamente se asemeja a la info
* que nos va a devolver la API en el JSON
* es importante que los nombres de esta clase coincidan con los nombres de los parámetros del JSON,
* ya que si no no lo podrá mappear bien Gson
* la forma en la que esta puesto puede ser confuso pero si te vas a la pagina de la api, se puede ver que cada parametro
* está ordenado como se ve aqui (la temperatura se saca de una clase main, si quisieramos sacar otra info
* tendriamos que ver como se mappea y colocarlo igual aqui.*/
public class WeatherResponse {
    public String name;
    public Main main;
    public List<Weather> weather;

    public static class Main{
        public double temp;
    }

    public static class Weather {
        public int id;
        public String description;
    }
}
