package ru.primer.primeapi.util;

import lombok.val;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class TimeUtil {

  public static int getTimeByString(String text) {
    var time = -2;
    val secondsPattern = Pattern.compile("\\d*s");
    val minutesPattern = Pattern.compile("\\d*m");
    val hoursPattern = Pattern.compile("\\d*h");
    val daysPattern = Pattern.compile("\\d*d");

    try {
      if (secondsPattern.matcher(text).find()) {
        time = Integer.parseInt(text.replace("s", ""));
      } else if (minutesPattern.matcher(text).find()) {
        time = Integer.parseInt(text.replace("m", "")) * 60;
      } else if (hoursPattern.matcher(text).find()) {
        time = Integer.parseInt(text.replace("h", "")) * 3600;
      } else if (daysPattern.matcher(text).find()) {
        time = Integer.parseInt(text.replace("d", "")) * 86400;
      }
    } catch (NumberFormatException exc) {
      return time;
    }

    return time;
  }

  public static String formatTimeToString(int timeInSeconds, FileConfiguration langConfig) {
    var minutes = 0;
    var hours = 0;
    var days = 0;
    val words = new ArrayList<String>();

    if(timeInSeconds / 86400 >= 1) {
      days = (int) Math.floor(timeInSeconds / 86400D);
      timeInSeconds -= days * 86400;
      words.add(days + langConfig.getString("global.days"));
    }

    if(timeInSeconds / 3600 >= 1) {
      hours = (int) Math.floor(timeInSeconds / 3600D);
      timeInSeconds -= hours * 3600;
      words.add(hours + langConfig.getString("global.hours"));
    }

    if(timeInSeconds / 60 >= 1) {
      minutes = (int) Math.floor(timeInSeconds / 60D);
      timeInSeconds -= minutes * 60;
      words.add(minutes + langConfig.getString("global.minutes"));
    }

    if(timeInSeconds > 0) {
      words.add(timeInSeconds + langConfig.getString("global.seconds"));
    }

    if(words.isEmpty()) {
      return 0 + langConfig.getString("global.seconds");
    }

    return String.join(" ", words);
  }

}