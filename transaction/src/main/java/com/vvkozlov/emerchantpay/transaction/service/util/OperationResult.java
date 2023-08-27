package com.vvkozlov.emerchantpay.transaction.service.util;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

@Getter
public class OperationResult<T> {
    private static final Logger logger = LoggerFactory.getLogger(OperationResult.class);

    private final T result;
    private final List<String> errors;

    private OperationResult(T result, List<String> errors) {
        this.result = result;
        this.errors = errors;

        // Log errors when instance is a failure
        if (!isSuccess() && errors != null) {
            logger.error("OperationResult failed with errors: {}", errors);
        }
    }

    public static <T> OperationResult<T> success(T result) {
        return new OperationResult<>(result, Collections.emptyList());
    }

    public static <T> OperationResult<T> failure(String error) {
        return new OperationResult<>(null, Collections.singletonList(error));
    }

    public static <T> OperationResult<T> failure(List<String> errors) {
        return new OperationResult<>(null, errors);
    }

    public boolean isSuccess() {
        return errors.isEmpty();
    }

    @Override
    public String toString() {
        return isSuccess()
                ? "Success: " + result
                : "Errors: " + errors;
    }
}