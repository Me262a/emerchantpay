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

    /**Map DTO to entity.
     * @param entity entity to map
     * @return mapped dto
     */
    @Mapping(source = "authId", target = "id")
    MerchantViewDTO toDto(Merchant entity);

    /**Map DTO to entity.
     * @param dto dto to map
     * @return mapped entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authId", ignore = true)
    @Mapping(target = "totalTransactionSum", ignore = true)
    Merchant toEntity(MerchantEditDTO dto);
}
