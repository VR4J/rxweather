package com.workshop.rxjava;

import com.workshop.rxjava.weather.model.WeatherCondition;
import com.workshop.rxjava.weather.services.OpenWeatherMapService;
import com.workshop.rxjava.weather.services.WeatherService;
import com.workshop.rxjava.weather.services.YahooWeatherService;

public class main {
    public static void main(String [] args) {
        // Get Weather Report using regular Java
        WeatherService openWeatherMap = new OpenWeatherMapService();
        WeatherCondition condition = openWeatherMap.getWeather("Weert");


        // Call 3 webservices to fetch weather data
        // * OpenWeatherMap
        // * YahooWeather
        // *


        // Get Weather Report using Reactive Java
        WeatherService yahooWeather = new YahooWeatherService();
        yahooWeather.getWeather("Weert");
    }
}
