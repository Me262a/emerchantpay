package com.vvkozlov.emerchantpay.merchant.service.mapper;

import com.vvkozlov.emerchantpay.merchant.domain.entities.Merchant;
import com.vvkozlov.emerchantpay.merchant.service.model.MerchantEditDTO;
import com.vvkozlov.emerchantpay.merchant.service.model.MerchantViewDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**Maps entity to DTO and vice versa.**/
@Mapper(componentModel = "spring")
public interface MerchantMapper {
    /**Mapper instance used or mapping.*/
    MerchantMapper INSTANCE = Mappers.getMapper(MerchantMapper.class);

    /**Map entity to DTO.
     * @param merchant - entity to map
     * @return mapped dto*/
    MerchantViewDTO toDto(Merchant merchant);

    /**Map DTO to entity.
     * @param dto dto to map
     * @return mapped entity
     */
    Merchant toEntity(MerchantEditDTO dto);
}
