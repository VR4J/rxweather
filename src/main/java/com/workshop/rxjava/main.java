package com.workshop.rxjava;

import com.workshop.rxjava.weather.model.WeatherCondition;
import com.workshop.rxjava.weather.services.OpenWeatherMapService;
import com.workshop.rxjava.weather.services.WeatherService;
import com.workshop.rxjava.weather.services.YahooWeatherService;

public class main {
    public static void main(String [] args) {
        // Get Weather Report using regular Java
        WeatherService openWeatherMap = new OpenWeatherMapService();
        WeatherCondition openWeathercondition = openWeatherMap.getWeather("Weert");

        WeatherService yahooWeather = new YahooWeatherService();
        WeatherCondition yahooWeathercondition = yahooWeather.getWeather("Weert");

        System.out.println("Weather in Weert is");
        System.out.println("Open Weather\t" + openWeathercondition);
        System.out.println("Yahoo Weather\t" + yahooWeathercondition);

        // Call 3 webservices to fetch weather data
        // * OpenWeatherMap
        // * YahooWeather
        // *


        // Get Weather Report using Reactive Java
    }
}
