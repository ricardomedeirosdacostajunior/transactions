package com.github.ricardomedeirosdacostajunior.transactions.domain.entity;

import static javax.persistence.EnumType.ORDINAL;
import static lombok.AccessLevel.PRIVATE;

import com.github.ricardomedeirosdacostajunior.transactions.domain.enumeration.OperationTypesEnumeration;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(force = true, access = PRIVATE)
@Getter
@SuperBuilder
public final class Transaction extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "account_id")
  private final Account account;

  @Enumerated(ORDINAL)
  @Column(name = "operation_type")
  private final OperationTypesEnumeration operationType;

  @Column(name = "event_date", columnDefinition = "TIMESTAMP")
  private final LocalDateTime eventDate;

  private final BigDecimal amount;
}
