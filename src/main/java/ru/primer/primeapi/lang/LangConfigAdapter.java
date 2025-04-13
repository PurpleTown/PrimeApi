package ru.primer.primeapi.lang;

import lombok.SneakyThrows;
import lombok.val;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHubBuilder;
import ru.primer.primeapi.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/*
Интерфейс для создания классов которые сохраняют конфигурационые файлы с GitHub
 */
public interface LangConfigAdapter {

  Long loadConfigs(boolean wait);

  default void getFiles(String token, String organizationName, String repositoryName,
                        String filePath, Path dataDirectory) throws IOException {
    val files = new ArrayList<File>();
    val github = new GitHubBuilder()
            .withOAuthToken(token, organizationName)
            .build();
    val organization = github.getOrganization(organizationName);
    val repository = organization.getRepository(repositoryName);
    val directory = new File(dataDirectory.toAbsolutePath().toString());

    directory.delete();
    directory.mkdir();

    addFiles(repository, directory, files, filePath);
  }

  @SneakyThrows
  private void addFiles(GHRepository repository, File directory, List<File> files, String path) {
    val contents = repository.getDirectoryContent(path);
    for (GHContent content : contents) {
      if (content.isDirectory()) {
        val newDirectory = new File(directory,
                content.getPath().replace(directory.getPath(), "")
                        .replace(path, ""));
        newDirectory.mkdir();
        addFiles(repository, newDirectory, files, content.getPath());
        return;
      }
      files.add(writeStringToFile(directory, content.getName(), new URL(content.getDownloadUrl())));
    }
  }

  @SneakyThrows
  default File writeStringToFile(File dir, String fileName, URL url) {
    val filePath = Path.of(dir.getAbsolutePath(), fileName);
    FileUtil.downloadFileFromUrl(url, filePath);
    return new File(filePath.toString());
  }

}
