package ru.primer.primeapi.scoreboard;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.primer.primeapi.PrimeApi;

import java.util.HashMap;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScoreboardService {

  HashMap<String, Scoreboard> scoreboards = new HashMap<>();
  HashMap<UUID, String> playerScoreboards = new HashMap<>();

  public ScoreboardService() {
    load();
    startTimer();
  }

  public void showScoreboard(Player player, String scoreboardName) {
    val scoreboard = scoreboards.get(scoreboardName);
    if (scoreboard == null) return;
    scoreboards.forEach((key, tScoreboard) -> tScoreboard.removeScore(player));
    scoreboard.createScore(player);
    playerScoreboards.put(player.getUniqueId(), scoreboardName);
  }

  public void hideScoreboard(Player player, String scoreboardName) {
    val scoreboard = scoreboards.get(scoreboardName);
    if (scoreboard == null) return;
    scoreboard.removeScore(player);
    playerScoreboards.remove(player.getUniqueId());
  }

  public void hideScoreboard(Player player) {
    scoreboards.forEach((key, tScoreboard) -> tScoreboard.removeScore(player));
    playerScoreboards.remove(player.getUniqueId());
  }

  public void load() {
    PrimeApi.getYamlLang().getLangConfigurations().forEach(((lang, langConfig) -> {
      if (langConfig.getConfigurationSection("scoreboards") != null) {
        langConfig.getConfigurationSection("scoreboards").getKeys(false).forEach(key -> {
          val title = langConfig.getString("scoreboards." + key + ".title");
          val lines = langConfig.getStringList("scoreboards." + key + ".lines");
          var scoreboard = new Scoreboard();

          if (scoreboards.containsKey(key)) {
            scoreboard = scoreboards.get(key);
            scoreboard.addTitle(title, lang);
            scoreboard.addLines(lines, lang);
            return;
          }

          scoreboard.addTitle(title, lang);
          scoreboard.addLines(lines, lang);
          scoreboards.put(key, scoreboard);
        });
      }
    }));
  }

  public void reload() {
    scoreboards.clear();
    load();
    playerScoreboards.forEach(((uuid, scoreboardName) -> {
      val scoreboard = scoreboards.get(scoreboardName);
      val player = Bukkit.getPlayer(uuid);

      if(player == null) return;
      if(scoreboard == null) return;

      showScoreboard(player, scoreboardName);
    }));
  }

  private void startTimer() {
    Bukkit.getScheduler().runTaskTimerAsynchronously(PrimeApi.getInstance(), () -> {
      scoreboards.forEach((name, scoreboard) ->
              Bukkit.getOnlinePlayers().forEach(scoreboard::updateScoreboard));
    }, 0L, 20L);
  }
}
