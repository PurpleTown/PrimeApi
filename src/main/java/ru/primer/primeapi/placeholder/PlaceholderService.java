package ru.primer.primeapi.placeholder;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import ru.primer.primeapi.placeholder.replacements.Placeholder;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlaceholderService {

  List<Placeholder> placeholders = new ArrayList<>();

  public void registerPlaceholder(Placeholder placeholder) {
    placeholders.add(placeholder);
  }

  public String handlePlaceholders(Player player, String text) {
    for (Placeholder placeholder : placeholders) {
      if(text.contains(placeholder.placeholder)) {
        text = placeholder.handleReplace(player, text);
      }
    }
    return text;
  }
}
