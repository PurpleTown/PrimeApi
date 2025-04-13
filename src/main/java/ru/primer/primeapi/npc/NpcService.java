package ru.primer.primeapi.npc;

import lombok.val;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import ru.primer.primeapi.PrimeApi;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NpcService {

  HashMap<String, NPC> npcMap = new HashMap<>();

  public NpcService() {
    loadNpc(PrimeApi.getInstance().getConfig());
  }

  public List<NPC> getNpcMap() {
    return npcMap.values().stream().toList();
  }

  public NPC getNpc(String name) {
    return npcMap.get(name);
  }

  public void loadNpc(FileConfiguration config) {
    CitizensAPI.getNPCRegistry().deregisterAll();
    CitizensAPI.getNPCRegistry().saveToStore();
    npcMap.clear();

    if (config.getConfigurationSection("npc") != null) {
      for (String npcName : config.getConfigurationSection("npc").getKeys(false)) {
        val npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, UUID.randomUUID().toString());
        val skinTrait = npc.getOrAddTrait(SkinTrait.class);

        npc.data().set(NPC.Metadata.SHOULD_SAVE, false);
        npc.data().set(NPC.Metadata.NAMEPLATE_VISIBLE, false);

        val world = config.getString(String.format("npc.%s.location.world", npcName));
        val x = config.getDouble(String.format("npc.%s.location.x", npcName));
        val y = config.getDouble(String.format("npc.%s.location.y", npcName));
        val z = config.getDouble(String.format("npc.%s.location.z", npcName));
        val pitch = config.getDouble(String.format("npc.%s.location.pitch", npcName));
        val yaw = config.getDouble(String.format("npc.%s.location.yaw", npcName));
        val location = new Location(Bukkit.getWorld(world), x, y, z, (float) pitch, (float) yaw);
        val skinValue = config.getString(String.format("npc.%s.skin-value", npcName));
        val skinSignature = config.getString(String.format("npc.%s.skin-signature", npcName));

        skinTrait.setTexture(skinValue, skinSignature);
        skinTrait.setSkinPersistent(npcName, skinSignature, skinTrait.getTexture());
        npc.spawn(location);
        npcMap.put(npcName, npc);
      }
    }
  }
}
