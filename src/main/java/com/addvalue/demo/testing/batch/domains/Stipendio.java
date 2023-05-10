package com.addvalue.demo.testing.batch.domains;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class Stipendio {

  private Dipendente dipendente;

  private BigDecimal importo;

  private LocalDate dataInizio;

  private LocalDate dataFine;
}
