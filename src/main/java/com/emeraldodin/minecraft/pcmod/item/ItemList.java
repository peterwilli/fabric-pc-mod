package com.emeraldodin.minecraft.pcmod.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.List;

public class ItemList {
    public static final Item ITEM_FLATSCREEN = new Item(new Item.Settings().group(ItemGroup.DECORATIONS));
    public static final List<Item> PLACABLE_ITEMS = Arrays.asList(ITEM_FLATSCREEN);

    public static void init() {
        registerItem("flatscreen", ITEM_FLATSCREEN);
    }

    private static Item registerItem(String id, Item it) {
        Registry.register(Registry.ITEM, new Identifier("pcmod", id), it);
        return it;
    }
}
