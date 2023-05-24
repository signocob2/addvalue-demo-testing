package com.addvalue.demo.testing.batch.classi_da_testare.rowmappers;

import static com.addvalue.demo.testing.batch.classi_da_testare.utils.DatabaseUtils.recuperaData;
import static com.addvalue.demo.testing.batch.classi_da_testare.utils.DatabaseUtils.recuperaStringa;

import com.addvalue.demo.testing.batch.domains.Assenza;
import com.addvalue.demo.testing.batch.domains.Dipendente;
import java.sql.ResultSet;
import org.springframework.jdbc.core.RowMapper;

public class AssenzaRowMapper implements RowMapper<Assenza> {

  @Override
  public Assenza mapRow(ResultSet rs, int rowNum) {
    final Assenza assenza = new Assenza();

    assenza.setDipendente(new Dipendente(recuperaStringa(rs, "MATRICOLA")));
    assenza.setDataAssenza(recuperaData(rs, "DATA_ASSENZA"));

    return assenza;
  }
}
