package ru.primer.primeapi.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public interface Hologram {

  String getId();

  int size();

  void addLine(HashMap<String, String> texts);

  void setLine(int index, String line, String langKey);

  HologramLine getLine(int index);

  List<HologramLine> getLines();

  Location getLocation();

  void teleport(Location target);

  void showTo(Player player);

  void update(Player player);

  void hideFrom(Player player);

  void removeLine(int index);
}