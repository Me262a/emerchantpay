package com.vvkozlov.emerchantpay.merchant.infra.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "webclient")
public class WebClientProperties {

    private TransactionMsClientProperties transaction;

    @Getter
    @Setter
    public static class TransactionMsClientProperties {
        private int connectTimeout;
        private int readTimeout;
        private String baseUrl;
    }
}