package com.workshop.rxjava.weather.controller;

import com.workshop.rxjava.weather.model.WeatherCondition;
import com.workshop.rxjava.weather.services.OpenWeatherMapService;
import com.workshop.rxjava.weather.services.YahooWeatherService;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class WeatherStation {

    private YahooWeatherService yahooWeather;
    private OpenWeatherMapService openWeatherMap;

    private static WeatherCondition openWeatherCondition;
    private static WeatherCondition yahooWeatherCondition;

    public WeatherStation() {
        openWeatherMap = new OpenWeatherMapService();
        yahooWeather = new YahooWeatherService();
    }

    public Single<WeatherCondition> getCombinedWeatherReportRx(String city){
        Single<WeatherCondition> ywSingle = Single.fromCallable(() -> yahooWeather.getWeather(city));
        Single<WeatherCondition> owSingle = Single.fromCallable(() -> openWeatherMap.getWeather(city));

        return Single
                .zip(owSingle, ywSingle, this::combineWeatherConditions)
                .subscribeOn(Schedulers.io());
    }

    private WeatherCondition combineWeatherConditions(WeatherCondition x, WeatherCondition y) {
        float avg = (x.getTemperature() + y.getTemperature()) / 2;
        String desc = String.format("%s / %s", x.getText(), y.getText());

        return new WeatherCondition(desc, avg);
    }

    public WeatherCondition getCombinedWeatherReportAsync(String city){
        Thread t1 = new Thread(() -> openWeatherCondition = openWeatherMap.getWeather(city));
        Thread t2 = new Thread(() -> yahooWeatherCondition = yahooWeather.getWeather(city));

        try {
            t1.start();
            t2.start();
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return combineWeatherConditions(openWeatherCondition, yahooWeatherCondition);
    }

    public WeatherCondition getCombinedWeatherReport(String city) {
        openWeatherMap = new OpenWeatherMapService();
        WeatherCondition openWeatherCondition = openWeatherMap.getWeather(city);

        yahooWeather = new YahooWeatherService();
        WeatherCondition yahooWeatherCondition = yahooWeather.getWeather(city);

        return combineWeatherConditions(openWeatherCondition, yahooWeatherCondition);
    }
}
