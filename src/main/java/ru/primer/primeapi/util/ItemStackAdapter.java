package ru.primer.primeapi.util;

import com.google.gson.*;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.Base64;

public class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

  @Override
  public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(Base64.getEncoder().encodeToString(src.serializeAsBytes()));
  }

  @Override
  public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
    return ItemStack.deserializeBytes(Base64.getDecoder().decode(jsonElement.getAsString()));
  }
}