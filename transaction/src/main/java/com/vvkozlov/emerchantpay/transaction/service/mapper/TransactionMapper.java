package com.vvkozlov.emerchantpay.transaction.service.mapper;

import com.vvkozlov.emerchantpay.transaction.domain.entities.*;
import com.vvkozlov.emerchantpay.transaction.service.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**Maps entity to DTO and vice versa.**/
@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "amount", ignore = true)
    TransactionViewDTO toDto(AbstractTransaction entity);

    TransactionViewDTO toDto(AbstractTransactionWithAmount entity);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateModified", ignore = true)
    AuthorizeTransaction toEntity(AuthorizeTransactionCreateDTO dto);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "customerEmail", ignore = true)
    @Mapping(target = "customerPhone", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateModified", ignore = true)
    ChargeTransaction toEntity(ChargeTransactionCreateDTO dto);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "customerEmail", ignore = true)
    @Mapping(target = "customerPhone", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateModified", ignore = true)
    RefundTransaction toEntity(RefundTransactionCreateDTO dto);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "customerEmail", ignore = true)
    @Mapping(target = "customerPhone", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateModified", ignore = true)
    ReversalTransaction toEntity(ReversalTransactionCreateDTO dto);
}
