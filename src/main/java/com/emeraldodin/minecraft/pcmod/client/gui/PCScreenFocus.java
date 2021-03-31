package com.emeraldodin.minecraft.pcmod.client.gui;

import com.emeraldodin.minecraft.pcmod.client.PCModClient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Language;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PCScreenFocus extends Screen{
	private String keyString;
	private ArrayList<Integer> keys;
	private final Language lang = Language.getInstance();
	private final MinecraftClient minecraft = MinecraftClient.getInstance();
	
	public PCScreenFocus() {
		super(new TranslatableText("Focus"));
	}
	
	@Override
	protected void init() {
		PCModClient.releaseKeys = false;

		keyString = "";

		int[] unfocusKeys = new int[] {PCModClient.glfwUnfocusKey1,
										PCModClient.glfwUnfocusKey2,
										PCModClient.glfwUnfocusKey3,
										PCModClient.glfwUnfocusKey4};

		keys = new ArrayList<>();
		for(int key : unfocusKeys) {
			if(key > 0) {
				keys.add(key);
			}
		}

		boolean plus = false;
		for(int key : keys) {
			if(plus) {
				keyString += " + ";
			}
			keyString += PCModClient.getKeyName(key);
			plus = true;
		}

		Timer ServerAddress = new Timer();
		class CheckAddress extends TimerTask {
			public void run() {
				long window = minecraft.getWindow().getHandle();
				try {
					String mcc = minecraft.getCurrentServerEntry().address;
				} catch (NullPointerException address) {
					GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
					ServerAddress.cancel();
				}
			}
		}
		ServerAddress.schedule(new CheckAddress(), 0, 1000);
	}
	
	@Override
	public void render(MatrixStack ms, int wmouseX, int wmouseY, float delta) {
		long window = minecraft.getWindow().getHandle();
		DoubleBuffer mX = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer mY = BufferUtils.createDoubleBuffer(1);
		GLFW.glfwGetCursorPos(window, mX, mY);
		double mouseX = mX.get();
		double mouseY = mY.get();
		mX.clear();
		mY.clear();
		PCModClient.mouseCurX = mouseX;
		PCModClient.mouseCurY = mouseY;
		PCModClient.leftMouseButton = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
		PCModClient.middleMouseButton = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_MIDDLE) == GLFW.GLFW_PRESS;
		PCModClient.rightMouseButton = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
		
		this.textRenderer.draw(ms, lang.get("pcmod.focus.lose").replace("%s", keyString), 4, 4, -1);
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		
		boolean pressed = true;
		for(int key : keys) {
			if(GLFW.glfwGetKey(window, key) == GLFW.GLFW_RELEASE) {
				pressed = false;
				break;
			}
		}
		
		if(pressed) {
			minecraft.openScreen(null);
		}
		
		super.render(ms, wmouseX, wmouseY, delta);
	}
	
	@Override
	public void removed() {
		PCModClient.releaseKeys = true;
	}
	
	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
