package ru.primer.primeapi.gui;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.primer.primeapi.PrimeApi;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class Gui implements Listener, InventoryHolder {

  @NonFinal
  Inventory inventory = null;
  HashMap<Integer, GuiItem> items = new HashMap<>();
  boolean clickPlayerInventory;
  boolean update;
  long updatePeriod;
  Class<? extends Gui> clazz;

  public Gui(Class<? extends Gui> clazz) {
    this(false, false, 0, clazz);
  }

  public Gui(boolean clickPlayerInventory, Class<? extends Gui> clazz) {
    this(clickPlayerInventory, false, 0, clazz);
  }

  public Gui(boolean update, long updatePeriod, Class<? extends Gui> clazz) {
    this(false, update, updatePeriod, clazz);
  }

  public Gui(boolean clickPlayerInventory, boolean update, long updatePeriod, Class<? extends Gui> clazz) {
    this.clickPlayerInventory = clickPlayerInventory;
    this.update = update;
    this.updatePeriod = updatePeriod;
    this.clazz = clazz;

    PrimeApi.getInstance().getServer().getPluginManager().registerEvents(this, PrimeApi.getInstance());
  }

  public void open(Player player, Object... objects) {
    val user = PrimeApi.getUserService().getUser(player.getName());
    val langConfig = PrimeApi.getYamlLang().getLangConfig(user.lang());
    inventory = initializeInventory(player, langConfig, objects);

    if (inventory == null) {
      return;
    }

    updateInventory(player, objects, langConfig);
    player.openInventory(inventory);
    guis.put(player.getUniqueId(), this);

    if (update) {
      Bukkit.getScheduler().runTaskTimer(PrimeApi.getInstance(), task -> {
        if (!player.isOnline()) {
          task.cancel();
          return;
        }

        val topInventory = player.getOpenInventory().getTopInventory();

        if (!topInventory.getHolder().getClass().equals(clazz)) {
          task.cancel();
          return;
        }

        updateInventory(player, objects, langConfig);
      }, updatePeriod, updatePeriod);
    }
  }

  public GuiItem getItem(int slot) {
    return items.get(slot);
  }

  public void addItem(GuiItem item) {
    items.put(item.getSlot(), item);
  }

  private void updateInventory(Player player, Object[] objects, FileConfiguration langConfig) {
    initializeItems(player, langConfig, objects);

    items.forEach((slot, guiItem) -> {
      inventory.setItem(slot, guiItem.getItem());
    });
  }

  public void fillItems(ItemStack item) {
    fillItems(item, slot -> !items.containsKey(slot));
  }

  public void fillItems(ItemStack item, Function<Integer, Boolean> expression) {
    for (int slot = 0; slot < inventory.getSize(); slot++) {
      if (expression.apply(slot)) {
        items.put(slot, new GuiItem(slot, item));
      }
    }
  }

  abstract public Inventory initializeInventory(Player player, FileConfiguration langConfig, Object[] objects);

  abstract public void initializeItems(Player player, FileConfiguration langConfig, Object[] objects);

  @Override
  public @NotNull Inventory getInventory() {
    return inventory;
  }

  static Cache<UUID, Long> clickCooldown = CacheBuilder.newBuilder()
          .expireAfterWrite(50, TimeUnit.MILLISECONDS).build();
  static HashMap<UUID, Gui> guis = new HashMap<>();

  @EventHandler
  public void onClose(InventoryCloseEvent event) {
    guis.remove(event.getPlayer().getUniqueId());
  }

  @EventHandler
  public void onClick(InventoryClickEvent event) {
    val player = (Player) event.getWhoClicked();
    val clickedInventory = event.getClickedInventory();
    val topInventory = player.getOpenInventory().getTopInventory();

    if (clickedInventory == null) return;
    if (topInventory.getHolder() == null) return;
    if (!topInventory.getHolder().getClass().equals(clazz)) return;

    event.setCancelled(true);

    if(clickCooldown.getIfPresent(player.getUniqueId()) != null) {
      return;
    }

    clickCooldown.put(player.getUniqueId(), System.currentTimeMillis());

    if (!clickPlayerInventory && !clickedInventory.getHolder().getClass().equals(clazz))
      return;

    val slot = event.getSlot();
    val gui = guis.get(player.getUniqueId());

    gui.items.values().stream().filter(guiItem -> guiItem instanceof ClickableGuiItem)
            .filter(guiItem -> guiItem.getSlot() == slot)
            .map(guiItem -> (ClickableGuiItem) guiItem)
            .forEach(guiItem -> guiItem.getOnClick().accept(event));
  }
}
