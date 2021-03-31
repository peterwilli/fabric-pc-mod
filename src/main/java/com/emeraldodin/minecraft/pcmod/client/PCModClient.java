package com.emeraldodin.minecraft.pcmod.client;

import com.emeraldodin.minecraft.pcmod.client.entities.render.FlatScreenRender;
import com.emeraldodin.minecraft.pcmod.client.entities.render.ItemPreviewRender;
import com.emeraldodin.minecraft.pcmod.client.gui.PCScreenFocus;
import com.emeraldodin.minecraft.pcmod.entities.EntityItemPreview;
import com.emeraldodin.minecraft.pcmod.entities.EntityList;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PCModClient implements ClientModInitializer {
    public static EntityItemPreview thePreviewEntity;
    public static Map<UUID, Identifier> vmScreenTextures;

    @SuppressWarnings("SpellCheckingInspection")
    public static int glfwUnfocusKey1;
    @SuppressWarnings("SpellCheckingInspection")
    public static int glfwUnfocusKey2;
    @SuppressWarnings("SpellCheckingInspection")
    public static int glfwUnfocusKey3;
    @SuppressWarnings("SpellCheckingInspection")
    public static int glfwUnfocusKey4;

    public static boolean releaseKeys = false;
    public static double mouseLastX = 0;
    public static double mouseLastY = 0;
    public static double mouseCurX = 0;
    public static double mouseCurY = 0;
    public static int mouseDeltaScroll;
    public static boolean leftMouseButton;
    public static boolean middleMouseButton;
    public static boolean rightMouseButton;
    public static boolean isOnClient = false;

    static {
        if (SystemUtils.IS_OS_MAC) {
            glfwUnfocusKey1 = GLFW.GLFW_KEY_LEFT_ALT;
            glfwUnfocusKey2 = GLFW.GLFW_KEY_RIGHT_ALT;
        } else {
            glfwUnfocusKey1 = GLFW.GLFW_KEY_LEFT_CONTROL;
            glfwUnfocusKey2 = GLFW.GLFW_KEY_RIGHT_CONTROL;
        }
        glfwUnfocusKey3 = GLFW.GLFW_KEY_BACKSPACE;
        glfwUnfocusKey4 = -1;
    }

    public static void openPCFocusGUI() {
        MinecraftClient.getInstance().openScreen(new PCScreenFocus());
    }

    public static String getKeyName(int key) {
        if (key < 0) {
            return "None";
        }else {
            return glfwKey(key);
        }
    }

    private static String glfwKey(int key) {
        switch(key) {
            case GLFW.GLFW_KEY_LEFT_CONTROL:
                return "L Control";
            case GLFW.GLFW_KEY_RIGHT_CONTROL:
                return "R Control";
            case GLFW.GLFW_KEY_RIGHT_ALT:
                return "R Alt";
            case GLFW.GLFW_KEY_LEFT_ALT:
                return "L Alt";
            case GLFW.GLFW_KEY_LEFT_SHIFT:
                return "L Shift";
            case GLFW.GLFW_KEY_RIGHT_SHIFT:
                return "R Shift";
            case GLFW.GLFW_KEY_ENTER:
                return "Enter";
            case GLFW.GLFW_KEY_BACKSPACE:
                return "Backspace";
            case GLFW.GLFW_KEY_CAPS_LOCK:
                return "Caps Lock";
            case GLFW.GLFW_KEY_TAB:
                return "Tab";
            default:
                return GLFW.glfwGetKeyName(key, 0);
        }
    }

    @Override
    public void onInitializeClient() {
        isOnClient = true;
        vmScreenTextures = new HashMap<UUID, Identifier>();

        EntityRendererRegistry.INSTANCE.register(EntityList.FLATSCREEN,
                (entityRenderDispatcher, context) -> new FlatScreenRender(entityRenderDispatcher));

        EntityRendererRegistry.INSTANCE.register(EntityList.ITEM_PREVIEW,
                (entityRenderDispatcher, context) -> new ItemPreviewRender(entityRenderDispatcher));

        new VNCReceiver("127.0.0.1", 5900).connect();
    }

}
