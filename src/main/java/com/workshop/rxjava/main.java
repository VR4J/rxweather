package com.workshop.rxjava;

import com.workshop.rxjava.weather.controller.WeatherStationController;
import com.workshop.rxjava.weather.model.WeatherCondition;

public class main {
    public static void main(String [] args) {
        // Get Weather Report using regular Java
        WeatherStationController controller = new WeatherStationController();

        WeatherCondition condition1 = controller.getCombinedWeatherReportAsync("Weert");
        WeatherCondition condition2 = controller.getCombinedWeatherReport("Weert");

        System.out.println("Weather report for Weert:");
        System.out.println("\t" + condition1);
        System.out.println("\t" + condition2);


        // Get Weather Report using Reactive Java
        WeatherCondition condition3 = controller.getCombinedWeatherReportRx("Weert");

        System.out.println("Weather report for Weert:");
        System.out.println("\t" + condition3);
    }
}
