package com.addvalue.demo.testing.batch.tasklet;

import com.addvalue.demo.testing.batch.domains.Stipendio;
import com.addvalue.demo.testing.batch.rowmappers.StipendioRowMapper;
import com.addvalue.demo.testing.batch.service.DipendenteService;
import java.util.List;
import javax.annotation.Resource;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Data
@Log4j2
public class ProduzioneResocontoStipendiTasklet implements Tasklet {

  @Resource private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private DipendenteService dipendenteService;

  @Override
  public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {

    final List<Stipendio> stipendi =
        this.namedParameterJdbcTemplate.query(
            "SELECT MATRICOLA, IMPORTO, DATA_INIZIO, DATA_FINE FROM STIPENDI",
            new StipendioRowMapper());

    valorizzaNominativiDipendenti(stipendi);

    return RepeatStatus.FINISHED;
  }

  private void valorizzaNominativiDipendenti(List<Stipendio> stipendi) {
    stipendi.forEach(
        stipendio ->
            stipendio.setDipendente(
                dipendenteService.ottieniDipendenteDaMatricola(
                    stipendio.getDipendente().getMatricola())));
  }
}
