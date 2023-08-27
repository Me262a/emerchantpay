package com.vvkozlov.emerchantpay.merchant.service.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Use this DTO to view user data (get)
 */
@Data
@NoArgsConstructor
@SuperBuilder
public abstract class BaseUserViewDTO {
    protected String id;
    protected String name;
    protected String description;
    protected String email;
}
