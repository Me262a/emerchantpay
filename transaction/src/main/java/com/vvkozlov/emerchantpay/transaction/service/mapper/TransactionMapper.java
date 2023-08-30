package com.vvkozlov.emerchantpay.transaction.service.mapper;

import com.vvkozlov.emerchantpay.transaction.domain.entities.*;
import com.vvkozlov.emerchantpay.transaction.service.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

/**Maps entity to DTO and vice versa.**/
@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "amount", expression = "java(getAmount(entity))")
    @Mapping(target = "transactionType", expression = "java(getTransactionType(entity))")
    TransactionViewDTO toDto(AbstractTransaction entity);

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

    default String getTransactionType(AbstractTransaction transaction) {
        return transaction.getClass().getSimpleName();
    }

    default BigDecimal getAmount(AbstractTransaction transaction) {
        if (transaction instanceof AbstractTransactionWithAmount amountTransaction) {
            return amountTransaction.getAmount();
        } else {
            return null;
        }
    }
}
