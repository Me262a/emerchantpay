package com.vvkozlov.emerchantpay.merchant.service.util;

public enum ProfilesEnum {
    DEV, STAGE, RC, PROD;

    public String getProfileName() {
        return this.name().toLowerCase();
    }
}
