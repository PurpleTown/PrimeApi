package ru.primer.primeapi.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface HologramLine {

  String getText(String langKey);

  void showTo(Player player);

  void update(Player player);

  void hideFrom(Player player);

  void teleport(Location location);

  void setText(String text, String langKey);
}