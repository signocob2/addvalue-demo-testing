package com.addvalue.demo.testing.batch.tasklet;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.addvalue.demo.testing.batch.domains.Stipendio;
import com.addvalue.demo.testing.batch.exceptions.TestingException;
import com.addvalue.demo.testing.batch.rowmappers.StipendioRowMapper;
import com.addvalue.demo.testing.batch.service.DipendenteService;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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

    final List<Stipendio> stipendi = recuperaStipendi();

    valorizzaNominativiDipendenti(stipendi);

    scriviFileResocontoStipendi(stipendi);

    return RepeatStatus.FINISHED;
  }

  private List<Stipendio> recuperaStipendi() {
    return this.namedParameterJdbcTemplate.query(
        "SELECT MATRICOLA, IMPORTO, DATA_INIZIO, DATA_FINE FROM STIPENDI",
        new StipendioRowMapper());
  }

  private void valorizzaNominativiDipendenti(List<Stipendio> stipendi) {
    stipendi.forEach(
        stipendio ->
            stipendio.setDipendente(
                dipendenteService.ottieniDipendenteDaMatricola(
                    stipendio.getDipendente().getMatricola())));
  }

  private void scriviFileResocontoStipendi(List<Stipendio> stipendi) {
    final File fileResocontoStipendi = ottieniFileSuCuiScrivere();

    stipendi.forEach(
        stipendio -> {
          try {
            FileUtils.writeStringToFile(
                fileResocontoStipendi,
                StringUtils.join(
                        Arrays.asList(
                            stipendio.getDipendente().getMatricola(),
                            stipendio.getDipendente().getNome(),
                            stipendio.getDipendente().getCognome(),
                            stipendio.getImporto(),
                            stipendio.getDataInizio(),
                            stipendio.getDataFine()),
                        ";")
                    + System.lineSeparator(),
                UTF_8,
                true);
          } catch (IOException e) {
            throw new TestingException(
                "Impossibile svuotare il file degli scarti: " + ExceptionUtils.getStackTrace(e));
          }
        });
  }

  private File ottieniFileSuCuiScrivere() {
    File fileResocontoStipendi =
        new File(
            "C:\\Users\\marco.signorini\\OneDrive - ADD VALUE SPA\\Desktop\\Seminario Testing\\resocontoStipendi.csv");

    try {
      FileUtils.writeStringToFile(
          fileResocontoStipendi,
          "matricola;nome;cognome;importo;dataInizio;dataFine" + System.lineSeparator(),
          UTF_8,
          false);
    } catch (IOException e) {
      throw new TestingException(
          "Impossibile scrivere nel file degli scarti: " + ExceptionUtils.getStackTrace(e));
    }
    return fileResocontoStipendi;
  }
}
