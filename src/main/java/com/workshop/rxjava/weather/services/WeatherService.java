package com.workshop.rxjava.weather.services;

import com.workshop.rxjava.weather.model.WeatherCondition;

public interface WeatherService {
    WeatherCondition getWeather(String city);
}
