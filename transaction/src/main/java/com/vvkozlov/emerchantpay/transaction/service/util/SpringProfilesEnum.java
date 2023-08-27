package com.vvkozlov.emerchantpay.transaction.service.util;

public enum SpringProfilesEnum {
    DEV, STAGE, RC, PROD;

    public String getProfileName() {
        return this.name().toLowerCase();
    }
}
