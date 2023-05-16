package com.addvalue.demo.testing.batch.exceptions;

import lombok.Getter;

@Getter
public class TestingException extends RuntimeException {

  public TestingException(String messaggio) {
    super("Ops! Questo è imbarazzante - " + messaggio);
  }
}
