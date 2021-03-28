package com.emeraldodin.minecraft.pcmod.client;

import com.emeraldodin.minecraft.pcmod.client.entities.render.FlatScreenRender;
import com.emeraldodin.minecraft.pcmod.client.entities.render.ItemPreviewRender;
import com.emeraldodin.minecraft.pcmod.entities.EntityItemPreview;
import com.emeraldodin.minecraft.pcmod.entities.EntityList;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PCModClient implements ClientModInitializer {
    public static EntityItemPreview thePreviewEntity;
    public static Map<UUID, Identifier> vmScreenTextures;

    @Override
    public void onInitializeClient() {
        vmScreenTextures = new HashMap<UUID, Identifier>();

        EntityRendererRegistry.INSTANCE.register(EntityList.FLATSCREEN,
                (entityRenderDispatcher, context) -> new FlatScreenRender(entityRenderDispatcher));

        EntityRendererRegistry.INSTANCE.register(EntityList.ITEM_PREVIEW,
                (entityRenderDispatcher, context) -> new ItemPreviewRender(entityRenderDispatcher));

        new VNCReceiver("127.0.0.1", 5900).connect();
    }

}
