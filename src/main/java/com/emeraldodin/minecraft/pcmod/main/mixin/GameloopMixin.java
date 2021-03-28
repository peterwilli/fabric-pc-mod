package com.emeraldodin.minecraft.pcmod.main.mixin;

import com.emeraldodin.minecraft.pcmod.client.PCModClient;
import com.emeraldodin.minecraft.pcmod.entities.EntityItemPreview;
import com.emeraldodin.minecraft.pcmod.item.ItemList;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.emeraldodin.minecraft.pcmod.client.PCModClient.thePreviewEntity;

@Mixin(MinecraftClient.class)
public class GameloopMixin {
    @Shadow
    public ClientPlayerEntity player;

    @Shadow
    public HitResult crosshairTarget;

    @Shadow
    public ClientWorld world;

    @Shadow
    public Screen currentScreen;

    @Shadow
    private boolean paused;

    @Shadow
    private float pausedTickDelta;

    @Shadow
    private RenderTickCounter renderTickCounter;


    @Inject(at = @At("HEAD"), method = "render")
    private void render(CallbackInfo info) {
        if (player != null) {
            if (player.getActiveItem() != null) {
                for (ItemStack is : player.getItemsHand()) {
                    if (is.getItem() != null) {
                        if (ItemList.PLACABLE_ITEMS.contains(is.getItem())) {
                            if (thePreviewEntity != null) {
                                thePreviewEntity.setItem(is);
                                if (crosshairTarget != null) {
                                    Vec3d hit = crosshairTarget.getPos();
                                    thePreviewEntity.updatePosition(hit.x, hit.y, hit.z);
                                } else {
                                    break;
                                }
                            } else {
                                if (crosshairTarget != null) {
                                    Vec3d hit = crosshairTarget.getPos();
                                    thePreviewEntity = new EntityItemPreview(world, hit.x, hit.y, hit.z, is);
                                    this.world.addEntity(Integer.MAX_VALUE - 20, thePreviewEntity);
                                }
                            }
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                    return;
                }
                if (thePreviewEntity != null) {
                    thePreviewEntity.kill();
                    thePreviewEntity = null;
                }
            }
        }
    }
}
