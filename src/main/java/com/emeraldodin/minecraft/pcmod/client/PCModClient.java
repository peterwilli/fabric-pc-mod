package com.emeraldodin.minecraft.pcmod.client;

import com.emeraldodin.minecraft.pcmod.client.entities.render.FlatScreenRender;
import com.emeraldodin.minecraft.pcmod.client.entities.render.ItemPreviewRender;
import com.emeraldodin.minecraft.pcmod.entities.EntityItemPreview;
import com.emeraldodin.minecraft.pcmod.entities.EntityList;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class PCModClient implements ClientModInitializer {
    public static EntityItemPreview thePreviewEntity;

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(EntityList.FLATSCREEN,
                (entityRenderDispatcher, context) -> new FlatScreenRender(entityRenderDispatcher));

        EntityRendererRegistry.INSTANCE.register(EntityList.ITEM_PREVIEW,
                (entityRenderDispatcher, context) -> new ItemPreviewRender(entityRenderDispatcher));
    }

}
