package com.pgms.shared.util;

import java.util.function.Consumer;

public class Utility {

  public static <T> void updateIfNotNull(
    T newValue,
    Consumer<T> setter
  ) {
    if (newValue != null) setter.accept(newValue);
  }
}
