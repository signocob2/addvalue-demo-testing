package com.addvalue.demo.testing.batch.classi_da_testare.tasklet;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.addvalue.demo.testing.batch.classi_da_testare.rowmappers.AssenzaRowMapper;
import com.addvalue.demo.testing.batch.classi_da_testare.service.DipendenteService;
import com.addvalue.demo.testing.batch.classi_da_testare.utils.ScritturaFileUtils;
import com.addvalue.demo.testing.batch.domains.Assenza;
import com.addvalue.demo.testing.batch.exceptions.TestingException;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import lombok.Data;
import lombok.Setter;
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
@Setter
@Log4j2
public class ProduzioneResocontoAssenzeTasklet implements Tasklet {

  public static final String HEADER_FILE_RESOCONTO_ASSENZE = "Matricola;Nome;Cognome;Data assenza";

  private String percorsoAssolutoFileResocontoAssenze;

  @Resource private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private DipendenteService dipendenteService;

  @Override
  public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
    if (StringUtils.isBlank(percorsoAssolutoFileResocontoAssenze)) {
      throw new TestingException("percorsoAssolutoFileResocontoAssenze non valorizzato");
    }

    final List<Assenza> assenze = recuperaAssenze();

    valorizzaDipendenti(assenze);

    scriviFileResocontoAssenze(assenze);

    return RepeatStatus.FINISHED;
  }

  private List<Assenza> recuperaAssenze() {
    return this.namedParameterJdbcTemplate.query(
        "SELECT MATRICOLA, DATA_ASSENZA FROM ASSENZE", new AssenzaRowMapper());
  }

  private void valorizzaDipendenti(List<Assenza> assenze) {
    assenze.forEach(
        assenza ->
            assenza.setDipendente(
                dipendenteService.ottieniDipendenteDaMatricola(
                    assenza.getDipendente().getMatricola())));
  }

  private void scriviFileResocontoAssenze(List<Assenza> assenze) {
    final File fileResocontoAssenze =
        ScritturaFileUtils.ottieniFileSuCuiScrivere(
            percorsoAssolutoFileResocontoAssenze, HEADER_FILE_RESOCONTO_ASSENZE);

    assenze.forEach(
        assenza -> {
          try {
            FileUtils.writeStringToFile(
                fileResocontoAssenze,
                StringUtils.join(
                        Arrays.asList(
                            assenza.getDipendente().getMatricola(),
                            assenza.getDipendente().getNome(),
                            assenza.getDipendente().getCognome(),
                            assenza.getDataAssenza().format(DateTimeFormatter.ISO_DATE)),
                        ";")
                    + System.lineSeparator(),
                UTF_8,
                true);
          } catch (IOException e) {
            throw new TestingException(
                "Impossibile svuotare il file: " + ExceptionUtils.getStackTrace(e));
          }
        });
  }
}
