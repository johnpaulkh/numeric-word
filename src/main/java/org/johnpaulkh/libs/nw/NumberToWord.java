package org.johnpaulkh.libs.nw;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class NumberToWord {

  private static final List<Number> NUMBERS = ImmutableList.<Number>builder()
      .add(
          new Number(1_000_000_000_000L, "triliyun"),
          new Number(1_000_000_000L, "milyar"),
          new Number(1_000_000L, "juta"),
          new Number(1_000L, "ribu"),
          new Number(100L, "ratus"),
          new Number(10L, "puluh"),
          new Number(9L, "sembilan"),
          new Number(8L, "delapan"),
          new Number(7L, "tujuh"),
          new Number(6L, "enam"),
          new Number(5L, "lima"),
          new Number(4L, "empat"),
          new Number(3L, "tiga"),
          new Number(2L, "dua"),
          new Number(1L, "satu")
      )
      .build();

  private static final List<Number> DIGITS = ImmutableList.<Number>builder()
      .add(
          new Number(1L, "satu"),
          new Number(2L, "dua"),
          new Number(3L, "tiga"),
          new Number(4L, "empat"),
          new Number(5L, "lima"),
          new Number(6L, "enam"),
          new Number(7L, "tujuh"),
          new Number(8L, "delapan"),
          new Number(9L, "sembilan")
      )
      .build();

  private record Number(
      Long value,
      String word
  ){}

  public static String toIndonesian(Long number) {
    return getWord(number, 0);
  }

  private static String getWord(Long number, int index) {
    var num = NUMBERS.get(index);
    var currDivider = num.value;
    var word = num.word;

    if (number <= 0) {
      return "";
    }

    if (number < currDivider) {
      return getWord(number, index + 1);
    }

    var currVal = number / currDivider;
    var remain = number % currDivider;

    // current value still need to be processed even after divided
    // eg: 132.000
    if (remain > 0 && currVal >= 10) {
      return String.format("%s %s %s", getWord(currVal, index + 1), word, getWord(remain, index + 1));
    }

    // only for 11 - 19
    if (number > 10 && number < 20) {
      var digit = DIGITS.get((int) (number - 11)).word;
      return number == 11
          ? "sebelas"
          : String.format("%s %s", digit, "belas");
    }

    // only remain need to be processed
    // eg: 520
    if (remain > 0) {
      var digit = DIGITS.get((int) (currVal - 1)).word;

      // Indonesian uses 'se' in place for 'satu' for <= 1000
      return currVal == 1 && currDivider <= 1000
          ? String.format("se%s %s", word, getWord(remain, index + 1))
          : String.format("%s %s %s", digit, word, getWord(remain, index + 1));
    }

    // no remain, but not a digit
    // eg: 500, 5000, 20
    if (currDivider >= 10) {
      var digit = DIGITS.get((int) (currVal - 1)).word;

      return currVal == 1 && currDivider <= 1000
          ? String.format("se%s", word)
          : String.format("%s %s", digit, word);
    }

    // under 10 (digit)
    return DIGITS.get((int) (number - 1)).word;
  }

}
