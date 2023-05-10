package com.addvalue.demo.testing.batch.service;

import com.addvalue.demo.testing.batch.domains.Dipendente;
import com.addvalue.demo.testing.batch.rowmappers.DipendenteRowMapper;
import javax.annotation.Resource;
import lombok.Setter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Setter
public class DipendenteService {

  @Resource private NamedParameterJdbcTemplate namedJdbcTemplateDb2;

  public Dipendente ottieniDipendenteDaMatricola(String matricola) {
    return this.namedJdbcTemplateDb2.queryForObject(
        "SELECT MATRICOLA, NOME, COGNOME FROM DIPENDENTI WHERE MATRICOLA = :matricola",
        new MapSqlParameterSource().addValue("matricola", matricola),
        new DipendenteRowMapper());
  }
}
