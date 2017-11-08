package com.workshop.rxjava;

import com.workshop.rxjava.weather.controller.WeatherStation;
import com.workshop.rxjava.weather.model.WeatherCondition;

public class main {
    public static void main(String [] args) throws InterruptedException {
        WeatherStation controller = new WeatherStation();

        System.out.println("------- Getting Sync ------");
        WeatherCondition condition1 = controller.getCombinedWeatherReport("Weert");
        System.out.println("------- Getting Sync ------\n");

        System.out.println("------- Result Sync ------");
        System.out.println("MultiWeather report for Weert:");
        System.out.println("\t" + condition1);
        System.out.println("------- Result Sync ------\n");

        System.out.println("------- Getting Async ------");
        WeatherCondition condition2 = controller.getCombinedWeatherReportAsync("Weert");
        System.out.println("------- Getting Async ------\n");

        System.out.println("------- Result Async ------");
        System.out.println("Weather report for Weert:");
        System.out.println("\t" + condition2);
        System.out.println("------- Result Async ------\n");

        System.out.println("------- Getting RxJava Sync ------");
        WeatherCondition condition3 = controller
                .getCombinedWeatherReportRx("Weert")
                .blockingGet();
        System.out.println("------- Getting RxJava Sync ------\n");

        System.out.println("------- Result RxJava Sync ------");
        System.out.println("Weather report for Weert:");
        System.out.println("\t" + condition3);
        System.out.println("------- Result RxJava Sync ------\n");

        System.out.println("------- Getting RxJava Async ------");
        WeatherCondition condition4 = controller
                .getCombinedWeatherReportRxAsync("Weert")
                .blockingGet();
        System.out.println("------- Getting RxJava Async ------\n");

        System.out.println("------- Result RxJava Async ------");
        System.out.println("Weather report for Weert:");
        System.out.println("\t" + condition4);
        System.out.println("------- Result RxJava Async ------\n");
    }
}
