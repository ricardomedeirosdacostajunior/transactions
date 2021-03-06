package com.github.ricardomedeirosdacostajunior.transactions.domain.enumeration;

import static com.github.ricardomedeirosdacostajunior.transactions.domain.enumeration.OperationTypesEnumeration.IN_CASH;
import static com.github.ricardomedeirosdacostajunior.transactions.domain.enumeration.OperationTypesEnumeration.IN_INSTALLMENTS;
import static com.github.ricardomedeirosdacostajunior.transactions.domain.enumeration.OperationTypesEnumeration.PAYMENT;
import static com.github.ricardomedeirosdacostajunior.transactions.domain.enumeration.OperationTypesEnumeration.WITHDRAW;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.ricardomedeirosdacostajunior.transactions.domain.exception.InvalidOperationTypeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

public class OperationTypesEnumerationTest {

  private static final String OPERATION_TYPE_INVALID_MESSAGE = "Operation type is invalid";

  @ParameterizedTest
  @EnumSource(value = OperationTypesEnumeration.class)
  public void getValueFromOperationTypesIsValid(
      final OperationTypesEnumeration operationTypesEnumeration) {
    var validOperationTypes = asList(IN_CASH, IN_INSTALLMENTS, WITHDRAW, PAYMENT);

    assertThat(validOperationTypes, hasItem(operationTypesEnumeration));
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4})
  public void valueOf(int value) {
    var operationType = OperationTypesEnumeration.valueOf(value);

    assertThat(operationType, is(notNullValue()));
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 5})
  public void valueOfWithInvalidValues(int invalidValue) {
    var invalidOperationType =
        assertThrows(
            InvalidOperationTypeException.class,
            () -> OperationTypesEnumeration.valueOf(invalidValue));

    assertThat(invalidOperationType.getMessage(), is(equalTo(OPERATION_TYPE_INVALID_MESSAGE)));
  }

  @Test
  public void valueOfWithNullValue() {
    final Integer nullValue = null;

    var invalidOperationType =
        assertThrows(
            InvalidOperationTypeException.class,
            () -> OperationTypesEnumeration.valueOf(nullValue));

    assertThat(invalidOperationType.getMessage(), is(equalTo(OPERATION_TYPE_INVALID_MESSAGE)));
  }

  @ParameterizedTest
  @EnumSource(
      value = OperationTypesEnumeration.class,
      names = {"IN_CASH", "IN_INSTALLMENTS", "WITHDRAW"})
  public void isNegative(final OperationTypesEnumeration operationType) {
    assertThat(operationType.isNegative(), is(true));
  }

  @ParameterizedTest
  @EnumSource(
      value = OperationTypesEnumeration.class,
      names = {"PAYMENT"})
  public void isPositive(final OperationTypesEnumeration operationType) {
    assertThat(operationType.isNegative(), is(false));
  }
}
