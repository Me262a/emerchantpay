package com.vvkozlov.emerchantpay.merchant.service.model;

import lombok.*;

import java.util.UUID;

/**
 * Use this DTO to view user data (get)
 */
@Getter
@Setter
public abstract class BaseUserViewDTO {
    protected String authId;
    protected String name;
    protected String description;
    protected String email;
}
