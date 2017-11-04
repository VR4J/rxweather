package com.workshop.rxjava.weather.services;

import java.util.HashMap;
import java.util.Map;

public class YahooWeatherService implements WeatherService {

    private Map<String, Integer> cities;

    public YahooWeatherService() {
        this.cities = new HashMap<String, Integer>();
        this.cities.put("Weert", 734564);
    }

    public Float getWeather(String city) {
        int defaultVal = -1;
        final Integer woeid = cities.getOrDefault(city,defaultVal);

        if(woeid == defaultVal) {
            throw new IllegalArgumentException("Unknown city: " + city);
        }



        return null;
    }

    private String buildWeatherYQL(Integer woeid) {
        String yql = "https://query.yahooapis.com/v1/public/yql?q=select%20item.condition%20from%20weather.forecast%20where%20woeid%20%3D%" +
                woeid +
                "%20and%20u%3D'c'&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

        return yql;
    }
}
