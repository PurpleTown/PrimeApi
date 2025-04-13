package ru.primer.primeapi.hologram;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HologramListener implements Listener {

  private final HologramService hologramService;

  public HologramListener(HologramService hologramService) {
    this.hologramService = hologramService;
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    hologramService.initPlayer(event.getPlayer());
  }

}
