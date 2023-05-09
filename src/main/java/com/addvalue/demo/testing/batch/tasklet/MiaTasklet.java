package com.addvalue.demo.testing.batch.tasklet;

import com.addvalue.demo.testing.batch.utils.MiaUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

@Data
@Log4j2
public class MiaTasklet implements Tasklet {

  private JdbcTemplate jdbcTemplate;

  @Override
  public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {

    final List<String> valoriDellaMiaTabella =
        this.jdbcTemplate.queryForList("SELECT COLONNA_1 FROM MIA_TABELLA", String.class);

    final List<String> valoriDellaMiaTabellaConVocaliMaiuscole =
        ottieniValoriDellaMiaTabellaConVocaliMaiuscole(valoriDellaMiaTabella);

    valoriDellaMiaTabellaConVocaliMaiuscole.forEach(log::info);

    return RepeatStatus.FINISHED;
  }

  private List<String> ottieniValoriDellaMiaTabellaConVocaliMaiuscole(
      List<String> valoriDellaMiaTabella) {
    List<String> valoriDellaMiaTabellaConVocaliMaiuscole = new ArrayList<>();

    for (String valore : valoriDellaMiaTabella) {
      valoriDellaMiaTabellaConVocaliMaiuscole.add(MiaUtils.vocaliToUpperCase(valore));
    }

    return valoriDellaMiaTabellaConVocaliMaiuscole;
  }
}
