package ru.primer.primeapi.util;

import lombok.val;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Function;

public class ItemStackUtil {

  public ItemStack createItemStack(ItemStack itemStack, Function<ItemMeta, ItemMeta> itemMeta) {
    itemStack.setItemMeta(itemMeta.apply(itemStack.getItemMeta()));

    return itemStack;
  }

  public ItemStack createItemStack(Material material, Function<ItemMeta, ItemMeta> itemMeta) {
    return createItemStack(new ItemStack(material), itemMeta);
  }

  public String getTranslateItemStack(ItemStack itemStack) {
    if(itemStack == null) {
      return "";
    }

    val itemMeta = itemStack.getItemMeta();

    if(itemMeta == null) {
      return "";
    }

    return itemMeta.getDisplayName().isEmpty() ?
            String.format("<lang:%s>", itemStack.getType().getItemTranslationKey())
            : itemMeta.getDisplayName();
  }
}