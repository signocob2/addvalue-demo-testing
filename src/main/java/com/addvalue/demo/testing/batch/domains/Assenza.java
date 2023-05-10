package com.addvalue.demo.testing.batch.domains;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Assenza {

  private String matricola;

  private LocalDate dataAssenza;
}
