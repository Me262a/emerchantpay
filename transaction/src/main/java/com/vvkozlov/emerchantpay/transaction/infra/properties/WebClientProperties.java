package com.vvkozlov.emerchantpay.transaction.infra.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "webclient")
public class WebClientProperties {

    private MerchantMsClientProperties merchant;

    @Getter
    @Setter
    public static class MerchantMsClientProperties {
        private int connectTimeout;
        private int readTimeout;
        private String baseUrl;
    }
}