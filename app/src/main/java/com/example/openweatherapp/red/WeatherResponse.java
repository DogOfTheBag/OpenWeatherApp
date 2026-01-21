package com.example.openweatherapp.red;

import java.util.List;

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
