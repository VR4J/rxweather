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
            throw new IllegalArgumentException("Unknown city name");
        }
        
        return null;
    }
}
