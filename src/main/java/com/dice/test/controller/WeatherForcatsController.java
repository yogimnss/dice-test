package com.dice.test.controller;

import com.dice.test.service.WeatherForCastService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
public class WeatherForcatsController {


    private final WeatherForCastService weatherForCastService;
    private final String clientId;

    private final String clientSecret;
    private final Pattern pattern;
    private static final String INVALID_CITY_NAME_MSG = "Invalid city name format";


    @Autowired
    public WeatherForcatsController(WeatherForCastService weatherForCastService, @Value("${client.id}") String clientId,
                                    @Value("${client.secret}") String clientSecret, @Value("${city.name.validation.regex}") String cityNameRegex) {
        this.weatherForCastService = weatherForCastService;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.pattern = Pattern.compile(cityNameRegex);
    }

    @GetMapping(path = "/{cityName}/getWeatherForCastSummary", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getWeatherForCastSummary(@PathVariable String cityName, @RequestHeader(value = "Client-ID") String apiId,
                                           @RequestHeader("Client-Secret") String secret) {

        if (validateHeader(apiId, secret)) {

            if (validateParam(cityName)) {
                return weatherForCastService.getWeatherForCast(cityName);
            }
            return new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_CITY_NAME_MSG);

        } else {
            return new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(path = "/{cityName}/getWeatherForCastHourly", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getWeatherForCastSummaryHourly(@PathVariable String cityName, @RequestHeader(value = "Client-ID") String apiId,
                                                 @RequestHeader("Client-Secret") String secret) {

        if (validateHeader(apiId, secret)) {
            if (validateParam(cityName)) {
                return weatherForCastService.getWeatherForCastHourly(cityName);
            }
            return new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_CITY_NAME_MSG);
        } else {
            return new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    private boolean validateHeader(String id, String secret) {
        return StringUtils.equals(id, clientId) && StringUtils.equals(secret, clientSecret);
    }

    private boolean validateParam(String cityName) {

        if (StringUtils.isBlank(cityName) || StringUtils.equals("null", cityName)) {
            return false;
        }

        if (cityName.length() < 2 || cityName.length() > 50) {
            return false;
        }


        Matcher matcher = pattern.matcher(cityName);
        return matcher.matches();
    }
}
