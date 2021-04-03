package com.emeraldodin.minecraft.pcmod.client.utils;

import com.emeraldodin.minecraft.pcmod.client.VNCReceiver;
import com.emeraldodin.minecraft.pcmod.client.gui.PCScreenFocus;
import com.shinyhut.vernacular.client.VernacularClient;

import net.minecraft.client.MinecraftClient;

import static com.emeraldodin.minecraft.pcmod.client.PCModClient.leftMouseButton;
import static com.emeraldodin.minecraft.pcmod.client.PCModClient.middleMouseButton;
import static com.emeraldodin.minecraft.pcmod.client.PCModClient.mouseCurX;
import static com.emeraldodin.minecraft.pcmod.client.PCModClient.mouseCurY;
import static com.emeraldodin.minecraft.pcmod.client.PCModClient.mouseLastX;
import static com.emeraldodin.minecraft.pcmod.client.PCModClient.mouseLastY;
import static com.emeraldodin.minecraft.pcmod.client.PCModClient.rightMouseButton;

public class VNCControlRunnable implements Runnable {

    private boolean lastLeftMouseButton = false;

    @Override
    public void run() {
        while (true) {
            try {
                MinecraftClient mcc = MinecraftClient.getInstance();
                double deltaX = 0;
                double deltaY = 0;

                deltaX = mouseCurX - mouseLastX;
                deltaY = mouseCurY - mouseLastY;
                mouseLastX = mouseCurX;
                mouseLastY = mouseCurY;

                if (mcc.currentScreen instanceof PCScreenFocus) {
                    if((Math.abs(deltaX) + Math.abs(deltaY)) > 2) {
                        VNCReceiver.current.client.moveMouse((int) mouseCurX, (int) mouseCurY);
                    }

                    if(leftMouseButton && lastLeftMouseButton != leftMouseButton) {
                        VNCReceiver.current.client.click(1);
                    }

                    if(lastLeftMouseButton != leftMouseButton) {
                        lastLeftMouseButton = leftMouseButton;
                    }
                }

                Thread.sleep(1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
