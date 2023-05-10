package com.addvalue.demo.testing.batch.rowmappers;

import static com.addvalue.demo.testing.batch.utils.DatabaseUtils.recuperaStringa;

import com.addvalue.demo.testing.batch.domains.Dipendente;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class DipendenteRowMapper implements RowMapper<Dipendente> {

  @Override
  public Dipendente mapRow(ResultSet rs, int rowNum) throws SQLException {

    final Dipendente dipendente = new Dipendente();

    dipendente.setMatricola(recuperaStringa(rs, "MATRICOLA"));
    dipendente.setNome(recuperaStringa(rs, "NOME"));
    dipendente.setCognome(recuperaStringa(rs, "COGNOME"));

    return dipendente;
  }
}
