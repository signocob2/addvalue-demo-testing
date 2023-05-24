package com.addvalue.demo.testing.batch.classi_da_testare.utils;

import static com.addvalue.demo.testing.batch.classi_da_testare.utils.DatabaseUtils.recuperaBigDecimal;
import static com.addvalue.demo.testing.batch.classi_da_testare.utils.DatabaseUtils.recuperaData;
import static com.addvalue.demo.testing.batch.classi_da_testare.utils.DatabaseUtils.recuperaStringa;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.addvalue.demo.testing.batch.exceptions.TestingException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

class DatabaseUtilsTest {

  private ResultSet resultSet;

  @BeforeEach
  public void inizializzaContesto() {
    resultSet = Mockito.mock(ResultSet.class);
  }

  //
  // ===============================================================================================
  //                                         recuperaStringa
  // ===============================================================================================

  @Test
  void recuperaStringa_stringaNullADb_vieneRestituitaUnaStringaVuota() throws SQLException {
    Mockito.when(resultSet.getString("COLONNA1")).thenReturn(null);

    assertThat(recuperaStringa(resultSet, "COLONNA1")).isEmpty();
  }

  @Test
  void recuperaStringa_stringaConSpaziADb_vieneRestituitaUnaStringaVuota() throws SQLException {
    Mockito.when(resultSet.getString("COLONNA1")).thenReturn("   ");

    assertThat(recuperaStringa(resultSet, "COLONNA1")).isEmpty();
  }

  @Test
  void recuperaStringa_stringaVuotaADb_vieneRestituitaUnaStringaVuota() throws SQLException {
    Mockito.when(resultSet.getString("COLONNA1")).thenReturn("");

    assertThat(recuperaStringa(resultSet, "COLONNA1")).isEmpty();
  }

  @Test
  void recuperaStringa_stringaValorizzataESenzaSpaziADb_vieneRestituitaUnaStringaVuota()
      throws SQLException {
    Mockito.when(resultSet.getString("COLONNA1")).thenReturn("VALORE_DELLA_STRINGA");

    assertThat(recuperaStringa(resultSet, "COLONNA1")).isEqualTo("VALORE_DELLA_STRINGA");
  }

  @Test
  void recuperaStringa_stringaValorizzataEConSpaziADb_vieneRestituitaUnaStringaVuota()
      throws SQLException {
    Mockito.when(resultSet.getString("COLONNA1")).thenReturn(" VALORE_DELLA_STRINGA   ");

    assertThat(recuperaStringa(resultSet, "COLONNA1")).isEqualTo("VALORE_DELLA_STRINGA");
  }

  @ParameterizedTest
  @MethodSource("recuperaStringa_colonnaEsistenteADb")
  void recuperaStringa_colonnaEsistenteADb_vieneRestituitoIlValoreCorretto(
      String valoreColonnaDb, String valoreAtteso) throws SQLException {
    Mockito.when(resultSet.getString("COLONNA1")).thenReturn(valoreColonnaDb);

    assertThat(recuperaStringa(resultSet, "COLONNA1")).isEqualTo(valoreAtteso);
  }

  private static Stream<Arguments> recuperaStringa_colonnaEsistenteADb() {
    return Stream.of(
        Arguments.arguments(null, ""),
        Arguments.arguments("    ", ""),
        Arguments.arguments("", ""),
        Arguments.arguments("VALORE_DELLA_STRINGA", "VALORE_DELLA_STRINGA"),
        Arguments.arguments(" VALORE_DELLA_STRINGA   ", "VALORE_DELLA_STRINGA"));
  }

  @Test
  void recuperaStringa_colonnaNonEsistenteADb_vieneGenerataUnaTestingException()
      throws SQLException {
    Mockito.when(resultSet.getString("COLONNA1")).thenThrow(new SQLException());

    assertThrows(TestingException.class, () -> recuperaStringa(resultSet, "COLONNA1"));
  }

  //
  // ===============================================================================================
  //                                         recuperaBigDecimal
  // ===============================================================================================

  @ParameterizedTest
  @MethodSource("recuperaBigDecimal_colonnaEsistenteADb")
  void recuperaBigDecimal_colonnaEsistenteADb_vieneRestituitoIlValoreCorretto(
      BigDecimal valoreColonnaDb, BigDecimal valoreAtteso) throws SQLException {
    Mockito.when(resultSet.getBigDecimal("COLONNA1")).thenReturn(valoreColonnaDb);

    assertThat(recuperaBigDecimal(resultSet, "COLONNA1", 2)).isEqualTo(valoreAtteso);
  }

  private static Stream<Arguments> recuperaBigDecimal_colonnaEsistenteADb() {
    return Stream.of(
        Arguments.arguments(null, "0.00"),
        Arguments.arguments(BigDecimal.ZERO, new BigDecimal("0.00")),
        Arguments.arguments(new BigDecimal("123.123"), new BigDecimal("123.12")),
        Arguments.arguments(new BigDecimal("123.125"), new BigDecimal("123.13")),
        Arguments.arguments(new BigDecimal("123.88"), new BigDecimal("123.88")));
  }

  @Test
  void recuperaBigDecimal_colonnaNonEsistenteADb_vieneGenerataUnaTestingException()
      throws SQLException {
    Mockito.when(resultSet.getBigDecimal("COLONNA1")).thenThrow(new SQLException());

    assertThrows(TestingException.class, () -> recuperaBigDecimal(resultSet, "COLONNA1", 2));
  }

  //
  // ===============================================================================================
  //                                         recuperaData
  // ===============================================================================================

  @ParameterizedTest
  @MethodSource("recuperaData_colonnaEsistenteADb")
  void recuperaData_colonnaEsistenteADb_vieneRestituitoIlValoreCorretto(
      Date valoreColonnaDb, LocalDate valoreAtteso) throws SQLException {
    Mockito.when(resultSet.getDate("COLONNA1")).thenReturn(valoreColonnaDb);

    assertThat(recuperaData(resultSet, "COLONNA1")).isEqualTo(valoreAtteso);
  }

  private static Stream<Arguments> recuperaData_colonnaEsistenteADb() {
    return Stream.of(
        Arguments.arguments(null, LocalDate.of(1, 1, 1)),
        Arguments.arguments(Date.valueOf(LocalDate.of(1, 1, 1)), LocalDate.of(1, 1, 1)),
        Arguments.arguments(Date.valueOf(LocalDate.of(9999, 12, 31)), LocalDate.of(9999, 12, 31)),
        Arguments.arguments(Date.valueOf(LocalDate.of(2000, 5, 5)), LocalDate.of(2000, 5, 5)),
        Arguments.arguments(Date.valueOf(LocalDate.of(2023, 2, 3)), LocalDate.of(2023, 2, 3)));
  }

  @Test
  void recuperaData_colonnaNonEsistenteADb_vieneGenerataUnaTestingException() throws SQLException {
    Mockito.when(resultSet.getDate("COLONNA1")).thenThrow(new SQLException());

    assertThrows(TestingException.class, () -> recuperaData(resultSet, "COLONNA1"));
  }
}
