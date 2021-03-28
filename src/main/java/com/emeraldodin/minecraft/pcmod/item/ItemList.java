package com.emeraldodin.minecraft.pcmod.item;

import com.emeraldodin.minecraft.pcmod.entities.EntityFlatScreen;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.List;

public class ItemList {
    public static final OrderableItem ITEM_FLATSCREEN = new PlacableOrderableItem(new Item.Settings().group(ItemGroup.TOOLS), EntityFlatScreen.class, SoundEvents.BLOCK_METAL_PLACE, 0);
    public static final List<Item> PLACABLE_ITEMS = Arrays.asList(ITEM_FLATSCREEN);

    public static void init() {
        registerItem("flatscreen", ITEM_FLATSCREEN);
    }

    private static Item registerItem(String id, Item it) {
        Registry.register(Registry.ITEM, new Identifier("pcmod", id), it);
        return it;
    }
}
