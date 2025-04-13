package ru.primer.primeapi.event;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LangReloadEvent extends Event {
  public static HandlerList handlers = new HandlerList();

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
