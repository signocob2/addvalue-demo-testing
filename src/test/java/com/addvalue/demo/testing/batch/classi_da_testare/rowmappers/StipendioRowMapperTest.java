package com.addvalue.demo.testing.batch.classi_da_testare.rowmappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.addvalue.demo.testing.batch.domains.Dipendente;
import com.addvalue.demo.testing.batch.domains.Stipendio;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class StipendioRowMapperTest {

  private ResultSet resultSet;

  @Test
  void mapRow_mappaturaCampiCorretta_vieneRestituitoUnoStipendio() throws Exception {
    resultSet = Mockito.mock(ResultSet.class);
    inizializzaDatiMockati();

    final StipendioRowMapper stipendioRowMapper = new StipendioRowMapper();
    final Stipendio stipendioOttenuto = stipendioRowMapper.mapRow(resultSet, 1);

    final Stipendio stipendioAtteso = new Stipendio();
    stipendioAtteso.setDipendente(new Dipendente("ABC"));
    stipendioAtteso.setImporto(new BigDecimal("1000"));
    stipendioAtteso.setDataInizio(LocalDate.of(2020, 2, 1));
    stipendioAtteso.setDataFine(LocalDate.of(2023, 5, 5));
    assertThat(stipendioOttenuto).usingRecursiveComparison().isEqualTo(stipendioAtteso);
  }

  private void inizializzaDatiMockati() throws Exception {
    when(resultSet.getString("MATRICOLA")).thenReturn("ABC");
    when(resultSet.getBigDecimal("IMPORTO")).thenReturn(new BigDecimal("1000.12"));
    when(resultSet.getDate("DATA_INIZIO")).thenReturn(Date.valueOf(LocalDate.of(2020, 2, 1)));
    when(resultSet.getDate("DATA_FINE")).thenReturn(Date.valueOf(LocalDate.of(2023, 5, 5)));
  }
}
