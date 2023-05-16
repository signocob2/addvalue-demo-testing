package com.addvalue.demo.testing.batch.classi_da_testare.rowmappers;

import static com.addvalue.demo.testing.batch.classi_da_testare.utils.DatabaseUtils.recuperaBigDecimal;
import static com.addvalue.demo.testing.batch.classi_da_testare.utils.DatabaseUtils.recuperaData;
import static com.addvalue.demo.testing.batch.classi_da_testare.utils.DatabaseUtils.recuperaStringa;

import com.addvalue.demo.testing.batch.domains.Dipendente;
import com.addvalue.demo.testing.batch.domains.Stipendio;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class StipendioRowMapper implements RowMapper<Stipendio> {

  @Override
  public Stipendio mapRow(ResultSet rs, int rowNum) throws SQLException {

    final Stipendio stipendio = new Stipendio();

    stipendio.setDipendente(new Dipendente(recuperaStringa(rs, "MATRICOLA")));
    stipendio.setImporto(recuperaBigDecimal(rs, "IMPORTO", 0));
    stipendio.setDataInizio(recuperaData(rs, "DATA_INIZIO"));
    stipendio.setDataFine(recuperaData(rs, "DATA_FINE"));

    return stipendio;
  }
}
