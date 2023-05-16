package com.addvalue.demo.testing.batch.classi_da_testare.utils;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.addvalue.demo.testing.batch.exceptions.TestingException;
import java.io.File;
import java.io.IOException;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

@UtilityClass
public class ScritturaFileUtils {

  public static File ottieniFileSuCuiScrivere(String percorsoAssolutoFile, String headerFile) {
    File fileResocontoStipendi = new File(percorsoAssolutoFile);

    try {
      FileUtils.writeStringToFile(
          fileResocontoStipendi, headerFile + System.lineSeparator(), UTF_8, false);
    } catch (IOException e) {
      throw new TestingException(
          "Impossibile scrivere nel file: " + ExceptionUtils.getStackTrace(e));
    }
    return fileResocontoStipendi;
  }
}
