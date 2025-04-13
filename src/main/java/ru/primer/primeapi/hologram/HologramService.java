package ru.primer.primeapi.hologram;

import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.primer.primeapi.PrimeApi;
import ru.primer.primeapi.lang.impl.YamlLang;

import java.util.*;

public class HologramService {

  private final Map<String, Hologram> hologramMap = new HashMap<>();
  private final HologramFactory hologramFactory;

  public HologramService(HologramFactory hologramFactory) {
    this.hologramFactory = hologramFactory;
  }

  public Hologram getHologram(String hologramID) {
    return hologramMap.get(hologramID);
  }

  public void loadNpcHolograms(FileConfiguration config, YamlLang lang) {
    if (config.getConfigurationSection("npc") != null) {
      for (String npcName : config.getConfigurationSection("npc").getKeys(false)) {
        val world = config.getString(String.format("npc.%s.location.world", npcName));
        val x = config.getDouble(String.format("npc.%s.location.x", npcName));
        val y = config.getDouble(String.format("npc.%s.location.y", npcName));
        val z = config.getDouble(String.format("npc.%s.location.z", npcName));
        val pitch = config.getDouble(String.format("npc.%s.location.pitch", npcName));
        val yaw = config.getDouble(String.format("npc.%s.location.yaw", npcName));
        val location = new Location(Bukkit.getWorld(world), x, y + 1.95, z, (float) pitch, (float) yaw);
        val hologram = createHologram(location, String.format("npc_%s", npcName));

        HashMap<Integer, HashMap<String, String>> liness = new HashMap<>();

        lang.getLangConfigurations().forEach((langKey, langConfig) -> {
          val lines = langConfig.getStringList(String.format("npc.%s", npcName));
          for (int i = 0; i < lines.size(); i++) {
            val line = lines.get(i);
            var tLines = liness.get(i);

            if (tLines == null) {
              tLines = new HashMap<>();
            }

            tLines.put(langKey, line);
            liness.put(i, tLines);
          }
        });

        liness.forEach((index, lines) -> hologram.addLine(lines));
      }
    }
  }

  public void loadHolograms() {
    clearHolograms();
    val config = PrimeApi.getInstance().getConfig();
    val lang = PrimeApi.getYamlLang();

    if (config.getConfigurationSection("holograms") != null) {
      for (String key : config.getConfigurationSection("holograms").getKeys(false)) {
        val hologram = createHologram(key);
        HashMap<Integer, HashMap<String, String>> liness = new HashMap<>();

        lang.getLangConfigurations().forEach((langKey, langConfig) -> {
          val lines = langConfig.getStringList(String.format("holograms.%s", key));
          for (int i = 0; i < lines.size(); i++) {
            val line = lines.get(i);
            var tLines = liness.get(i);

            if (tLines == null) {
              tLines = new HashMap<>();
            }

            tLines.put(langKey, line);
            liness.put(i, tLines);
          }
        });

        liness.forEach((index, lines) -> hologram.addLine(lines));
      }
    }

    loadNpcHolograms(config, lang);
    Bukkit.getOnlinePlayers().forEach(this::initPlayer);
  }

  public Hologram createHologram(String name) {
    val config = PrimeApi.getInstance().getConfig();
    val world = config.getString(String.format("holograms.%s.location.world", name));
    val x = config.getDouble(String.format("holograms.%s.location.x", name));
    val y = config.getDouble(String.format("holograms.%s.location.y", name));
    val z = config.getDouble(String.format("holograms.%s.location.z", name));
    val location = new Location(Bukkit.getWorld(world), x, y, z);
    return createHologram(location, name);
  }

  public Hologram createHologram(Location location, String name) {
    if (hologramMap.containsKey(name)) {
      return null;
    }

    Hologram hologram = hologramFactory.createHologram(location, name);
    hologramMap.put(name, hologram);
    return hologram;
  }

  public void deleteHologram(String hologramID) {
    Hologram hologram = hologramMap.remove(hologramID);
    if (hologram == null) {
      return;
    }
    Bukkit.getOnlinePlayers().forEach(hologram::hideFrom);
  }

  public void clearHolograms() {
    new ArrayList<>(hologramMap.keySet()).forEach(this::deleteHologram);
  }

  public void initPlayer(Player player) {
    hologramMap.values().forEach(hologram -> hologram.showTo(player));
  }

  public List<String> getHologramNames() {
    return List.copyOf(hologramMap.keySet());
  }

  public List<Hologram> getHolograms() {
    return List.copyOf(hologramMap.values());
  }

}