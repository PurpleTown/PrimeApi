package ru.primer.primeapi.gui;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClickableGuiItem extends GuiItem {

  Consumer<InventoryClickEvent> onClick;

  public ClickableGuiItem(int slot, ItemStack item, Consumer<InventoryClickEvent> onClick) {
    super(slot, item);
    this.onClick = onClick;
  }

  public ClickableGuiItem(int slot, Material item, Consumer<InventoryClickEvent> onClick) {
    super(slot, item);
    this.onClick = onClick;
  }
}
