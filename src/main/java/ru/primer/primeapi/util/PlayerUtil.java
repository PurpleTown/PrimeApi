package ru.primer.primeapi.util;

import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerUtil {

  public static void removeItems(Player player, ItemStack itemStack, int amount) {
    val inventory = player.getInventory();

    for (int i = 0; i < inventory.getSize(); i++) {
      if (amount <= 0) {
        return;
      }

      val item = inventory.getItem(i);

      if (item == null) {
        continue;
      }

      if (!item.isSimilar(itemStack)) {
        continue;
      }

      var itemAmount = item.getAmount() - amount;

      item.setAmount(itemAmount);
      amount = itemAmount < 0 ? Math.abs(itemAmount) : 0;
    }
  }

  public static int countItems(Player player, ItemStack itemStack) {
    val inventory = player.getInventory();
    var amount = 0;

    for (int i = 0; i < inventory.getSize(); i++) {
      val item = inventory.getItem(i);

      if (item == null) {
        continue;
      }

      if (!item.isSimilar(itemStack)) {
        continue;
      }

      amount += item.getAmount();
    }

    return amount;
  }

  public static boolean canGiveItem(Player player, ItemStack itemStack) {
    val inventory = player.getInventory();
    var amount = itemStack.getAmount();

    for (int i = 0; i < inventory.getSize(); i++) {
      val item = inventory.getItem(i);

      if (item == null) {
        amount -= itemStack.getMaxStackSize();
        continue;
      };
      if (!item.isSimilar(itemStack)) continue;

      amount -= 64 - item.getAmount();
    }

    return amount <= 0;
  }
}
