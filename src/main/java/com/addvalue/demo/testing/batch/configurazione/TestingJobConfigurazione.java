package com.addvalue.demo.testing.batch.configurazione;

import com.addvalue.demo.testing.batch.incrementer.CustomRunIncrementerId;
import com.addvalue.demo.testing.batch.tasklet.MiaTasklet;
import javax.sql.DataSource;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableBatchProcessing
@Log4j2
public class TestingJobConfigurazione {

  @Autowired private StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job testing(JobBuilderFactory jobBuilderFactory, Step stepProva) {
    return jobBuilderFactory
        .get("testing")
        .incrementer(new CustomRunIncrementerId())
        .preventRestart()
        .start(stepProva)
        .build();
  }

  @Bean
  public Step stepProva(Tasklet taskletProva) {
    return stepBuilderFactory.get("stepProva").tasklet(taskletProva).build();
  }

  @Bean
  @StepScope
  public MiaTasklet taskletProva(JdbcTemplate jdbcTemplate) {

    MiaTasklet miaTasklet = new MiaTasklet();

    miaTasklet.setJdbcTemplate(jdbcTemplate);

    return miaTasklet;
  }

  @Bean(name = "dataSource")
  @ConfigurationProperties(prefix = "database.datasource")
  public DataSource dataSource() {
    return DataSourceBuilder.create().build();
  }
}
