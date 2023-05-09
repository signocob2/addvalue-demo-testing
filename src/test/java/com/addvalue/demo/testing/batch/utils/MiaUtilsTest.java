package com.addvalue.demo.testing.batch.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MiaUtilsTest {

  @Test
  void
      vocaliToUpperCase_passoUnaStringaConQualcheVocaleEQualcheConsonante_vieneRestituitaLaStringaOriginaleConLeVocaliInMaiuscolo() {
    assertThat(MiaUtils.vocaliToUpperCase("Ciao amici, come va?"))
        .isEqualTo("CIAO AmIcI, cOmE vA?");
  }
}
