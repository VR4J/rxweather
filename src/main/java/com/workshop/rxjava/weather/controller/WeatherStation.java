package com.workshop.rxjava.weather.controller;

import com.workshop.rxjava.weather.model.WeatherCondition;
import com.workshop.rxjava.weather.services.OpenWeatherMapService;
import com.workshop.rxjava.weather.services.YahooWeatherService;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WeatherStation {

    private YahooWeatherService yahooWeather;
    private OpenWeatherMapService openWeatherMap;

    private static WeatherCondition openWeatherCondition;
    private static WeatherCondition yahooWeatherCondition;

    public WeatherStation() {
        openWeatherMap = new OpenWeatherMapService();
        yahooWeather = new YahooWeatherService();
    }

    public Single<WeatherCondition> getCombinedWeatherReportRxAsync(String city){
        Single<WeatherCondition> ywSingle = Single.fromCallable(() -> yahooWeather.getWeather(city))
                .subscribeOn(Schedulers.io());

        Single<WeatherCondition> owSingle = Single.fromCallable(() -> openWeatherMap.getWeather(city))
                .subscribeOn(Schedulers.io());

        return Single.zip(owSingle, ywSingle, this::combineWeatherConditions);
    }

    public Single<WeatherCondition> getCombinedWeatherReportRx(String city){
        Single<WeatherCondition> ywSingle = Single.fromCallable(() -> yahooWeather.getWeather(city));
        Single<WeatherCondition> owSingle = Single.fromCallable(() -> openWeatherMap.getWeather(city));

        return Single.zip(owSingle, ywSingle, this::combineWeatherConditions);
    }

    public WeatherCondition getCombinedWeatherReportAsync(String city) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> openWeatherCondition = openWeatherMap.getWeather(city));
        executor.submit(() -> yahooWeatherCondition = yahooWeather.getWeather(city));

        executor.shutdown(); //do not accept more tasks
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

        return combineWeatherConditions(openWeatherCondition, yahooWeatherCondition);
    }

    public WeatherCondition getCombinedWeatherReport(String city){
        WeatherCondition openWeatherCondition = openWeatherMap.getWeather(city);
        WeatherCondition yahooWeatherCondition = yahooWeather.getWeather(city);

        return combineWeatherConditions(openWeatherCondition, yahooWeatherCondition);
    }

    private WeatherCondition combineWeatherConditions(WeatherCondition x, WeatherCondition y) {
        float avg = (x.getTemperature() + y.getTemperature()) / 2;
        String desc = String.format("%s / %s", x.getText(), y.getText());

        return new WeatherCondition(desc, avg);
    }
}
