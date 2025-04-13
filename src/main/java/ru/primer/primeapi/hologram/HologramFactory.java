package ru.primer.primeapi.hologram;

import org.bukkit.Location;

public interface HologramFactory {

  Hologram createHologram(Location location, String hologramName);

}