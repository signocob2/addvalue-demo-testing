package com.addvalue.demo.testing.esecuzioneJob;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import javax.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@SpringBatchTest
@ActiveProfiles("test")
class LancioTestingJob {

  public static final String PERCORSO_SCRIPT_E2E = "/script/e2e/";

  @Autowired private JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired private JobRepositoryTestUtils jobRepositoryTestUtils;

  @Autowired private JobLauncher jobLauncher;

  @Autowired private Job testing;

  @Resource private JdbcTemplate jdbcTemplate;

  public static final String SVUOTA_TABELLE =
      "DELETE FROM TESTAUTOMATICI.STIPENDI;"
          + "DELETE FROM TESTAUTOMATICI.DIPENDENTI;"
          + "DELETE FROM TESTAUTOMATICI.ASSENZE;";

  @BeforeEach
  public void preparaContesto() {
    jobLauncherTestUtils.setJob(testing);
    jobLauncherTestUtils.setJobLauncher(jobLauncher);
  }

  @AfterEach
  public void pulisciContesto() {
    jobRepositoryTestUtils.removeJobExecutions();
    jdbcTemplate.execute(SVUOTA_TABELLE);
  }

  @Test
  @Sql(
      scripts = PERCORSO_SCRIPT_E2E + "/01_elaborazioneCompleta.sql",
      executionPhase = BEFORE_TEST_METHOD)
  void application_lancioBatch_ilBatchTerminaInStatoCompleted() throws Exception {
    jobLauncherTestUtils.launchJob();
  }
}
