package com.github.ricardomedeirosdacostajunior.transactions.domain.service;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.valueOf;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.fromString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.github.ricardomedeirosdacostajunior.transactions.domain.dto.AccountDTO;
import com.github.ricardomedeirosdacostajunior.transactions.domain.entity.Account;
import com.github.ricardomedeirosdacostajunior.transactions.domain.exception.InvalidAccountException;
import com.github.ricardomedeirosdacostajunior.transactions.domain.exception.InvalidAvailableLimitCreditException;
import com.github.ricardomedeirosdacostajunior.transactions.domain.repository.AccountRepository;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

  private static final String DOCUMENT_NUMBER = "12345678900";
  private static final BigDecimal AVAILABLE_LIMIT_CREDIT = TEN;
  private static final BigDecimal TRANSACTION_VALUE = ONE;
  private static final String aUUID = "3554cc7e-ae24-4ab7-b52d-fbfd53644bfe";
  private static final String ACCOUNT_NOT_FOUND_MESSAGE = "Account invalid or not found";
  private static final String ACCOUNT_LIMIT_CREDIT_MESSAGE =
      "Available limit credit invalid or not found";
  private static final UUID REQUEST_UUID = fromString(aUUID);

  @InjectMocks private AccountService accountService;

  @Mock private AccountRepository accountRepository;

  @Captor private ArgumentCaptor<Account> accountArgumentCaptor;

  private Account account;

  @BeforeEach
  public void setup() {
    account =
        Account.builder()
            .availableCreditLimit(AVAILABLE_LIMIT_CREDIT)
            .documentNumber(DOCUMENT_NUMBER)
            .uuid(fromString(aUUID))
            .build();
  }

  @Test
  public void accountServiceClassMustBeAnnotatedWithServiceAnnotation() {
    assertThat(AccountService.class.isAnnotationPresent(Service.class), is(true));
  }

  @Test
  public void create() {
    var accountDTO =
        AccountDTO.builder()
            .availableCreditLimit(AVAILABLE_LIMIT_CREDIT)
            .documentNumber(DOCUMENT_NUMBER)
            .build();
    doReturn(account).when(accountRepository).save(any(Account.class));

    var actualAccountDTO = accountService.create(accountDTO);

    verify(accountRepository).save(accountArgumentCaptor.capture());
    var accountCaptured = accountArgumentCaptor.getValue();
    assertAll(
        () -> assertThat(accountCaptured.getUuid(), is(notNullValue())),
        () -> assertThat(accountCaptured.getDocumentNumber(), is(equalTo(DOCUMENT_NUMBER))),
        () ->
            assertThat(
                accountCaptured.getAvailableCreditLimit(), is(equalTo(AVAILABLE_LIMIT_CREDIT))));
    assertAll(
        () -> assertThat(actualAccountDTO.getUuid(), is(equalTo(fromString(aUUID)))),
        () -> assertThat(accountCaptured.getDocumentNumber(), is(equalTo(DOCUMENT_NUMBER))),
        () ->
            assertThat(
                accountCaptured.getAvailableCreditLimit(), is(equalTo(AVAILABLE_LIMIT_CREDIT))));
  }

  @Test
  public void updateAvailableCreditLimit() {
    accountService.updateAvailableCreditLimit(TRANSACTION_VALUE, account);

    verify(accountRepository).save(accountArgumentCaptor.capture());
    assertThat(
        accountArgumentCaptor.getValue().getAvailableCreditLimit(),
        is(closeTo(valueOf(9), valueOf(1))));
  }

  @Test
  public void createMustThrowInvalidAccountExceptionWhenDocumentNumberIsInvalid() {
    var accountDTOWithInvalidAccount =
        AccountDTO.builder().availableCreditLimit(AVAILABLE_LIMIT_CREDIT).build();

    var invalidAccountException =
        assertThrows(
            InvalidAccountException.class,
            () -> accountService.create(accountDTOWithInvalidAccount));

    assertThat(invalidAccountException.getMessage(), is(equalTo(ACCOUNT_NOT_FOUND_MESSAGE)));
  }

  @Test
  public void createMustThrowInvalidAvailableLimitCreditWhenDocumentNumberIsInvalid() {
    var accountDTOWithInvalidAvailableLimitCredit =
        AccountDTO.builder().documentNumber(DOCUMENT_NUMBER).build();

    var invalidAvailableLimitCreditException =
        assertThrows(
            InvalidAvailableLimitCreditException.class,
            () -> accountService.create(accountDTOWithInvalidAvailableLimitCredit));

    assertThat(
        invalidAvailableLimitCreditException.getMessage(),
        is(equalTo(ACCOUNT_LIMIT_CREDIT_MESSAGE)));
  }

  @Test
  public void find() {
    doReturn(of(account)).when(accountRepository).findById(REQUEST_UUID);

    var actualAccountDTO = accountService.find(REQUEST_UUID);

    verify(accountRepository).findById(REQUEST_UUID);
    assertAll(
        () -> assertThat(actualAccountDTO.getUuid(), is(equalTo(fromString(aUUID)))),
        () -> assertThat(actualAccountDTO.getDocumentNumber(), is(equalTo(DOCUMENT_NUMBER))));
  }

  @Test
  public void findWhenThereIsNoAccount() {
    doReturn(empty()).when(accountRepository).findById(REQUEST_UUID);

    var actualAccountDTO = accountService.find(REQUEST_UUID);

    verify(accountRepository).findById(REQUEST_UUID);
    assertThat(actualAccountDTO, is(nullValue()));
  }
}
