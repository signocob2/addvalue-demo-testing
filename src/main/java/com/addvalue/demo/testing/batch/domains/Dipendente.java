package com.addvalue.demo.testing.batch.domains;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Dipendente {

  private String matricola;

  private String nome;

  private String cognome;

  public Dipendente(String matricola) {
    this.matricola = matricola;
  }
}
