package com.workshop.rxjava.weather.controller;

import com.workshop.rxjava.weather.model.WeatherCondition;
import com.workshop.rxjava.weather.services.OpenWeatherMapService;
import com.workshop.rxjava.weather.services.YahooWeatherService;

public class WeatherStationController {

    private YahooWeatherService yahooWeather;
    private OpenWeatherMapService openWeatherMap;

    private static WeatherCondition openWeatherCondition;
    private static WeatherCondition yahooWeatherCondition;

    public WeatherStationController() {
        openWeatherMap = new OpenWeatherMapService();
        yahooWeather = new YahooWeatherService();
    }

    public WeatherCondition getCombinedWeatherReportAsync(String city){
        Thread t1 = new Thread(
                () -> {
                    System.out.println("[YahooWeather] Fetching .. ");
                    yahooWeatherCondition = yahooWeather.getWeather(city);
                    System.out.println("[YahooWeather] Done");
                }
        );

        Thread t2 = new Thread(
                () -> {
                    System.out.println("[OpenWeather] Fetching .. ");
                    openWeatherCondition = openWeatherMap.getWeather(city);
                    System.out.println("[OpenWeather] Done");
                }
        );

        try {
            t1.start();
            t2.start();
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Float avg = (openWeatherCondition.getTemperature() + yahooWeatherCondition.getTemperature()) / 2;
        String text = String.format("%s / %s", openWeatherCondition.getText(), yahooWeatherCondition.getText());

        return new WeatherCondition(text, avg);
    }

    public WeatherCondition getCombinedWeatherReport(String city){
        openWeatherMap = new OpenWeatherMapService();
        WeatherCondition openWeatherCondition = openWeatherMap.getWeather(city);

        yahooWeather = new YahooWeatherService();
        WeatherCondition yahooWeatherCondition = yahooWeather.getWeather(city);

        Float avg = (openWeatherCondition.getTemperature() + yahooWeatherCondition.getTemperature()) / 2;
        String text = String.format("%s / %s", openWeatherCondition.getText(), yahooWeatherCondition.getText());

        return new WeatherCondition(text, avg);
    }

}
