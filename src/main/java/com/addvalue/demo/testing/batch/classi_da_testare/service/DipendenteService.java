package com.addvalue.demo.testing.batch.classi_da_testare.service;

import com.addvalue.demo.testing.batch.classi_da_testare.rowmappers.DipendenteRowMapper;
import com.addvalue.demo.testing.batch.domains.Dipendente;
import javax.annotation.Resource;
import lombok.Setter;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Setter
public class DipendenteService {

  @Resource private NamedParameterJdbcTemplate namedJdbcTemplate;

  public Dipendente ottieniDipendenteDaMatricola(String matricola) {
    try {
      return this.namedJdbcTemplate.queryForObject(
          "SELECT MATRICOLA, NOME, COGNOME FROM DIPENDENTI WHERE MATRICOLA = :matricola",
          new MapSqlParameterSource().addValue("matricola", matricola),
          new DipendenteRowMapper());
    } catch (IncorrectResultSizeDataAccessException e) {
      return new Dipendente();
    }
  }
}
