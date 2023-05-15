package com.addvalue.demo.testing;

import javax.sql.DataSource;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class BatchConfiguration extends DefaultBatchConfigurer {
  @Autowired
  @Qualifier("dataSource")
  private DataSource dataSource;

  @Autowired
  @Qualifier("transactionManagerDataSource")
  private DataSourceTransactionManager transactionManagerDataSource;

  @Override
  protected JobRepository createJobRepository() throws Exception {
    JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
    factory.setDataSource(dataSource);
    factory.setTransactionManager(transactionManagerDataSource);
    factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");

    return factory.getObject();
  }
}
