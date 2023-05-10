package com.addvalue.demo.testing.batch.configurazione;

import com.addvalue.demo.testing.batch.incrementer.CustomRunIncrementerId;
import com.addvalue.demo.testing.batch.tasklet.ProduzioneResocontoStipendiTasklet;
import javax.sql.DataSource;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
@EnableBatchProcessing
@Log4j2
public class TestingJobConfigurazione {

  @Autowired private StepBuilderFactory stepBuilderFactory;

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
      ProduzioneResocontoStipendiTasklet produzioneResocontoStipendiTasklet) {
    return stepBuilderFactory
        .get("produzioneResocontoStipendi")
        .tasklet(produzioneResocontoStipendiTasklet)
        .build();
  }

  @Bean
  @StepScope
  public ProduzioneResocontoStipendiTasklet produzioneResocontoStipendiTasklet(
      NamedParameterJdbcTemplate namedJdbcTemplate) {

    ProduzioneResocontoStipendiTasklet produzioneResocontoStipendiTasklet =
        new ProduzioneResocontoStipendiTasklet();

    produzioneResocontoStipendiTasklet.setNamedParameterJdbcTemplate(namedJdbcTemplate);

    return produzioneResocontoStipendiTasklet;
  }

  /*----- BEAN DI CONFIGURAZIONE -----*/

  @Bean(name = "dataSource")
  @ConfigurationProperties(prefix = "database.datasource")
  public DataSource dataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = {"namedJdbcTemplate"})
  public NamedParameterJdbcTemplate namedJdbcTemplate(
      @Qualifier("dataSource") DataSource dataSource) {
    return new NamedParameterJdbcTemplate(dataSource);
  }
}
