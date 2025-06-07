package dev.redstudio.valkyrie.renderer.cloud;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GeneratedCloudTexture {

    private static final int WIDTH = 256;
    private static final int HEIGHT = 256;

    @Getter private static DynamicTexture texture;
    private final ResourceLocation resource;

    public GeneratedCloudTexture() {
        BufferedImage image = generateCloudImage(WIDTH, HEIGHT);
        texture = new DynamicTexture(image);
        resource = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("generated_clouds", texture);
    }

    public GeneratedCloudTexture bindTexture() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(resource);
        return this;
    }

    /**
     * Generates a soft, cloud-like texture inspired by Minecraft's vanilla clouds.
     */
    private BufferedImage generateCloudImage(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        float scale = 0.05f; // lower = larger features
        float threshold = 0.5f;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float fx = x * scale;
                float fy = y * scale;

                // Smooth pseudo-random noise using a custom function
                float noise = smoothNoise(fx, fy);
                float cloud = clamp((noise - threshold) * 5f); // sharpen edge contrast

                int alpha = (int) (cloud * 255);

                int color = new Color(255, 255, 255, alpha).getRGB();
                img.setRGB(x, y, color);
            }
        }

        return img;
    }

    /**
     * A cheap "smooth" pseudo-noise function using layered sin waves.
     */
    private float smoothNoise(float x, float y) {
        float value = 0;
        value += 0.6f * (float) Math.sin(x * 1.3 + Math.cos(y * 0.7));
        value += 0.3f * (float) Math.sin(x * 3.1 + y * 2.5);
        value += 0.1f * (float) Math.sin(y * 5.1 - x * 1.7);
        return (value + 1f) / 2f; // normalize to 0–1
    }

    private float clamp(float f) {
        return Math.max(0f, Math.min(1f, f));
    }
}
