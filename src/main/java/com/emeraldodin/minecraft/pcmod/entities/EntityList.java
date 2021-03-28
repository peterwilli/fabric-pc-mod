package com.emeraldodin.minecraft.pcmod.entities;

import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntityList {
	public static EntityType<Entity> ITEM_PREVIEW;
	public static EntityType<Entity> FLATSCREEN;

	public static void init() {
		FLATSCREEN = Registry.register(Registry.ENTITY_TYPE,
				new Identifier("pcmod", "flat_screen"),
				FabricEntityTypeBuilder.create(SpawnGroup.MISC, EntityFlatScreen::new)
						.size(new EntityDimensions(0.8f, 0.8f, true)).trackable(60, 2, true).build());
		ITEM_PREVIEW = Registry.register(Registry.ENTITY_TYPE,
				new Identifier("pcmod", "item_preview"),
				FabricEntityTypeBuilder.create(SpawnGroup.MISC, EntityItemPreview::new)
						.size(new EntityDimensions(1,1, true)).trackable(60, 2,true).build());
	}
}