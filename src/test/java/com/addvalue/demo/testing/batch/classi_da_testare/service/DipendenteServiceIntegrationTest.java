package com.addvalue.demo.testing.batch.classi_da_testare.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import com.addvalue.demo.testing.batch.domains.Dipendente;
import javax.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ActiveProfiles("test")
class DipendenteServiceIntegrationTest {

  private static final String SVUOTA_TABELLE = "DELETE FROM TESTAUTOMATICI.DIPENDENTI";

  @Resource private DipendenteService dipendenteService;

  @Resource(name = "jdbcTemplate")
  private JdbcTemplate jdbcTemplate;

  @AfterEach
  public void pulisciContesto() {
    jdbcTemplate.execute(SVUOTA_TABELLE);
  }

  @Test
  @Sql(
      scripts = "/script/integrations/dipendenteService/01_estrazioneSingoloRecord.sql",
      executionPhase = BEFORE_TEST_METHOD)
  void ottieniDipendenteDaMatricola_vieneEstrattoUnRecord_vieneRestituitoIlDipendenteCorretto() {
    final Dipendente dipendente = dipendenteService.ottieniDipendenteDaMatricola("AAA");

    assertThat(dipendente)
        .usingRecursiveComparison()
        .isEqualTo(new Dipendente("AAA", "Pino", "Philip"));
  }

  // TODO-TEST
}
