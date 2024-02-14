package com.dice.test.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WeatherForCastProducer {


    private final WebClient.Builder webClientBuilder;

    private final String url;

    private final String apiKey;

    private final String apiHost;

    @Autowired
    public WeatherForCastProducer(WebClient.Builder webClientBuilder, @Value("${weather.forcast.summary.url}") String url,
                                  @Value("${rapid.api.key}") String apiKey, @Value("${rapid.api.host}") String apiHost) {
        this.webClientBuilder = webClientBuilder;
        this.url = url;
        this.apiKey = apiKey;
        this.apiHost = apiHost;
    }


    public Object getWeatherForCastSummaryByCity(String cityName) {
        WebClient webClient = webClientBuilder.baseUrl(url + cityName + "/summary/").defaultHeader("X-RapidAPI-Key", apiKey).
                defaultHeader("X-RapidAPI-Host", apiHost).build();
        return webClient.get().retrieve().bodyToMono(Object.class);
    }

    public Mono<Object> getWeatherForCastSummaryHourlyByCityName(String cityName) {
        WebClient webClient = webClientBuilder.baseUrl(url + cityName + "/hourly/").defaultHeader("X-RapidAPI-Key", apiKey).
                defaultHeader("X-RapidAPI-Host", apiHost).build();
        return webClient.get().retrieve().onStatus(HttpStatusCode::isError, response ->{
                return Mono.error( new RuntimeException("Failed with status code" + response.statusCode()));}).bodyToMono(Object.class);
    }
}
