package ru.primer.primeapi.hologram.impl.protocollib;

import org.bukkit.Location;
import ru.primer.primeapi.hologram.Hologram;
import ru.primer.primeapi.hologram.HologramFactory;

public class PlibHologramFactory implements HologramFactory {
  @Override
  public Hologram createHologram(Location location, String hologramName) {
    return new PlibHologram(location, hologramName);
  }
}