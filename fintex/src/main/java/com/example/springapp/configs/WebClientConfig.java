package com.example.springapp.configs;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Data
@Configuration
public class WebClientConfig {
    private static final String BASE_URL = "http://api.weatherapi.com/v1/current.json";

    @Bean(name = "WebClientAPICurrentWeather")
    public WebClient webClient() {
        //Задание ограничений на время
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
        return WebClient.builder()
                .baseUrl(BASE_URL)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE,
                               HttpHeaders.TRANSFER_ENCODING,"chunked")
                .build();
    }
}
