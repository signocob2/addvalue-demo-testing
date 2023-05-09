package com.addvalue.demo.testing.batch.incrementer;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

public class CustomRunIncrementerId implements JobParametersIncrementer {

  private static final String RUN_ID_KEY = "oraCorrente";
  private String key = RUN_ID_KEY;

  @Override
  public final JobParameters getNext(final JobParameters parameters) {
    return new JobParametersBuilder().addLong(key, System.currentTimeMillis()).toJobParameters();
  }
}
