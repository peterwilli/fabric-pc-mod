package com.emeraldodin.minecraft.pcmod.main.mixin;

import com.emeraldodin.minecraft.pcmod.entities.EntityFlatScreen;
import com.emeraldodin.minecraft.pcmod.entities.EntityItemPreview;
import com.emeraldodin.minecraft.pcmod.entities.EntityList;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.thread.ThreadExecutor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkMixin {
	@Shadow
	private ClientWorld world;
	
	@Shadow
	private MinecraftClient client;
	
	@Inject(at = @At("TAIL"), method = "onEntitySpawn")
	public void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo ci) {
		EntityType<?> entityType = packet.getEntityTypeId();
		double d = packet.getX();
		double e = packet.getY();
		double f = packet.getZ();
	    Object entity15 = null;
	    if (entityType == EntityList.ITEM_PREVIEW) {
	    	entity15 = new EntityItemPreview(this.world, d, e, f);
	    }else if (entityType == EntityList.FLATSCREEN) {
	    	entity15 = new EntityFlatScreen(this.world, d, e, f);
	    }
	    
	    if (entity15 != null) {
	         int i = packet.getId();
	         ((Entity)entity15).updateTrackedPosition(d, e, f);
	         ((Entity)entity15).pitch = (float)(packet.getPitch() * 360) / 256.0F;
	         ((Entity)entity15).yaw = (float)(packet.getYaw() * 360) / 256.0F;
	         ((Entity)entity15).setEntityId(i);
	         ((Entity)entity15).setUuid(packet.getUuid());
	         this.world.addEntity(i, (Entity)entity15);
	      }
	}
	
	public void onEntityPosition(EntityPositionS2CPacket packet) {
	      NetworkThreadUtils.forceMainThread(packet, this.client.getNetworkHandler(), (ThreadExecutor<?>)this.client);
	      Entity entity = this.world.getEntityById(packet.getId());
	      if (entity != null) {
	         double d = packet.getX();
	         double e = packet.getY();
	         double f = packet.getZ();
	         entity.updateTrackedPosition(d, e, f);
	         if (!entity.isLogicalSideForUpdatingMovement()) {
	            float g = (float)(packet.getYaw() * 360) / 256.0F;
	            float h = (float)(packet.getPitch() * 360) / 256.0F;
	            if (Math.abs(entity.getX() - d) < 0.03125D && Math.abs(entity.getY() - e) < 0.015625D && Math.abs(entity.getZ() - f) < 0.03125D) {
	               entity.updateTrackedPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), g, h, 3, true);
	            } else {
	               entity.updateTrackedPositionAndAngles(d, e, f, g, h, 3, true);
	            }

	            entity.setOnGround(packet.isOnGround());
	         }

	      }
	   }
}
