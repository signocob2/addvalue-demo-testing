package com.addvalue.demo.testing.batch.classi_da_testare.utils;

import com.addvalue.demo.testing.batch.exceptions.TestingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

@UtilityClass
public class DatabaseUtils {

  public static String recuperaStringa(ResultSet resultSet, String nomeColonna) {

    try {

      final String valore = resultSet.getString(nomeColonna);

      return StringUtils.trimToEmpty(valore);
    } catch (SQLException e) {
      throw new TestingException(ExceptionUtils.getMessage(e));
    }
  }

  public static BigDecimal recuperaBigDecimal(
      ResultSet resultSet, String nomeColonna, int numeroCifreDecimali) {

    try {

      final BigDecimal valore = resultSet.getBigDecimal(nomeColonna);

      return Objects.nonNull(valore)
          ? valore.setScale(numeroCifreDecimali, RoundingMode.HALF_UP)
          : BigDecimal.ZERO.setScale(numeroCifreDecimali, RoundingMode.HALF_UP);
    } catch (SQLException e) {
      throw new TestingException(ExceptionUtils.getMessage(e));
    }
  }

  public static LocalDate recuperaData(ResultSet resultSet, String nomeColonna) {

    try {

      final Date valore = resultSet.getDate(nomeColonna);

      return Objects.nonNull(valore) ? valore.toLocalDate() : LocalDate.of(1, 1, 1);
    } catch (SQLException e) {
      throw new TestingException(ExceptionUtils.getMessage(e));
    }
  }
}
