package com.addvalue.demo.testing.batch.classi_da_testare.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.addvalue.demo.testing.batch.domains.Dipendente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

class DipendenteServiceTest {

  @Mock private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @InjectMocks private DipendenteService dipendenteService;

  @BeforeEach
  public void before() {
    MockitoAnnotations.openMocks(this);
    dipendenteService.setNamedJdbcTemplate(namedParameterJdbcTemplate);
  }

  @Test
  void
      ottieniDipendenteDaMatricola_laQueryDiEstrazioneNonTrovaAlcunRecord_ritornaDipendenteDiDefault() {
    when(namedParameterJdbcTemplate.queryForObject(
            anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
        .thenThrow(new EmptyResultDataAccessException("Zero record", 1));

    assertThat(dipendenteService.ottieniDipendenteDaMatricola("ABC")).isEqualTo(new Dipendente());
  }

  // TODO-TEST
}
