package ru.primer.primeapi.hologram.impl.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import ru.primer.primeapi.PrimeApi;
import ru.primer.primeapi.hologram.HologramLine;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static ru.primer.primeapi.util.ColorUtil.formatLegacy;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlibHologramLine implements HologramLine {

  UUID entityUid;
  int entityId;
  @NonFinal
  Location location;
  HashMap<String, String> texts = new HashMap<>();

  public PlibHologramLine(Location location) {
    this.location = location;
    this.entityId = ThreadLocalRandom.current().nextInt();
    this.entityUid = UUID.randomUUID();
  }

  @Override
  public String getText(String langKey) {
    return texts.get(langKey);
  }

  @Override
  @SneakyThrows
  public void showTo(Player player) {
    val langKey = PrimeApi.getYamlLang().getPlayerLang(player.getName());
    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    protocolManager.sendServerPacket(player, createAddPacket());
    protocolManager.sendServerPacket(player, createDataPacket(langKey));
  }

  @Override
  public void update(Player player) {
    val langKey = PrimeApi.getYamlLang().getPlayerLang(player.getName());
    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    protocolManager.sendServerPacket(player, createDataPacket(langKey));
  }

  @Override
  @SneakyThrows
  public void hideFrom(Player player) {
    ProtocolLibrary.getProtocolManager().sendServerPacket(player, createRemovePacket());
  }

  @Override
  public void teleport(Location location) {
    this.location = location;
    ProtocolLibrary.getProtocolManager().broadcastServerPacket(createMovePacket());
  }

  @Override
  public void setText(String text, String langKey) {
    texts.put(langKey, text);
    ProtocolLibrary.getProtocolManager().broadcastServerPacket(createDataPacket(langKey));
  }

  private PacketContainer createAddPacket() {
    PacketType type = PacketType.Play.Server.SPAWN_ENTITY;
    PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(type);

    StructureModifier<Integer> intMod = packet.getIntegers();
    StructureModifier<EntityType> typeMod = packet.getEntityTypeModifier();
    StructureModifier<UUID> uuidMod = packet.getUUIDs();
    StructureModifier<Double> doubleMod = packet.getDoubles();

    // Write id of entity
    intMod.write(0, this.entityId);

    // Write type of entity
    typeMod.write(0, EntityType.ARMOR_STAND);

    // Write entities UUID
    uuidMod.write(0, this.entityUid);

    // Write position
    doubleMod.write(0, location.getX());
    doubleMod.write(1, location.getY());
    doubleMod.write(2, location.getZ());

    return packet;
  }

  private PacketContainer createDataPacket(String langKey) {
    PacketType type = PacketType.Play.Server.ENTITY_METADATA;
    PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(type);

    packet.getIntegers().write(0, this.entityId);

    WrappedDataWatcher.Serializer byteSerializer = WrappedDataWatcher.Registry.get(Byte.class);
    WrappedDataWatcher.Serializer chatSerializer = WrappedDataWatcher.Registry.getChatComponentSerializer(true);
    WrappedDataWatcher.Serializer boolSerializer = WrappedDataWatcher.Registry.get(Boolean.class);

    List<WrappedDataValue> dataValues = new ArrayList<>();

    Byte flags = 0x20;
    dataValues.add(new WrappedDataValue(0, byteSerializer, flags));

    val text = formatLegacy(texts.get(langKey));

    Optional<?> optChat = Optional.of(WrappedChatComponent.fromChatMessage(text)[0].getHandle());
    dataValues.add(new WrappedDataValue(2, chatSerializer, optChat));

    Boolean nameVisible = !text.equals("");
    dataValues.add(new WrappedDataValue(3, boolSerializer, nameVisible));

    Byte armorStandTypeFlags = 0x10;
    dataValues.add(new WrappedDataValue(15, byteSerializer, armorStandTypeFlags));

    packet.getDataValueCollectionModifier().write(0, dataValues);

    return packet;
  }

  private PacketContainer createMovePacket() {
    PacketType type = PacketType.Play.Server.ENTITY_TELEPORT;
    PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(type);

    packet.getIntegers().write(0, entityId);

    StructureModifier<Double> doubleMod = packet.getDoubles();
    doubleMod.write(0, this.location.getX());
    doubleMod.write(1, this.location.getY());
    doubleMod.write(2, this.location.getZ());

    return packet;
  }

  private PacketContainer createRemovePacket() {
    PacketType type = PacketType.Play.Server.ENTITY_DESTROY;
    PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(type);

    packet.getIntLists().write(0, IntList.of(this.entityId));

    return packet;
  }

}