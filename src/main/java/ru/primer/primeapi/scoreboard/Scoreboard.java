package ru.primer.primeapi.scoreboard;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import ru.primer.primeapi.PrimeApi;
import ru.primer.primeapi.placeholder.PlaceholderService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static ru.primer.primeapi.util.ColorUtil.format;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Scoreboard {

  private static final HashMap<UUID, Scoreboard> scoreboards = new HashMap<>();

  public boolean hasScore(Player player) {
    return scoreboards.containsKey(player.getUniqueId());
  }

  public Scoreboard createScore(Player player) {
    scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    if (scoreboard.getObjective("sidebar") != null) {
      scoreboard.getObjective("sidebar").unregister();
    }
    sidebar = scoreboard.registerNewObjective("sidebar", "dummy");
    sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);

    val linesCount = lines.values().stream().findFirst().orElse(new ArrayList<>()).size();

    for (int i = 0; i < linesCount; i++) {
      val teamName = "SLOT_" + i;
      val team = scoreboard.registerNewTeam(teamName);
      team.addEntry(genEntry(i));
    }

    player.setScoreboard(scoreboard);
    scoreboards.put(player.getUniqueId(), this);
    updateScoreboard(player);
    return this;
  }

  public Scoreboard getByPlayer(Player player) {
    return scoreboards.get(player.getUniqueId());
  }

  public Scoreboard removeScore(Player player) {
    scoreboard = player.getScoreboard();
    if (scoreboard.getObjective("sidebar") != null) {
      scoreboard.getObjective("sidebar").unregister();
    }
    return scoreboards.remove(player.getUniqueId());
  }

  org.bukkit.scoreboard.Scoreboard scoreboard;
  Objective sidebar;
  @Setter
  @NonFinal
  HashMap<String, List<String>> lines = new HashMap<>();
  HashMap<String, String> titles = new HashMap<>();

  public void setTitle(String title) {
    sidebar.displayName(title.length() > 128 ? format(title.substring(0, 128)) : format(title));
  }

  public void addLines(List<String> lines, String lang) {
    this.lines.put(lang, lines);
  }

  public void addTitle(String title, String lang) {
    this.titles.put(lang, title);
  }

  public void setSlot(int slot, String text) {
    val team = scoreboard.getTeam("SLOT_" + slot);
    val entry = genEntry(slot);

    if (!scoreboard.getEntries().contains(entry)) {
      sidebar.getScore(entry).setScore(slot);
    }

    val prefix = text;
    team.prefix(format(prefix));
  }

  public void updateScoreboard(Player player) {
    if(!scoreboards.containsKey(player.getUniqueId())) {
      return;
    }

    val lang = PrimeApi.getYamlLang().getPlayerLang(player.getName());
    val placeholderService = PrimeApi.getPlaceholderService();
    updateLines(player, lang, placeholderService);
    updateTitle(player, lang, placeholderService);
  }

  public void updateTitle(Player player, String lang, PlaceholderService placeholderService) {
    if (titles.isEmpty()) {
      return;
    }

    var title = titles.get(lang);

    if (title == null) {
      title = "";
    }

    getByPlayer(player).setTitle(title);
  }

  public void updateLines(Player player, String lang, PlaceholderService placeholderService) {
    if (lines.isEmpty()) {
      return;
    }

    var lines = this.lines.get(lang);

    if (lines == null) {
      lines = new ArrayList<>();
    }

    for (int i = lines.size(); i > 0; i--) {
      if(!hasScore(player)) return;
      val line = lines.get(i - 1);
      getByPlayer(player).setSlot(lines.size() - i, placeholderService.handlePlaceholders(player, line));
    }
  }

  public void removeSlot(int slot) {
    val entry = genEntry(slot);
    if (scoreboard.getEntries().contains(entry)) {
      scoreboard.resetScores(entry);
    }
  }

  private String genEntry(int slot) {
    return ChatColor.values()[slot].toString();
  }

  private String getFirstSplit(String s) {
    return s.length() > 64 ? s.substring(0, 64) : s;
  }

  private String getSecondSplit(String s) {
    if (s.length() > 128) {
      s = s.substring(0, 128);
    }
    return s.length() > 64 ? s.substring(64) : "";
  }

}
