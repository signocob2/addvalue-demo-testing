package com.addvalue.demo.testing.batch.configurazione;

import com.addvalue.demo.testing.batch.incrementer.CustomRunIncrementerId;
import com.addvalue.demo.testing.batch.service.DipendenteService;
import com.addvalue.demo.testing.batch.tasklet.ProduzioneResocontoAssenzeTasklet;
import com.addvalue.demo.testing.batch.tasklet.ProduzioneResocontoStipendiTasklet;
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

  @Bean
  public Job testing(
      JobBuilderFactory jobBuilderFactory,
      Step produzioneResocontoStipendi,
      Step produzioneResocontoAssenze) {
    return jobBuilderFactory
        .get("testing")
        .incrementer(new CustomRunIncrementerId())
        .preventRestart()
        .start(produzioneResocontoStipendi)
        .next(produzioneResocontoAssenze)
        .build();
  }

  @Bean
  public Step produzioneResocontoStipendi(
      StepBuilderFactory stepBuilderFactory,
      ProduzioneResocontoStipendiTasklet produzioneResocontoStipendiTasklet) {
    return stepBuilderFactory
        .get("produzioneResocontoStipendi")
        .tasklet(produzioneResocontoStipendiTasklet)
        .build();
  }

  @Bean
  @StepScope
  public ProduzioneResocontoStipendiTasklet produzioneResocontoStipendiTasklet(
      NamedParameterJdbcTemplate namedJdbcTemplate, DipendenteService dipendenteService) {

    ProduzioneResocontoStipendiTasklet produzioneResocontoStipendiTasklet =
        new ProduzioneResocontoStipendiTasklet();

    produzioneResocontoStipendiTasklet.setNamedParameterJdbcTemplate(namedJdbcTemplate);
    produzioneResocontoStipendiTasklet.setDipendenteService(dipendenteService);

    return produzioneResocontoStipendiTasklet;
  }

  @Bean
  public Step produzioneResocontoAssenze(
      StepBuilderFactory stepBuilderFactory,
      ProduzioneResocontoAssenzeTasklet produzioneResocontoAssenzeTasklet) {
    return stepBuilderFactory
        .get("produzioneResocontoStipendi")
        .tasklet(produzioneResocontoAssenzeTasklet)
        .build();
  }

  @Bean
  @StepScope
  public ProduzioneResocontoAssenzeTasklet produzioneResocontoAssenzeTasklet(
      NamedParameterJdbcTemplate namedJdbcTemplate, DipendenteService dipendenteService) {

    ProduzioneResocontoAssenzeTasklet produzioneResocontoAssenzeTasklet =
        new ProduzioneResocontoAssenzeTasklet();

    produzioneResocontoAssenzeTasklet.setNamedParameterJdbcTemplate(namedJdbcTemplate);
    produzioneResocontoAssenzeTasklet.setDipendenteService(dipendenteService);

    return produzioneResocontoAssenzeTasklet;
  }
}
