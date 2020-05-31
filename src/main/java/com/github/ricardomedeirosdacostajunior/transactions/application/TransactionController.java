package com.github.ricardomedeirosdacostajunior.transactions.application;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.ricardomedeirosdacostajunior.transactions.domain.dto.TransactionDTO;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/transactions", produces = APPLICATION_JSON_VALUE)
public class TransactionController {

  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  public TransactionDTO create(@NotNull @RequestBody final TransactionDTO transactionDTO) {
    return null;
  }
}
