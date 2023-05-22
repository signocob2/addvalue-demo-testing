package com.addvalue.demo.testing.integrations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.batch.core.BatchStatus.COMPLETED;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
class ProduzioneResocontoStipendiStepIntegrationTest {

  @Autowired private JobRepositoryTestUtils jobRepositoryTestUtils;

  private JobExecution jobExecution;

  @Autowired private JobLauncherTestUtils jobLauncherTestUtils;

  @BeforeEach
  public void inizializzaContesto() {
    jobExecution = MetaDataInstanceFactory.createJobExecution(1L);
  }

  @AfterEach
  public void pulisciContesto() {
    jobRepositoryTestUtils.removeJobExecutions();
  }

  @Test
  void produzioneResocontoStipendiStep_vieneLanciatoLoStep_terminaInStatoCompleted() {

    JobExecution jobExecutionLancio =
        jobLauncherTestUtils.launchStep(
            "produzioneResocontoStipendiStep", jobExecution.getExecutionContext());

    assertThat(jobExecutionLancio.getStatus()).isEqualTo(COMPLETED);
  }
}
