package com.emeraldodin.minecraft.pcmod.client;

import com.emeraldodin.minecraft.pcmod.client.entities.render.FlatScreenRender;
import com.emeraldodin.minecraft.pcmod.entities.EntityList;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class PCModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(EntityList.FLATSCREEN,
                (entityRenderDispatcher, context) -> new FlatScreenRender(entityRenderDispatcher));
    }

}
