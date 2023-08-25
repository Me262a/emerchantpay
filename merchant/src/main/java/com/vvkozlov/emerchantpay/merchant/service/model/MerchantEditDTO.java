package com.vvkozlov.emerchantpay.merchant.service.model;

import com.vvkozlov.emerchantpay.merchant.domain.constants.MerchantStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Use this DTO to edit merchant data (post/put)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantEditDTO {
    @Schema(name = "name", example = "ABC Store", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(name = "description", example = "ABC Store in City 17", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;
    @Schema(name = "email", example = "company@mail.io", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
    @Schema(name = "status", example = "ACTIVE", requiredMode = Schema.RequiredMode.REQUIRED)
    private MerchantStatusEnum status;
}
