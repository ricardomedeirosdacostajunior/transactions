package com.github.ricardomedeirosdacostajunior.transactions.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class AccountDTO {

  @JsonProperty("document_number")
  String documentNumber;

  @JsonProperty("id")
  UUID uuid;

  @JsonProperty("available_credit_limit")
  BigDecimal availableCreditLimit;
}
