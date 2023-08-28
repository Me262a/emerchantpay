package com.vvkozlov.emerchantpay.transaction.domain.entities;

import com.vvkozlov.emerchantpay.transaction.domain.constants.TransactionStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;


import java.util.UUID;

@Entity
@Table(name = "transactions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "transaction_type", discriminatorType = DiscriminatorType.STRING)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class AbstractTransaction {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;
    @Column(name = "belongs_to")
    private String belongsTo;
    @Enumerated(EnumType.STRING)
    private TransactionStatusEnum status;
    @Column(name = "reference_id")
    private UUID referenceId;
    @Column(name = "customer_email")
    private String customerEmail;
    @Column(name = "customer_phone")
    private String customerPhone;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "date_created", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = "date_modified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModified;
}