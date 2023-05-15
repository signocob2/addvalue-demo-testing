package com.addvalue.demo.testing;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Log4j2
@SpringBootApplication
public class TestingApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(TestingApplication.class, args);
    int exitCode = SpringApplication.exit(context);
    log.info("Exit Code: {}", exitCode);
    System.exit(exitCode);
  }
}
