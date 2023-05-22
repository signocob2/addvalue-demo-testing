package com.addvalue.demo.testing.batch.classi_da_testare.utils;

import static com.addvalue.demo.testing.batch.classi_da_testare.utils.ScritturaFileUtils.ottieniFileSuCuiScrivere;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;

import com.addvalue.demo.testing.batch.exceptions.TestingException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ScritturaFileUtilsTest {

  @TempDir private File directoryTemporanea;

  @Test
  void
      ottieniFileSuCuiScrivere_percorsoAssolutoFileNonValorizzato_vieneGenerataUnaTestingException() {
    assertThrows(TestingException.class, () -> ottieniFileSuCuiScrivere(null, null));
  }

  @Test
  void ottieniFileSuCuiScrivere_erroreDiIOInFaseDiCreazioneFile_vieneGenerataUnaTestingException() {

    try (MockedStatic<FileUtils> mockedStatic = Mockito.mockStatic(FileUtils.class)) {
      mockedStatic.when(() -> FileUtils.touch(any())).thenThrow(IOException.class);

      assertThrows(
          TestingException.class,
          () ->
              ottieniFileSuCuiScrivere(
                  directoryTemporanea.getAbsolutePath() + "/mioFile.csv", null));
    }
  }

  @Test
  void ottieniFileSuCuiScrivere_erroreDiIOInFaseDiScritturaFile_vieneGenerataUnaTestingException() {

    try (MockedStatic<FileUtils> mockedStatic = Mockito.mockStatic(FileUtils.class)) {
      mockedStatic
          .when(
              () ->
                  FileUtils.writeStringToFile(any(), anyString(), any(Charset.class), anyBoolean()))
          .thenThrow(IOException.class);

      assertThrows(
          TestingException.class,
          () ->
              ottieniFileSuCuiScrivere(
                  directoryTemporanea.getAbsolutePath() + "/mioFile.csv", "header"));
    }
  }

  @Test
  void ottieniFileSuCuiScrivere_headerNonValorizzato_vieneCreatoIlFileVuoto() {
    final File fileOttenuto =
        ottieniFileSuCuiScrivere(directoryTemporanea.getAbsolutePath() + "/mioFile.csv", null);

    assertThat(fileOttenuto).exists().isEmpty();
  }

  @Test
  void ottieniFileSuCuiScrivere_headerValorizzato_vieneCreatoIlFileConLHeader() throws IOException {
    final File fileOttenuto =
        ottieniFileSuCuiScrivere(
            directoryTemporanea.getAbsolutePath() + "/mioFile.csv", "headerTest");

    assertThat(fileOttenuto).exists();
    assertThat(FileUtils.readFileToString(fileOttenuto, UTF_8))
        .isEqualTo("headerTest" + System.lineSeparator());
  }
}
