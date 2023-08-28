package com.vvkozlov.emerchantpay.merchant.infra.config;

import com.vvkozlov.emerchantpay.merchant.infra.properties.WebClientProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    private final WebClientProperties webClientProperties;

    public WebClientConfig(WebClientProperties webClientProperties) {
        this.webClientProperties = webClientProperties;
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        var props = webClientProperties.getTransaction();

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, props.getConnectTimeout())
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(props.getReadTimeout(), TimeUnit.MILLISECONDS))
                );

        return WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient));
    }
}