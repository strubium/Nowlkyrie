package dev.redstudio.valkyrie.mixin;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.nio.FloatBuffer;

@SideOnly(Side.CLIENT)
@Mixin(RenderHelper.class)
public class RenderHelperMixin {

    private static final FloatBuffer ZERO_BUFFER = createColorBuffer(0f, 0f, 0f, 1f);
    private static final FloatBuffer LIGHT_COLOR_BUFFER = createColorBuffer(0.6f, 0.6f, 0.6f, 1f);
    private static final FloatBuffer LIGHT_MODEL_BUFFER = createColorBuffer(0.4f, 0.4f, 0.4f, 1f);

    private static final FloatBuffer LIGHT0_POSITION_BUFFER = createPositionBuffer(0.2D, 1.0D, -0.7D);
    private static final FloatBuffer LIGHT1_POSITION_BUFFER = createPositionBuffer(-0.2D, 1.0D, 0.7D);

    private static FloatBuffer createColorBuffer(float r, float g, float b, float a) {
        FloatBuffer buffer = net.minecraft.client.renderer.GLAllocation.createDirectFloatBuffer(4);
        buffer.put(r).put(g).put(b).put(a);
        buffer.flip();
        return buffer;
    }

    private static FloatBuffer createPositionBuffer(double x, double y, double z) {
        Vec3d vec = new Vec3d(x, y, z).normalize();
        FloatBuffer buffer = net.minecraft.client.renderer.GLAllocation.createDirectFloatBuffer(4);
        buffer.put((float) vec.x).put((float) vec.y).put((float) vec.z).put(0.0f);
        buffer.flip();
        return buffer;
    }

    /**
     * @author Strubium
     * @reason Cache all FloatBuffers to avoid allocations and improve CPU usage
     */
    @Overwrite
    public static void enableStandardItemLighting() {
        GlStateManager.enableLighting();
        GlStateManager.enableLight(0);
        GlStateManager.enableLight(1);
        GlStateManager.enableColorMaterial();
        GlStateManager.colorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);

        GlStateManager.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, LIGHT0_POSITION_BUFFER);
        GlStateManager.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, LIGHT_COLOR_BUFFER);
        GlStateManager.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, ZERO_BUFFER);
        GlStateManager.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, ZERO_BUFFER);

        GlStateManager.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, LIGHT1_POSITION_BUFFER);
        GlStateManager.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, LIGHT_COLOR_BUFFER);
        GlStateManager.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, ZERO_BUFFER);
        GlStateManager.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, ZERO_BUFFER);

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, LIGHT_MODEL_BUFFER);
    }
}
