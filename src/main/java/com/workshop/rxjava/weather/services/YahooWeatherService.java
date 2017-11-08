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

public class YahooWeatherService implements WeatherService {

    private Map<String, Integer> cities;

    public YahooWeatherService() {
        this.cities = new HashMap<String, Integer>();
        this.cities.put("Weert", 734564);
    }

    public WeatherCondition getWeather(String city) {
        System.out.println("[ Yahoo ] Getting weather! - " + Thread.currentThread().getName());

        int defaultVal = -1;
        final Integer woeid = cities.getOrDefault(city,defaultVal);

        if(woeid == defaultVal) {
            throw new IllegalArgumentException("Unknown city: " + city);
        }

        String url = buildWeatherURL(woeid);

        HttpGet request = new HttpGet(url);
        try {
            HttpResponse response = HttpClientBuilder.create().build().execute(request);
            HttpEntity entity = response.getEntity();
            String jsonString = EntityUtils.toString(entity);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(jsonString);
            JsonNode cond = node.get("query")
                    .get("results")
                    .get("channel")
                    .get("item")
                    .get("condition");

            Float temp = Float.parseFloat(cond.get("temp").asText());
            String text = cond.get("text").asText();

            System.out.println("[ Yahoo ] Done! - " + Thread.currentThread().getName());

            return new WeatherCondition(text, temp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String buildWeatherURL(Integer woeid) {
        String yql = "https://query.yahooapis.com/v1/public/yql?q=select%20item.condition%20from%20weather.forecast%20where%20woeid%20%3D%20" +
                woeid +
                "%20and%20u%3D'c'&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

        return yql;
    }
}
