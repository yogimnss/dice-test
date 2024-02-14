package com.dice.test.service;

import com.dice.test.producer.WeatherForCastProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherForCastService {


    private final WeatherForCastProducer weatherForCastProducer;


    @Autowired
    public WeatherForCastService(WeatherForCastProducer weatherForCastProducer) {
        this.weatherForCastProducer = weatherForCastProducer;
    }


    public Object getWeatherForCast(String cityName) {
        return weatherForCastProducer.getWeatherForCastSummaryByCity(cityName);
    }

    public Object getWeatherForCastHourly(String cityName) {
        return weatherForCastProducer.getWeatherForCastSummaryHourlyByCityName(cityName);
    }
}
