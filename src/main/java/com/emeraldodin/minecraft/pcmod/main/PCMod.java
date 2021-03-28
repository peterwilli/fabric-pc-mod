package com.emeraldodin.minecraft.pcmod.main;

import com.emeraldodin.minecraft.pcmod.entities.EntityList;
import com.emeraldodin.minecraft.pcmod.item.ItemList;

import net.fabricmc.api.ModInitializer;

public class PCMod implements ModInitializer {

	@Override
	public void onInitialize() {
		ItemList.init();
		EntityList.init();
	}
}
