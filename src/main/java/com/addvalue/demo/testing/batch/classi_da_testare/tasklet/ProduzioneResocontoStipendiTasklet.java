package com.addvalue.demo.testing.batch.classi_da_testare.tasklet;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.addvalue.demo.testing.batch.classi_da_testare.rowmappers.StipendioRowMapper;
import com.addvalue.demo.testing.batch.classi_da_testare.service.DipendenteService;
import com.addvalue.demo.testing.batch.classi_da_testare.utils.ScritturaFileUtils;
import com.addvalue.demo.testing.batch.domains.Stipendio;
import com.addvalue.demo.testing.batch.exceptions.TestingException;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
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

  public static final String HEADER_FILE_RESOCONTO_STIPENDI =
      "Matricola;Nome;Cognome;Importo;Data inizio;Data fine";

  public static final String PERCORSO_ASSOLUTO_FILE_RESOCONTO_STIPENDI =
      "C:\\Users\\marco.signorini\\OneDrive - ADD VALUE SPA\\Desktop\\Seminario Testing\\resoconto_stipendi.csv";

  @Resource private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private DipendenteService dipendenteService;

  @Override
  public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {

    List<Stipendio> stipendi = recuperaStipendiAttuali();

    valorizzaDipendenti(stipendi);

    scriviFileResocontoStipendi(stipendi);

    return RepeatStatus.FINISHED;
  }

  private List<Stipendio> recuperaStipendiAttuali() {
    return this.namedParameterJdbcTemplate.query(
        "SELECT A.MATRICOLA, A.IMPORTO, A.DATA_INIZIO, A.DATA_FINE"
            + " FROM STIPENDI A"
            + " WHERE NOT EXISTS (SELECT 1"
            + "                    FROM STIPENDI B"
            + "                   WHERE B.MATRICOLA = A.MATRICOLA"
            + "                     AND B.DATA_FINE > A.DATA_FINE"
            + "                 )",
        new StipendioRowMapper());
  }

  private void valorizzaDipendenti(List<Stipendio> stipendi) {
    stipendi.forEach(
        stipendio ->
            stipendio.setDipendente(
                dipendenteService.ottieniDipendenteDaMatricola(
                    stipendio.getDipendente().getMatricola())));
  }

  private void scriviFileResocontoStipendi(List<Stipendio> stipendi) {
    final File fileResocontoStipendi =
        ScritturaFileUtils.ottieniFileSuCuiScrivere(
            PERCORSO_ASSOLUTO_FILE_RESOCONTO_STIPENDI, HEADER_FILE_RESOCONTO_STIPENDI);

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
                            stipendio.getDataInizio().format(DateTimeFormatter.ISO_DATE),
                            stipendio.getDataFine().format(DateTimeFormatter.ISO_DATE)),
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
}
