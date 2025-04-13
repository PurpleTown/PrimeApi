package ru.primer.primeapi.service;

import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.val;
import net.citizensnpcs.trait.HologramTrait;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.primer.primeapi.PrimeApi;
import ru.primer.primeapi.model.ProxyUser;
import ru.primer.primeapi.tcp.TCPClient;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProxyUserService {

  TCPClient tcpClient;

  @SneakyThrows
  public @Nullable ProxyUser getUser(@NotNull String name) {
    return new Gson().fromJson(tcpClient.sendTCPPacket(String.format("user:%s", name)), ProxyUser.class);
  }

  @SneakyThrows
  public @Nullable ProxyUser updateUserLang(@NotNull String name, @NotNull String lang) {
    PrimeApi.getYamlLang().addPlayerLang(name, lang);
    return new Gson().fromJson(tcpClient.sendTCPPacket(String.format("updateuserlang:%s:%s", name, lang)), ProxyUser.class);
  }

  public void updateLangEntities(Player player, ProxyUser proxyUser) {
    PrimeApi.getHologramService().getHolograms().forEach(hologram -> hologram.update(player));
    PrimeApi.getNpcService().getNpcMap().forEach(npc -> {
      val hologramTrait = npc.getOrAddTrait(HologramTrait.class);
      hologramTrait.getHologramRenderers().forEach(hologramRenderer -> {
        hologramRenderer.updateText(npc, "");
      });
    });
  }
}
