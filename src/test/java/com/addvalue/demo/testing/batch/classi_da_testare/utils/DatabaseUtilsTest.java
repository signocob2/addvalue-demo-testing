package com.addvalue.demo.testing.batch.classi_da_testare.utils;

import static com.addvalue.demo.testing.batch.classi_da_testare.utils.DatabaseUtils.recuperaStringa;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.ResultSet;
import java.sql.SQLException;
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

  @ParameterizedTest
  @MethodSource("recuperaStringa_colonnaEsistenteADb")
  void recuperaStringa_colonnaEsistenteADb_vieneRestituitoIlValoreCorretto(
      String valoreColonnaDb, String valoreAtteso) throws SQLException {
    Mockito.when(resultSet.getString("COLONNA1")).thenReturn(valoreColonnaDb);

    assertThat(recuperaStringa(resultSet, "COLONNA1")).isEqualTo(valoreAtteso);
  }

  private static Stream<Arguments> recuperaStringa_colonnaEsistenteADb() {
    return Stream.of(
        // TODO-TEST
        Arguments.arguments(" VALORE_DELLA_STRINGA   ", "VALORE_DELLA_STRINGA"));
  }

  // TODO-TEST
}
