package com.vvkozlov.emerchantpay.transaction.service.validator;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidationResults {
    private final List<String> errorMessages = new ArrayList<>();
    @Getter
    private boolean valid = true;

    public void addError(String errorMessage) {
        errorMessages.add(errorMessage);
        valid = false;
    }
    public List<String> getErrorMessages() {
        return Collections.unmodifiableList(errorMessages);
    }
}
