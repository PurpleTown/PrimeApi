package ru.primer.primeapi.lang.impl;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.primer.primeapi.lang.LangConfigAdapter;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/*
Класс сохраняющий языковые файлы с GitHub на сервере в .yml
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class YamlLang implements LangConfigAdapter {

  @Getter
  HashMap<String, FileConfiguration> langConfigurations = new HashMap<>();
  HashMap<String, String> langPlayers = new HashMap<>();
  File dataFolder;
  String githubToken;
  String githubOrganization;
  String githubRepository;
  String githubRepositoryFolder;

  @SneakyThrows
  public Long loadConfigs(boolean wait) {
    val after = System.currentTimeMillis();
    val thread = new Thread(new Runnable() {
      @SneakyThrows
      @Override
      public void run() {
        getFiles(githubToken,
                githubOrganization, githubRepository, githubRepositoryFolder,
                dataFolder.toPath());

        val langDirectory = new File(dataFolder, "lang");
        val langFiles = new ArrayList<File>();

        if(dataFolder.listFiles() != null)
          langFiles.addAll(Arrays.stream(langDirectory.listFiles()).toList());

        for (File file : langDirectory.listFiles()) {
          System.out.println(file.getName());
        }

        langConfigurations.clear();
        for (val langFile : langFiles) {
          val fileNameSplit = langFile.getName().split("_");
          if (!fileNameSplit[0].equals("lang") || fileNameSplit.length != 2) continue;
          val lang = langFile.getName().split("_")[1].replace(".yml", "");
          langConfigurations.put(lang, YamlConfiguration.loadConfiguration(langFile));
        }
      }
    });
    thread.start();
    if (wait) thread.join();
    return System.currentTimeMillis() - after;
  }

  @Nullable
  public FileConfiguration getLangConfig() {
    return getLangConfig(null);
  }

  @Nullable
  public FileConfiguration getLangConfig(String lang) {
    var langConfig = langConfigurations.get(lang);

    if (langConfig == null) {
      langConfig = langConfigurations.get("ru");
    }

    return langConfig;
  }

  @Nullable
  public FileConfiguration getPlayerLangConfig(String name) {
    return getLangConfig(getPlayerLang(name));
  }

  public boolean containsLangConfig(String lang) {
    return langConfigurations.containsKey(lang);
  }

  public void addPlayerLang(String name, String lang) {
    langPlayers.put(name, lang);
  }

  public void removePlayerLang(String name) {
    langPlayers.remove(name);
  }

  public String getPlayerLang(String name) {
    return langPlayers.get(name);
  }

}