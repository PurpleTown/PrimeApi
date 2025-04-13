package ru.primer.primeapi.gui;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import ru.primer.primeapi.util.ItemStackUtil;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GuiItem {

  int slot;
  ItemStack item;

  public GuiItem(int slot, ItemStack item) {
    this.slot = slot;
    this.item = new ItemStackUtil().createItemStack(item, itemMeta -> {
      itemMeta.addItemFlags(ItemFlag.values());
      return itemMeta;
    });
  }

  public GuiItem(int slot, Material item) {
    this(slot, new ItemStack(item));
  }
}
