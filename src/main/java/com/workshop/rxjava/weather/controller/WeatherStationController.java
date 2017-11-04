package com.workshop.rxjava.weather.controller;

import com.workshop.rxjava.weather.model.WeatherCondition;
import com.workshop.rxjava.weather.services.OpenWeatherMapService;
import com.workshop.rxjava.weather.services.YahooWeatherService;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class WeatherStationController {

    private YahooWeatherService yahooWeather;
    private OpenWeatherMapService openWeatherMap;

    private static WeatherCondition openWeatherCondition;
    private static WeatherCondition yahooWeatherCondition;

    public WeatherStationController() {
        openWeatherMap = new OpenWeatherMapService();
        yahooWeather = new YahooWeatherService();
    }

    public WeatherCondition getCombinedWeatherReportRx(String city){
        Single<WeatherCondition> yahooWeatherSingle = Single.fromCallable(
                () -> yahooWeather.getWeather(city)
        );

        Single<WeatherCondition> openWeatherSingle = Single.fromCallable(
                () -> openWeatherMap.getWeather(city)
        );

        Observable<WeatherCondition> combined = Single.merge(openWeatherSingle, yahooWeatherSingle).toObservable()
                .subscribeOn(Schedulers.io());

        Single<Float> avg = combined.map(WeatherCondition::getTemperature)
                .reduce(0.0f,
                        (x, y) -> x + y
                )
                .map(x -> x / 2);

        Single<String> text = combined.map(WeatherCondition::getText)
                .buffer(2)
                .map(strings -> String.join(" / ", strings)).singleOrError();

        // TODO
        Single<WeatherCondition> condition = Single.merge(avg, text);

        return condition.blockingGet();
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
