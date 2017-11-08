package com.workshop.rxjava.weather.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workshop.rxjava.weather.model.WeatherCondition;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OpenWeatherMapService implements WeatherService{

    private final String token = "d690375dbb9a438376642cac51407a09";

    private Map<String, Integer> cities;

    public OpenWeatherMapService(){
        this.cities = new HashMap<String, Integer>();
        cities.put("Weert", 2744910);
    }

    @Override
    public WeatherCondition getWeather(String city) {
        int defaultVal = -1;
        final Integer woeid = cities.getOrDefault(city,defaultVal);

        if(woeid == defaultVal) {
            throw new IllegalArgumentException("Unknown city name");
        }

        String url = String.format("http://api.openweathermap.org/data/2.5/weather?id=%s&appid=%s&units=metric", woeid, token);
        HttpGet request = new HttpGet(url);

        try {
            HttpResponse response = HttpClientBuilder.create().build().execute(request);
            HttpEntity entity = response.getEntity();
            String jsonString = EntityUtils.toString(entity);

            ObjectMapper mapper = new ObjectMapper();

            JsonNode json = mapper.readTree(jsonString);

            JsonNode weather = json.get("weather");
            JsonNode main = json.get("main");

            String text = weather.get(0).get("main").asText();
            Float temp = main.get("temp").floatValue();

            return new WeatherCondition(text, temp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
