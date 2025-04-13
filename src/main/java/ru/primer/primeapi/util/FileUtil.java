package ru.primer.primeapi.util;

import lombok.SneakyThrows;
import lombok.val;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileUtil {

  @SneakyThrows
  public static void downloadFileFromUrl(URL url, Path path) {
    val in = url.openStream();
    Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
  }
}
