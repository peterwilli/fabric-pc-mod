package com.emeraldodin.minecraft.pcmod.client;

import com.shinyhut.vernacular.client.VernacularClient;
import com.shinyhut.vernacular.client.VernacularConfig;
import com.shinyhut.vernacular.client.rendering.ColorDepth;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Native;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Random;

import javax.imageio.ImageIO;

public class VNCReceiver {

    private String host;
    private int port;

    private BufferedImage bufferedImage;
    private Graphics graphics;
    private NativeImage ni;
    private int width = 0, height = 0;
    private NativeImageBackedTexture lastNIBT;

    public static VNCReceiver current;

    public VernacularClient client;

    public VNCReceiver(String host, int port) {
        this.host = host;
        this.port = port;
        current = this;
    }

    public void connect() {
        MinecraftClient mcc = MinecraftClient.getInstance();
        VernacularConfig config = new VernacularConfig();
        this.client = new VernacularClient(config);

        // Select 8-bits per pixel indexed color, or 8/16/24 bits per pixel true color
        config.setColorDepth(ColorDepth.BPP_8_INDEXED);

        // Set up callbacks for the various events that can happen in a VNC session

        // Exception handler
        config.setErrorListener(Throwable::printStackTrace);

        // Password supplier - this is only invoked if the remote server requires authentication
        // config.setPasswordSupplier(() -> "my secret password");

        // Handle system bell events from the remote host
        config.setBellListener(v -> System.out.println("DING!"));

        config.setScreenUpdateListener(image -> {
            try {
                if(width == 0) {
                    width = image.getWidth(null);
                }
                if(height == 0) {
                    height = image.getHeight(null);
                }
                @SuppressWarnings("SpellCheckingInspection")
                NativeImageBackedTexture nibt = createNIBT(image);
                PCModClient.vmScreenTextures.put(mcc.player.getUuid(), mcc.getTextureManager().registerDynamicTexture("pc_screen_mp", nibt));
                if(lastNIBT != null) {
                    lastNIBT.clearGlId();
                    lastNIBT = null;
                }
                lastNIBT = nibt;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

        client.start(host, port);
    }

    private NativeImageBackedTexture createNIBT(Image image) {
        toBufferedImage(image);
        if(ni == null) {
            // We need to use ABGR otherwise we can't set a custom pixel color
            ni = new NativeImage(NativeImage.Format.ABGR, width, height, true);
        }
        final ColorModel colorModel = bufferedImage.getColorModel();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Object elements = bufferedImage.getRaster().getDataElements(x, y, (Object) null);

                int red = colorModel.getRed(elements);
                int green = colorModel.getGreen(elements);
                int blue = colorModel.getBlue(elements);
                int out = 255 << 24 | blue << 16 | green << 8 | red << 0;

                ni.setPixelColor(x, y, out);
            }
        }
        return new NativeImageBackedTexture(ni);
    }

    private void toBufferedImage(Image img) {
        // Initialize cached buffers
        if(this.bufferedImage == null) {
            this.bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
        }
        if(this.graphics == null) {
            this.graphics = this.bufferedImage.createGraphics();
        }
        this.graphics.drawImage(img, 0, 0, null);
    }

}
