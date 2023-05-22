package com.addvalue.demo.testing.batch.classi_da_testare.rowmappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.addvalue.demo.testing.batch.domains.Dipendente;
import java.sql.ResultSet;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DipendenteRowMapperTest {

  private ResultSet resultSet;

  @Test
  void mapRow_mappaturaCampiCorretta_vieneRestituitoUnDipendente() throws Exception {
    resultSet = Mockito.mock(ResultSet.class);
    inizializzaDatiMockati();

    final DipendenteRowMapper dipendenteRowMapper = new DipendenteRowMapper();
    final Dipendente dipendenteOttenuto = dipendenteRowMapper.mapRow(resultSet, 1);

    final Dipendente dipendenteAtteso = new Dipendente();
    dipendenteAtteso.setMatricola("ABC");
    dipendenteAtteso.setNome("MARCO");
    dipendenteAtteso.setCognome("SIGNORINI");
    assertThat(dipendenteOttenuto).usingRecursiveComparison().isEqualTo(dipendenteAtteso);
  }

  private void inizializzaDatiMockati() throws Exception {
    when(resultSet.getString("MATRICOLA")).thenReturn("ABC");
    when(resultSet.getString("NOME")).thenReturn("MARCO");
    when(resultSet.getString("COGNOME")).thenReturn("SIGNORINI");
  }
}
