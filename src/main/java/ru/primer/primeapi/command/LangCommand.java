package ru.primer.primeapi.command;

import lombok.val;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.primer.primeapi.PrimeApi;

import java.util.ArrayList;
import java.util.List;

import static ru.primer.primeapi.util.ColorUtil.format;

public class LangCommand implements TabExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
    if (!(sender instanceof Player player)) {
      return true;
    }

    var user = PrimeApi.getUserService().getUser(player.getName());
    val langConfig = PrimeApi.getYamlLang().getLangConfig(user.lang());

    if (args.length == 0) {
      player.sendMessage(format(langConfig.getString("messages.commands.lang.help")));
      return true;
    }

    val newLang = args[0];

    if (!PrimeApi.getYamlLang().containsLangConfig(newLang)) {
      player.sendMessage(format(langConfig.getString("messages.commands.lang.invalid")
              .replace("{lang}", newLang.toUpperCase())));
      return true;
    }

    user = PrimeApi.getUserService().updateUserLang(player.getName(), newLang);

    if(user == null) {
      return true;
    }

    PrimeApi.getUserService().updateLangEntities(player, user);
    PrimeApi.getScoreboardService().reload();
    val newLangConfig = PrimeApi.getYamlLang().getLangConfig(user.lang());
    player.sendMessage(format(newLangConfig.getString("messages.commands.lang.set")
            .replace("{lang}", newLang.toUpperCase())));
    return false;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
    val langs = PrimeApi.getYamlLang().getLangConfigurations().keySet().stream().toList();

    switch (args.length) {
      case 0 -> {
        return langs;
      }
      case 1 -> {
        return langs.stream().filter(lang -> lang.startsWith(args[0])).toList();
      }
    }

    return new ArrayList<>();
  }
}
