package ru.primer.primeapi.hologram.impl.protocollib;

import org.bukkit.Location;
import ru.primer.primeapi.hologram.AbstractHologram;
import ru.primer.primeapi.hologram.HologramLine;

public class PlibHologram extends AbstractHologram {
  public PlibHologram(Location location, String name) {
    super(location, name);
  }

  @Override
  protected HologramLine createLine(Location location) {
    return new PlibHologramLine(location);
  }
}