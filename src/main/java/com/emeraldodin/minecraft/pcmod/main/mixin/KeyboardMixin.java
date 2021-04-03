package com.emeraldodin.minecraft.pcmod.main.mixin;

import com.emeraldodin.minecraft.pcmod.client.VNCReceiver;
import com.emeraldodin.minecraft.pcmod.client.gui.PCScreenFocus;
import com.shinyhut.vernacular.utils.KeySyms;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.event.KeyEvent;
import java.security.Key;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    private static final Map<Character, Integer> CONTROL_CHARACTERS = new ConcurrentHashMap<>();
    static {
        CONTROL_CHARACTERS.put((char) 0x00, 0x0040);
        CONTROL_CHARACTERS.put((char) 0x01, 0x0061);
        CONTROL_CHARACTERS.put((char) 0x02, 0x0062);
        CONTROL_CHARACTERS.put((char) 0x03, 0x0063);
        CONTROL_CHARACTERS.put((char) 0x04, 0x0064);
        CONTROL_CHARACTERS.put((char) 0x05, 0x0065);
        CONTROL_CHARACTERS.put((char) 0x06, 0x0066);
        CONTROL_CHARACTERS.put((char) 0x07, 0x0067);
        CONTROL_CHARACTERS.put((char) 0x08, 0x0068);
        CONTROL_CHARACTERS.put((char) 0x09, 0x0069);
        CONTROL_CHARACTERS.put((char) 0x0a, 0x006a);
        CONTROL_CHARACTERS.put((char) 0x0b, 0x006b);
        CONTROL_CHARACTERS.put((char) 0x0c, 0x006c);
        CONTROL_CHARACTERS.put((char) 0x0d, 0x006d);
        CONTROL_CHARACTERS.put((char) 0x0e, 0x006e);
        CONTROL_CHARACTERS.put((char) 0x0f, 0x006f);
        CONTROL_CHARACTERS.put((char) 0x10, 0x0070);
        CONTROL_CHARACTERS.put((char) 0x11, 0x0071);
        CONTROL_CHARACTERS.put((char) 0x12, 0x0072);
        CONTROL_CHARACTERS.put((char) 0x13, 0x0073);
        CONTROL_CHARACTERS.put((char) 0x14, 0x0074);
        CONTROL_CHARACTERS.put((char) 0x15, 0x0075);
        CONTROL_CHARACTERS.put((char) 0x16, 0x0076);
        CONTROL_CHARACTERS.put((char) 0x17, 0x0077);
        CONTROL_CHARACTERS.put((char) 0x18, 0x0078);
        CONTROL_CHARACTERS.put((char) 0x19, 0x0079);
        CONTROL_CHARACTERS.put((char) 0x1a, 0x007a);
        CONTROL_CHARACTERS.put((char) 0x1b, 0x005b);
        CONTROL_CHARACTERS.put((char) 0x1c, 0x005c);
        CONTROL_CHARACTERS.put((char) 0x1d, 0x005d);
        CONTROL_CHARACTERS.put((char) 0x1e, 0x005e);
        CONTROL_CHARACTERS.put((char) 0x1f, 0x005f);
    }

    private void keyCheckAndPress(int key, int GLFW_KEY, int vk, boolean pressed) {
        try {
            if(key == GLFW_KEY) {
                int keySymCode = 0;
                if(isCtrlPressed) {
                    keySymCode = CONTROL_CHARACTERS.getOrDefault(vk, 0);
                }
                else {
                    keySymCode = KeySyms.forKeyCode(vk).get();
                }
                VNCReceiver.current.client.updateKey(keySymCode, pressed);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isCtrlPressed = false;

    @Inject(at = @At("HEAD"), method = "onKey")
    public void onKey(long window, int key, int scancode, int action, int j, CallbackInfo ci) {
        MinecraftClient mcc = MinecraftClient.getInstance();
        if (window == mcc.getWindow().getHandle()) {
            if (mcc.currentScreen instanceof PCScreenFocus) {
                if (action != 2) {
                    boolean pressed = action == 1;
                    if(key == GLFW.GLFW_KEY_LEFT_CONTROL || key == GLFW.GLFW_KEY_RIGHT_CONTROL) {
                        isCtrlPressed = pressed;
                    }
                    keyCheckAndPress(key, GLFW.GLFW_KEY_BACKSPACE, KeyEvent.VK_BACK_SPACE, pressed);
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "onChar")
    public void onChar(long window, int key, int i, CallbackInfo ci) {
        MinecraftClient mcc = MinecraftClient.getInstance();
        if (window == mcc.getWindow().getHandle()) {
            if (mcc.currentScreen instanceof PCScreenFocus) {
                try {
                    System.out.println("onChar: " + key);
                    VNCReceiver.current.client.type(key);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
