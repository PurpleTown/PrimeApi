package ru.primer.primeapi.util;

import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtil {
  public static String generateString(Random random, int length) {
    val characters = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvXxYyZz0123456789";
    char[] text = new char[length];
    for (int i = 0; i < length; i++)
    {
      text[i] = characters.charAt(random.nextInt(characters.length()));
    }
    return new String(text);
  }

  public static String convertToKebabCase(String input) {
    Matcher matcher = Pattern.compile("[A-Z]{2,}(?=[A-Z][a-z]+[0-9]*|\\b)|[A-Z]?[a-z]+[0-9]*|[A-Z]|[0-9]+").matcher(input);
    List< String > matched = new ArrayList< >();
    while (matcher.find()) {
      matched.add(matcher.group(0));
    }
    return matched.stream()
            .map(String::toLowerCase)
            .collect(Collectors.joining("-"));
  }
}
