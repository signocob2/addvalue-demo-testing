package com.addvalue.demo.testing.batch.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class MiaUtils {

  public String vocaliToUpperCase(String parola) {
    StringBuilder nuovaParola = new StringBuilder();

    for (int i = 0; i < StringUtils.length(parola); i++) {
      if (isVocale(parola.charAt(i))) {
        nuovaParola.append(Character.toUpperCase(parola.charAt(i)));
      } else {
        nuovaParola.append(parola.charAt(i));
      }
    }

    return nuovaParola.toString();
  }

  private static boolean isVocale(char c) {
    return "AEIOUaeiou".indexOf(c) != -1;
  }
}
