package ru.primer.primeapi.listener;

import lombok.val;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;
import ru.primer.primeapi.PrimeApi;

public class PlayerListener implements Listener {

  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    val player = e.getPlayer();
    val user = PrimeApi.getUserService().getUser(player.getName());
    val pdc = player.getPersistentDataContainer();
    val namespace = new NamespacedKey(PrimeApi.getInstance(), "scoreboard");

    if(!pdc.has(namespace, PersistentDataType.BOOLEAN)) {
      pdc.set(namespace, PersistentDataType.BOOLEAN, true);
    }

    PrimeApi.getYamlLang().addPlayerLang(player.getName(), user.lang());
    PrimeApi.getUserService().updateLangEntities(player, user);
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent e) {
    val player = e.getPlayer();
    PrimeApi.getYamlLang().removePlayerLang(player.getName());
  }
}
