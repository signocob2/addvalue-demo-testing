package com.addvalue.demo.testing.batch.classi_da_testare.rowmappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.addvalue.demo.testing.batch.domains.Assenza;
import com.addvalue.demo.testing.batch.domains.Dipendente;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AssenzaRowMapperTest {

  private ResultSet resultSet;

  @Test
  void mapRow_mappaturaCampiCorretta_vieneRestituitaUnAssenza() throws Exception {
    resultSet = Mockito.mock(ResultSet.class);
    inizializzaDatiMockati();

    final AssenzaRowMapper assenzaRowMapper = new AssenzaRowMapper();
    final Assenza assenzaOttenuta = assenzaRowMapper.mapRow(resultSet, 1);

    final Assenza assenzaAttesa = new Assenza();
    assenzaAttesa.setDipendente(new Dipendente("ABC"));
    assenzaAttesa.setDataAssenza(LocalDate.of(2023, 5, 5));
    assertThat(assenzaOttenuta).usingRecursiveComparison().isEqualTo(assenzaAttesa);
  }

  private void inizializzaDatiMockati() throws Exception {
    when(resultSet.getString("MATRICOLA")).thenReturn("ABC");
    when(resultSet.getDate("DATA_ASSENZA")).thenReturn(Date.valueOf(LocalDate.of(2023, 5, 5)));
  }
}
