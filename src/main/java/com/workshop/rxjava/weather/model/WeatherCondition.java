package com.workshop.rxjava.weather.model;

public class WeatherCondition {
    private String text;
    private Float temperature;

    public WeatherCondition(String text, Float temperature) {
        this.text = text;
        this.temperature = temperature;
    }

    public WeatherCondition() {
    }

    public String getText() {
        return text;
    }

    public Float getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return "Temperature: " + this.temperature + " text: " + this.text;
    }
}
