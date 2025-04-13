package ru.primer.primeapi.util;

import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;

import java.util.List;

public class ColorUtil {

  public static Component format(@NonNull String s) {
    return MiniMessage.miniMessage().deserialize(s.replace("<dark_red>", "<color:#D53032>")
                    .replace("<red>", "<color:#D53032>")
                    .replace("<gold>", "<color:#D4BF53>")
                    .replace("<yellow>", "<color:#FFFF64>")
                    .replace("<dark_green>", "<color:#007200>")
                    .replace("<green>", "<color:#64CE5A>")
                    .replace("<aqua>", "<color:#5AA9E6>")
                    .replace("<dark_aqua>", "<color:#168AAD>")
                    .replace("<dark_blue>", "<color:#0077B6>")
                    .replace("<blue>", "<color:#0096C7>")
                    .replace("<light_purple>", "<color:#FFAFCC>")
                    .replace("<dark_purple>", "<color:#7B2CBF>")
                    .replace("<white>", "<color:#FFD6FF>")
                    .replace("<gray>", "<color:#ADB5BD>")
                    .replace("<dark_gray>", "<color:#343A40>")
                    .replace("<black>", "<color:#212529>"))
            .decoration(TextDecoration.ITALIC, false);
  }

  public static List<Component> format(@NonNull List<String> list) {
    return list.stream().map(ColorUtil::format).toList();
  }

  public static String formatLegacy(@NonNull String text) {
    return ChatColor.translateAlternateColorCodes('&',
            text.replace("&4", "§x§D§5§3§0§3§2")
                    .replace("&c", "§x§D§5§3§0§3§2")
                    .replace("&6", "§x§D§4§B§F§5§3")
                    .replace("&e", "§x§F§F§F§F§6§4")
                    .replace("&2", "§x§0§0§7§2§0§0")
                    .replace("&a", "§x§6§4§C§E§5§A")
                    .replace("&b", "§x§5§A§A§9§E§6")
                    .replace("&3", "§x§1§6§8§A§A§D")
                    .replace("&1", "§x§0§0§7§7§B§6")
                    .replace("&9", "§x§0§0§9§6§C§7")
                    .replace("&d", "§x§F§F§A§F§C§C")
                    .replace("&5", "§x§7§B§2§C§B§F")
                    .replace("&f", "§x§F§F§D§6§F§F")
                    .replace("&7", "§x§A§D§B§5§B§D")
                    .replace("&8", "§x§3§4§3§A§4§0")
                    .replace("&0", "§x§2§1§2§5§2§9"));
  }

  public static List<String> formatLegacy(@NonNull List<String> list) {
    return list.stream().map(ColorUtil::formatLegacy).toList();
  }
}
