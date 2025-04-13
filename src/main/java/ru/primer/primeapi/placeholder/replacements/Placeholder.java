package ru.primer.primeapi.placeholder.replacements;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;

@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public abstract class Placeholder {

  String placeholder;

  public Placeholder(String placeholder) {
    this.placeholder = placeholder;
  }

  public abstract String handleReplace(Player player, String toReplace);
}
