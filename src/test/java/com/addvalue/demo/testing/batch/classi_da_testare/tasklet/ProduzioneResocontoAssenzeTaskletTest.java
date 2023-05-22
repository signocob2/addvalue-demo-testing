package com.addvalue.demo.testing.batch.classi_da_testare.tasklet;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.addvalue.demo.testing.batch.classi_da_testare.service.DipendenteService;
import com.addvalue.demo.testing.batch.domains.Assenza;
import com.addvalue.demo.testing.batch.domains.Dipendente;
import com.addvalue.demo.testing.batch.exceptions.TestingException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

class ProduzioneResocontoAssenzeTaskletTest {

  @TempDir private File directoryTemporanea;

  private ProduzioneResocontoAssenzeTasklet produzioneResocontoAssenzeTasklet;

  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private DipendenteService dipendenteService;

  @BeforeEach
  public void before() {
    MockitoAnnotations.openMocks(this);
    namedParameterJdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);
    dipendenteService = Mockito.mock(DipendenteService.class);

    produzioneResocontoAssenzeTasklet = new ProduzioneResocontoAssenzeTasklet();
    produzioneResocontoAssenzeTasklet.setPercorsoAssolutoFileResocontoAssenze(
        directoryTemporanea.getAbsolutePath() + "/mioFile.csv");
    produzioneResocontoAssenzeTasklet.setNamedParameterJdbcTemplate(namedParameterJdbcTemplate);
    produzioneResocontoAssenzeTasklet.setDipendenteService(dipendenteService);
  }

  @Test
  void
      execute_percorsoAssolutoFileResocontoAssenzeNonValorizzato_vieneGenerataUnaTestingException() {
    produzioneResocontoAssenzeTasklet.setPercorsoAssolutoFileResocontoAssenze(null);

    assertThrows(
        TestingException.class, () -> produzioneResocontoAssenzeTasklet.execute(null, null));
  }

  @Test
  void execute_nessunaAssenzaTrovata_vieneScrittoIlFileVuotoELaTaskletTerminaCorrettamente()
      throws IOException {
    Mockito.when(namedParameterJdbcTemplate.query(anyString(), any(RowMapper.class)))
        .thenReturn(Collections.emptyList());
    final RepeatStatus repeatStatus = produzioneResocontoAssenzeTasklet.execute(null, null);

    assertThat(
            FileUtils.readFileToString(
                new File(directoryTemporanea.getAbsolutePath() + "/mioFile.csv"), UTF_8))
        .isEqualTo(
            ProduzioneResocontoAssenzeTasklet.HEADER_FILE_RESOCONTO_ASSENZE
                + System.lineSeparator());
    assertThat(repeatStatus).isEqualTo(RepeatStatus.FINISHED);
  }

  @Test
  void
      execute_unaSolaAssenzaPerUnSoloDipendenteTrovata_vieneScrittoIlFileCorrettoELaTaskletTerminaCorrettamente()
          throws IOException {
    Mockito.when(namedParameterJdbcTemplate.query(anyString(), any(RowMapper.class)))
        .thenReturn(
            Collections.singletonList(
                new Assenza(
                    new Dipendente("ABC", "MARCO", "SIGNORINI"), LocalDate.of(2023, 5, 5))));
    Mockito.when(dipendenteService.ottieniDipendenteDaMatricola("ABC"))
        .thenReturn(new Dipendente("ABC", "MARCO", "SIGNORINI"));

    final RepeatStatus repeatStatus = produzioneResocontoAssenzeTasklet.execute(null, null);

    assertThat(
            FileUtils.readFileToString(
                new File(directoryTemporanea.getAbsolutePath() + "/mioFile.csv"), UTF_8))
        .isEqualTo(
            ProduzioneResocontoAssenzeTasklet.HEADER_FILE_RESOCONTO_ASSENZE
                + System.lineSeparator()
                + "ABC;MARCO;SIGNORINI;2023-05-05"
                + System.lineSeparator());
    assertThat(repeatStatus).isEqualTo(RepeatStatus.FINISHED);
  }
}
