package com.workshop.rxjava;

import com.workshop.rxjava.weather.controller.WeatherStation;
import com.workshop.rxjava.weather.model.WeatherCondition;

public class main {
    public static void main(String [] args) {
        // Get Weather Report using regular Java
        WeatherStation controller = new WeatherStation();

        WeatherCondition condition1 = controller.getCombinedWeatherReportAsync("Weert");
        WeatherCondition condition2 = controller.getCombinedWeatherReport("Weert");

        System.out.println("Weather report for Weert:");
        System.out.println(condition1);

        System.out.println("MultiThreadedWeather report for Weert:");
        System.out.println(condition2);

        // Get Weather Report using Reactive Java
        WeatherCondition condition3 = controller
                .getCombinedWeatherReportRx("Weert")
                .blockingGet();

        System.out.println("Weather report for Weert:");
        System.out.println(condition3);
    }
}
