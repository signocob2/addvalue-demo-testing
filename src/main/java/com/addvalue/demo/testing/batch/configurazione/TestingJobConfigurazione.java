package com.addvalue.demo.testing.batch.configurazione;

import com.addvalue.demo.testing.batch.classi_da_testare.service.DipendenteService;
import com.addvalue.demo.testing.batch.classi_da_testare.tasklet.ProduzioneResocontoAssenzeTasklet;
import com.addvalue.demo.testing.batch.classi_da_testare.tasklet.ProduzioneResocontoStipendiTasklet;
import com.addvalue.demo.testing.batch.incrementer.CustomRunIncrementerId;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
@EnableBatchProcessing
@Log4j2
public class TestingJobConfigurazione {

  public static final String PERCORSO_ASSOLUTO_FILE_RESOCONTO_STIPENDI =
      "C:\\Users\\marco.signorini\\Desktop\\Seminario Testing\\2024\\resoconto_stipendi.csv";

  public static final String PERCORSO_ASSOLUTO_FILE_RESOCONTO_ASSENZE =
      "C:\\Users\\marco.signorini\\Desktop\\Seminario Testing\\2024\\Seminario Testing\\resoconto_assenze.csv";

  @Bean
  public Job testing(
      JobBuilderFactory jobBuilderFactory,
      Step produzioneResocontoStipendiStep,
      Step produzioneResocontoAssenzeStep) {
    return jobBuilderFactory
        .get("testing")
        .incrementer(new CustomRunIncrementerId())
        .preventRestart()
        .start(produzioneResocontoStipendiStep)
        .next(produzioneResocontoAssenzeStep)
        .build();
  }

  @Bean
  public Step produzioneResocontoStipendiStep(
      StepBuilderFactory stepBuilderFactory,
      ProduzioneResocontoStipendiTasklet produzioneResocontoStipendiTasklet) {
    return stepBuilderFactory
        .get("produzioneResocontoStipendiStep")
        .tasklet(produzioneResocontoStipendiTasklet)
        .build();
  }

  @Bean
  @StepScope
  public ProduzioneResocontoStipendiTasklet produzioneResocontoStipendiTasklet(
      NamedParameterJdbcTemplate namedJdbcTemplate, DipendenteService dipendenteService) {

    final ProduzioneResocontoStipendiTasklet produzioneResocontoStipendiTasklet =
        new ProduzioneResocontoStipendiTasklet();
    produzioneResocontoStipendiTasklet.setPercorsoAssolutoFileResocontoStipendi(
        PERCORSO_ASSOLUTO_FILE_RESOCONTO_STIPENDI);

    produzioneResocontoStipendiTasklet.setNamedParameterJdbcTemplate(namedJdbcTemplate);
    produzioneResocontoStipendiTasklet.setDipendenteService(dipendenteService);

    return produzioneResocontoStipendiTasklet;
  }

  @Bean
  public Step produzioneResocontoAssenzeStep(
      StepBuilderFactory stepBuilderFactory,
      ProduzioneResocontoAssenzeTasklet produzioneResocontoAssenzeTasklet) {
    return stepBuilderFactory
        .get("produzioneResocontoAssenzeStep")
        .tasklet(produzioneResocontoAssenzeTasklet)
        .build();
  }

  @Bean
  @StepScope
  public ProduzioneResocontoAssenzeTasklet produzioneResocontoAssenzeTasklet(
      NamedParameterJdbcTemplate namedJdbcTemplate, DipendenteService dipendenteService) {

    final ProduzioneResocontoAssenzeTasklet produzioneResocontoAssenzeTasklet =
        new ProduzioneResocontoAssenzeTasklet();
    produzioneResocontoAssenzeTasklet.setPercorsoAssolutoFileResocontoAssenze(
        PERCORSO_ASSOLUTO_FILE_RESOCONTO_ASSENZE);

    produzioneResocontoAssenzeTasklet.setNamedParameterJdbcTemplate(namedJdbcTemplate);
    produzioneResocontoAssenzeTasklet.setDipendenteService(dipendenteService);

    return produzioneResocontoAssenzeTasklet;
  }
}
