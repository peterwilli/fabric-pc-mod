package com.emeraldodin.minecraft.pcmod.main.mixin;

import com.emeraldodin.minecraft.pcmod.client.PCModClient;

import net.minecraft.client.Mouse;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
	@Shadow
	private double eventDeltaWheel;
	
	@Shadow
	private int activeButton;
	
	@Shadow
	private boolean leftButtonClicked;
	
	@Shadow
	private boolean middleButtonClicked;
	
	@Shadow
	private boolean rightButtonClicked;
	
	@Inject(at = @At("TAIL"), method = "onMouseScroll")
	private void onMouseScroll(CallbackInfo ci) {
		PCModClient.mouseDeltaScroll = (int) this.eventDeltaWheel;
	}
	
	@Inject(at = @At("TAIL"), method = "onMouseButton")
	private void onMouseButton(CallbackInfo ci) {
		PCModClient.leftMouseButton = leftButtonClicked;
		PCModClient.middleMouseButton = middleButtonClicked;
		PCModClient.rightMouseButton = rightButtonClicked;
	}
}
