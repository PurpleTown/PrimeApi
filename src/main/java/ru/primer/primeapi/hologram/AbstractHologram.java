package ru.primer.primeapi.hologram;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractHologram implements Hologram {

  private static final double LINE_SPACE = 0.25;

  private final List<HologramLine> hologramLines = new ArrayList<>();
  private final String name;
  private Location location;

  public AbstractHologram(Location location, String name) {
    this.name = name;
    this.location = location;
  }

  private Location getRelativeLocationForIndex(int index) {
    return location.clone().add(0, -LINE_SPACE * index, 0);
  }

  @Override
  public String getId() {
    return name;
  }

  @Override
  public int size() {
    return hologramLines.size();
  }

  @Override
  public void addLine(HashMap<String, String> texts) {
    int nextIndex = size();
    Location lineLocation = getRelativeLocationForIndex(nextIndex);
    HologramLine hologramLine = createLine(lineLocation);
    texts.forEach((langKey, text) -> hologramLine.setText(text, langKey));
    hologramLines.add(hologramLine);
  }

  @Override
  public void setLine(int index, String line, String langKey) {
    Preconditions.checkArgument(index < size());
    HologramLine hologramLine = hologramLines.get(index);
    hologramLine.setText(line, langKey);
  }

  @Override
  public HologramLine getLine(int index) {
    Preconditions.checkArgument(index < size());
    return hologramLines.get(index);
  }

  @Override
  public List<HologramLine> getLines() {
    return hologramLines;
  }

  @Override
  public Location getLocation() {
    return location;
  }

  @Override
  public void teleport(Location target) {
    this.location = target;
    for(int index = 0; index < size(); index++) {
      Location lineLoc = getRelativeLocationForIndex(index);
      HologramLine hologramLine = hologramLines.get(index);
      hologramLine.teleport(lineLoc);
    }
  }

  @Override
  public void showTo(Player player) {
    hologramLines.forEach(line -> line.showTo(player));
  }

  @Override
  public void update(Player player) {
    hologramLines.forEach(line -> line.update(player));
  }

  @Override
  public void hideFrom(Player player) {
    hologramLines.forEach(line -> line.hideFrom(player));
  }

  @Override
  public void removeLine(int index) {
    Preconditions.checkArgument(index < size());
    HologramLine line = hologramLines.remove(index);
    Bukkit.getOnlinePlayers().forEach(line::hideFrom);
    teleport(this.location);
  }

  protected abstract HologramLine createLine(Location location);
}