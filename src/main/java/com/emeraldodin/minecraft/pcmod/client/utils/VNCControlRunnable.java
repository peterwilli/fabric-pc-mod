package com.emeraldodin.minecraft.pcmod.client.utils;

import com.emeraldodin.minecraft.pcmod.client.VNCReceiver;
import com.emeraldodin.minecraft.pcmod.client.gui.PCScreenFocus;

import net.minecraft.client.MinecraftClient;

import static com.emeraldodin.minecraft.pcmod.client.PCModClient.leftMouseButton;
import static com.emeraldodin.minecraft.pcmod.client.PCModClient.middleMouseButton;
import static com.emeraldodin.minecraft.pcmod.client.PCModClient.mouseCurX;
import static com.emeraldodin.minecraft.pcmod.client.PCModClient.mouseCurY;
import static com.emeraldodin.minecraft.pcmod.client.PCModClient.mouseLastX;
import static com.emeraldodin.minecraft.pcmod.client.PCModClient.mouseLastY;
import static com.emeraldodin.minecraft.pcmod.client.PCModClient.rightMouseButton;

public class VNCControlRunnable implements Runnable {
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
                    int val = 0x00;
                    if (leftMouseButton) {
                        val += 0x01;
                    }
                    if (middleMouseButton) {
                        val += 0x04;
                    }
                    if (rightMouseButton) {
                        val += 0x02;
                    }
                    //console.getMouse().putMouseEvent((int)deltaX, (int)deltaY, mouseDeltaScroll, 0, val);
                    System.out.println("poopie");
                    VNCReceiver.current.client.moveMouse((int) mouseCurX, (int) mouseCurY);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
