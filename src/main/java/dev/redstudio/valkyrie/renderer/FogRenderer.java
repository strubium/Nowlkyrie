package dev.redstudio.valkyrie.renderer;

import dev.redstudio.valkyrie.config.ValkyrieConfig;
import lombok.var;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GLContext;

import static dev.redstudio.valkyrie.Valkyrie.MC;

public class FogRenderer {

	private static final boolean HAS_NV_FOG = GLContext.getCapabilities().GL_NV_fog_distance;

	public static void setupFog(final int startCoords, final float farPlaneDistance, final float partialTicks) {
		final Entity entity = MC.getRenderViewEntity();
		final EntityLivingBase livingEntity = entity instanceof EntityLivingBase ? (EntityLivingBase) entity : null;
		final boolean hasBlindness = livingEntity != null && livingEntity.isPotionActive(MobEffects.BLINDNESS);
		final var fogConfig = ValkyrieConfig.graphics.fog;

		if (!fogConfig.enabled && !hasBlindness) {
			GlStateManager.disableFog();
			return;
		}

		final EntityRenderer renderer = MC.entityRenderer;
		final var world = MC.world;
		final var gui = MC.ingameGUI;

		renderer.setupFogColor(false);
		GlStateManager.glNormal3f(0, -1, 0);
		GlStateManager.color(1, 1, 1, 1);

		final IBlockState blockState = ActiveRenderInfo.getBlockStateAtEntityViewpoint(world, entity, partialTicks);
		final Material material = blockState.getMaterial();

		final float densityOverride = ForgeHooksClient.getFogDensity(renderer, entity, blockState, partialTicks, 0.1F);
		if (densityOverride >= 0) {
			GlStateManager.setFogDensity(densityOverride);
			return;
		}

		if (hasBlindness) {
			applyBlindnessFog(farPlaneDistance, startCoords, livingEntity.getActivePotionEffect(MobEffects.BLINDNESS).getDuration());
		} else if (material == Material.WATER) {
			if (!fogConfig.waterFog) return;

			GlStateManager.setFog(GlStateManager.FogMode.EXP);
			float density = 0.1F;
			if (livingEntity != null) {
				if (livingEntity.isPotionActive(MobEffects.WATER_BREATHING)) {
					density = 0.01F;
				} else {
					density -= EnchantmentHelper.getRespirationModifier(livingEntity) * 0.03F;
				}
			}
			GlStateManager.setFogDensity(density);

		} else if (material == Material.LAVA) {
			if (!fogConfig.lavaFog) return;

			GlStateManager.setFog(GlStateManager.FogMode.EXP);
			GlStateManager.setFogDensity(2.0F);

		} else {
			if (!fogConfig.distanceFog) {
				GlStateManager.disableFog();
				return;
			}

			GlStateManager.setFog(GlStateManager.FogMode.LINEAR);

			if (startCoords == -1) {
				GlStateManager.setFogStart(0);
				GlStateManager.setFogEnd(farPlaneDistance);
			} else {
				GlStateManager.setFogStart(farPlaneDistance * 0.75F);
				GlStateManager.setFogEnd(farPlaneDistance);
			}

			if (HAS_NV_FOG) {
				GlStateManager.glFogi(34138, 34139);
			}

			final int posX = (int) entity.posX;
			final int posZ = (int) entity.posZ;
			if (world.provider.doesXZShowFog(posX, posZ) || gui.getBossOverlay().shouldCreateFog()) {
				GlStateManager.setFogStart(farPlaneDistance * 0.05F);
				GlStateManager.setFogEnd(Math.min(farPlaneDistance, 192.0F) * 0.5F);
			}

			ForgeHooksClient.onFogRender(renderer, entity, blockState, partialTicks, startCoords, farPlaneDistance);
		}

		GlStateManager.enableColorMaterial();
		GlStateManager.enableFog();
		GlStateManager.colorMaterial(1028, 4608);
	}

	private static void applyBlindnessFog(float farPlaneDistance, int startCoords, int duration) {
		float strength = 5.0F;
		if (duration < 20) {
			strength += (farPlaneDistance - 5.0F) * (1.0F - (float) duration / 20.0F);
		}

		GlStateManager.setFog(GlStateManager.FogMode.LINEAR);

		if (startCoords == -1) {
			GlStateManager.setFogStart(0);
			GlStateManager.setFogEnd(strength * 0.8F);
		} else {
			GlStateManager.setFogStart(strength * 0.25F);
			GlStateManager.setFogEnd(strength);
		}

		if (HAS_NV_FOG) {
			GlStateManager.glFogi(34138, 34139);
		}
	}
}
