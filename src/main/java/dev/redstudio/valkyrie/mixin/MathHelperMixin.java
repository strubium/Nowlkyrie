package dev.redstudio.valkyrie.mixin;

import net.jafama.FastMath;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MathHelper.class)
public class MathHelperMixin {

	private static final int SIN_BITS = 10; // 2^10 = 1024 entries
	private static final int SIN_MASK = ~(-1 << SIN_BITS); // 0xFFF
	private static final int SIN_COUNT = SIN_MASK + 1;

	private static final float PI2 = (float) (Math.PI * 2.0);
	private static final float RAD_TO_INDEX = SIN_COUNT / PI2;

	private static final float[] SIN_TABLE = new float[SIN_COUNT];

	static {
		for (int i = 0; i < SIN_COUNT; i++) {
			float angle = (i * PI2) / SIN_COUNT;
			SIN_TABLE[i] = (float) Math.sin(angle);
		}
	}
	/**
	 * @reason Improving performance
	 * @author Desoroxxx
	 */
	@Overwrite
	public static float sin(float value) {
		int index = (int) (value * RAD_TO_INDEX) & SIN_MASK;
		return SIN_TABLE[index];
	}

	/**
	 * @reason Improving performance
	 * @author Desoroxxx
	 */
	@Overwrite
	public static float cos(float value) {
		int index = (int)(value * RAD_TO_INDEX + SIN_COUNT / 4) & SIN_MASK;
		return SIN_TABLE[index];
	}

	/**
	 * @reason Improving performance
	 * @author Desoroxxx
	 */
	@Overwrite
	public static float sqrt(float value) {
		return (float) FastMath.sqrtQuick(value);
	}

	/**
	 * @reason Improving performance
	 * @author Desoroxxx
	 */
	@Overwrite
	public static float sqrt(double value) {
		return (float) FastMath.sqrt(value);
	}

	/**
	 * @reason Improving performance
	 * @author Desoroxxx
	 */
	@Overwrite
	public static int floor(float value) {
		return (int) FastMath.floor(value);
	}

	/**
	 * @reason Improving performance
	 * @author Desoroxxx
	 */
	@Overwrite
	public static int fastFloor(double value) {
		return (int) FastMath.floor(value);
	}

	/**
	 * @reason Improving performance
	 * @author Desoroxxx
	 */
	@Overwrite
	public static int floor(double value) {
		return (int) FastMath.floor(value);
	}

	/**
	 * @reason Improving performance
	 * @author Desoroxxx
	 */
	@Overwrite
	public static int absFloor(double value) {
		return (int) FastMath.floor(Math.abs(value));
	}

	/**
	 * @reason Improving performance
	 * @author Desoroxxx
	 */
	@Overwrite
	public static int abs(int value) {
		return FastMath.abs(value);
	}

	/**
	 * @reason Improving performance
	 * @author Desoroxxx
	 */
	@Overwrite
	public static int ceil(float value) {
		return (int) FastMath.ceil(value);
	}

	/**
	 * @reason Improving performance
	 * @author Desoroxxx
	 */
	@Overwrite
	public static int ceil(double value) {
		return (int) FastMath.ceil(value);
	}
}
