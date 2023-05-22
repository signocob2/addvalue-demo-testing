package com.addvalue.demo.testing.batch.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dipendente {

  private String matricola;

  private String nome;

  private String cognome;

  public Dipendente(String matricola) {
    this.matricola = matricola;
  }
}
