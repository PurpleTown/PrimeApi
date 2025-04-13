package ru.primer.primeapi.command;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.LiteralArgument;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.primer.primeapi.PrimeApi;
import ru.primer.primeapi.event.LangReloadEvent;

import static ru.primer.primeapi.util.ColorUtil.format;

public class PrimeApiCommand {

  public PrimeApiCommand() {
    new CommandTree("primeapi")
            .withPermission("primeapi.admin")
            .then(new LiteralArgument("reloadlang")
                    .executes((sender, args) -> {
                      var lang = "ru";
                      if (sender instanceof Player player) {
                        val user = PrimeApi.getUserService().getUser(player.getName());
                        lang = user.lang();
                      }

                      var langConfig = PrimeApi.getYamlLang().getLangConfig(lang);

                      sender.sendMessage(format(langConfig.getString("messages.commands.reloadlang.start")));
                      val time = PrimeApi.getYamlLang().loadConfigs(true);
                      val YamlLang = PrimeApi.getYamlLang();
                      langConfig = YamlLang.getLangConfig(lang);
                      PrimeApi.getInstance().reloadConfig();
                      PrimeApi.getHologramService().loadHolograms();
                      PrimeApi.getNpcService().loadNpc(PrimeApi.getInstance().getConfig());
                      PrimeApi.getScoreboardService().reload();
                      Bukkit.getPluginManager().callEvent(new LangReloadEvent());
                      sender.sendMessage(format(langConfig.getString("messages.commands.reloadlang.finish")
                              .replace("{time}", String.valueOf(time))));
                    }))
            .register();
  }
}
