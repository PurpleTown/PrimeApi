package ru.primer.primeapi;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.primer.primeapi.command.LangCommand;
import ru.primer.primeapi.command.PrimeApiCommand;
import ru.primer.primeapi.hologram.HologramListener;
import ru.primer.primeapi.hologram.HologramService;
import ru.primer.primeapi.hologram.impl.protocollib.PlibHologramFactory;
import ru.primer.primeapi.lang.impl.YamlLang;
import ru.primer.primeapi.listener.PlayerListener;
import ru.primer.primeapi.npc.NpcService;
import ru.primer.primeapi.placeholder.PlaceholderService;
import ru.primer.primeapi.scoreboard.ScoreboardService;
import ru.primer.primeapi.service.ProxyUserService;
import ru.primer.primeapi.tcp.TCPClient;

import java.io.File;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class PrimeApi extends JavaPlugin {

  @Getter
  static PrimeApi instance;
  @Getter
  static ProxyUserService userService;
  @Getter
  static YamlLang yamlLang;
  @Getter
  static HologramService hologramService;
  @Getter
  static NpcService npcService;
  @Getter
  static PlaceholderService placeholderService;
  @Getter
  static ScoreboardService scoreboardService;
  TCPClient tcpClient;
  FileConfiguration githubConfig;

  @Override
  public void onEnable() {
    instance = this;
    getServer().createWorld(new WorldCreator("spawn"));
    saveResource("github.yml", true);
    githubConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "github.yml"));
    yamlLang = new YamlLang(getDataFolder(), githubConfig.getString("github-token"),
            githubConfig.getString("github-organization"),
            githubConfig.getString("github-repository"),
            githubConfig.getString("github-repository-folder"));
    getLogger().info("Loading github configs...");
    getLogger().info(String.format("Loaded for %s ms.", yamlLang.loadConfigs(true)));
    placeholderService = new PlaceholderService();
    npcService = new NpcService();
    tcpClient = new TCPClient("localhost", 6666);
    userService = new ProxyUserService(tcpClient);
    hologramService = new HologramService(new PlibHologramFactory());
    scoreboardService = new ScoreboardService();

    hologramService.loadHolograms();

    getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    getServer().getPluginManager().registerEvents(new HologramListener(hologramService), this);

    getCommand("lang").setExecutor(new LangCommand());
    getCommand("lang").setTabCompleter(new LangCommand());
    new PrimeApiCommand();
  }

}
