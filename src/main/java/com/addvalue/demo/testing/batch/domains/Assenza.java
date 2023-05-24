package com.addvalue.demo.testing.batch.domains;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assenza {

  private Dipendente dipendente;

  private LocalDate dataAssenza;
}
