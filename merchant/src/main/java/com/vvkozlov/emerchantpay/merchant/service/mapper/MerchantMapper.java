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
     * @param entity - entity to map
     * @return mapped dto*/
    default MerchantViewDTO toDto(Merchant entity) {
        if (entity == null) {
            return null;
        }

        MerchantViewDTO dto = new MerchantViewDTO();
        dto.setAuthId(entity.getAuthId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setEmail(entity.getEmail());
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setTotalTransactionSum(entity.getTotalTransactionSum());

        return dto;
    }

    /**Map DTO to entity.
     * @param dto dto to map
     * @return mapped entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authId", ignore = true)
    @Mapping(target = "totalTransactionSum", ignore = true)
    Merchant toEntity(MerchantEditDTO dto);
}
