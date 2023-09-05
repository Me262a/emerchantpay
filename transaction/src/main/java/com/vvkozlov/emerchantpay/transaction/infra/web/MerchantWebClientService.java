package com.vvkozlov.emerchantpay.transaction.infra.web;

import com.vvkozlov.emerchantpay.transaction.service.contract.MerchantMsClient;
import com.vvkozlov.emerchantpay.transaction.service.util.OperationResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MerchantWebClientService implements MerchantMsClient {

    private final WebClient.Builder webClientBuilder;

    public MerchantWebClientService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public OperationResult<Boolean> getIsMerchantActive(String merchantId) {
        WebClient webClient = webClientBuilder.build();
        try {
            Boolean isActive = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/merchants/{merchantId}/status")
                            .build(merchantId))
                    .retrieve()
                    .onStatus(status -> status.equals(HttpStatus.NOT_FOUND),
                            response -> Mono.just(new RuntimeException("Merchant not found")))
                    .bodyToMono(Boolean.class)
                    .block(); //We should avoid blocking and be asynchronous in real application

            return OperationResult.success(isActive != null && isActive);
        } catch (Exception e) {
            return OperationResult.failure(e.getMessage());
        }
    }
}
