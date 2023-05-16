package com.addvalue.demo.testing.batch.classi_da_testare.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class DatabaseUtils {

  public static String recuperaStringa(ResultSet resultSet, String nomeColonna)
      throws SQLException {

    return campoValorizzato(resultSet, nomeColonna, String.class)
        ? StringUtils.trimToEmpty(resultSet.getString(nomeColonna))
        : "";
  }

  public static BigDecimal recuperaBigDecimal(
      ResultSet resultSet, String nomeColonna, int numeroCifreDecimali) throws SQLException {

    return campoValorizzato(resultSet, nomeColonna, BigDecimal.class)
        ? resultSet.getBigDecimal(nomeColonna).setScale(numeroCifreDecimali, RoundingMode.HALF_UP)
        : BigDecimal.ZERO;
  }

  public static LocalDate recuperaData(ResultSet resultSet, String nomeColonna)
      throws SQLException {

    return campoValorizzato(resultSet, nomeColonna, LocalDate.class)
        ? resultSet.getDate(nomeColonna).toLocalDate()
        : LocalDate.of(1, 1, 1);
  }

  private static boolean campoValorizzato(ResultSet resultSet, String nomeColonna, Class<?> classe)
      throws SQLException {
    if (String.class.equals(classe)) {
      return StringUtils.isNotBlank(resultSet.getString(nomeColonna));
    } else if (BigDecimal.class.equals(classe)) {
      return Objects.nonNull(resultSet.getBigDecimal(nomeColonna));
    } else {
      return LocalDate.class.equals(classe) && Objects.nonNull(resultSet.getDate(nomeColonna));
    }
  }
}
