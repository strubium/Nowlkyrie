package dev.redstudio.valkyrie;

import net.jafama.FastMath;
import net.minecraft.util.math.MathHelper;

public class SinCosBenchmark {

    private static final int ITERATIONS = 100_000_000;
    private static final float STEP = 0.0001f;

    public static void main(String[] args) {
        System.out.println("Starting sin/cos benchmark...");

        // Warm-up JVM + JIT
        warmup();

        // Benchmark Math.sin / Math.cos
        benchmarkMath();

        // Benchmark FastMath.sin / FastMath.cos
        benchmarkFastMath();

        // Benchmark Minecraft's original MathHelper sin/cos (large table)
        benchmarkMinecraftSinCos();

        // Benchmark your lookup table sin/cos (smaller tables)
        benchmarkCustomLookup();

        System.out.println("Benchmark complete.");
    }

    private static void warmup() {
        System.out.println("Warming up...");
        for (int i = 0; i < 10_000_000; i++) {
            Math.sin(i * STEP);
            FastMath.sin(i * STEP);
            MathHelper.sin(i * STEP);
            MinecraftSinCos.sin(i * STEP);
            MinecraftSinCos.cos(i * STEP);
            CustomSinCos.sin(i * STEP);
            CustomSinCos.cos(i * STEP);
        }
        System.gc();
    }

    private static void benchmarkMath() {
        System.out.println("Benchmarking Math.sin and Math.cos...");
        double sum = 0;
        long start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            double val = i * STEP;
            sum += Math.sin(val);
            sum += Math.cos(val);
        }
        long duration = System.nanoTime() - start;
        System.out.printf("Math.sin/cos: %.2f ms, sum=%.5f%n", duration / 1e6, sum);
    }

    private static void benchmarkFastMath() {
        System.out.println("Benchmarking FastMath.sin and FastMath.cos...");
        double sum = 0;
        long start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            float val = i * STEP;
            sum += FastMath.sin(val);
            sum += FastMath.cos(val);
        }
        long duration = System.nanoTime() - start;
        System.out.printf("FastMath.sin/cos: %.2f ms, sum=%.5f%n", duration / 1e6, sum);
    }

    private static void benchmarkMinecraftSinCos() {
        System.out.println("Benchmarking Minecraft MathHelper.sin and MathHelper.cos (original large table)...");
        double sum = 0;
        long start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            float val = i * STEP;
            sum += MinecraftSinCos.sin(val);
            sum += MinecraftSinCos.cos(val);
        }
        long duration = System.nanoTime() - start;
        System.out.printf("Minecraft MathHelper sin/cos: %.2f ms, sum=%.5f%n", duration / 1e6, sum);
    }

    private static void benchmarkCustomLookup() {
        System.out.println("Benchmarking custom lookup table sin and cos...");
        double sum = 0;
        long start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            float val = i * STEP;
            sum += CustomSinCos.sin(val);
            sum += CustomSinCos.cos(val);
        }
        long duration = System.nanoTime() - start;
        System.out.printf("Custom lookup sin/cos: %.2f ms, sum=%.5f%n", duration / 1e6, sum);
    }

    // Minecraft's original sin/cos implementation (using large sin table)
    static class MinecraftSinCos {
        private static final int TABLE_SIZE = 65536;
        private static final float[] SIN_TABLE = new float[TABLE_SIZE];

        static {
            for (int i = 0; i < TABLE_SIZE; i++) {
                SIN_TABLE[i] = (float) Math.sin(i * Math.PI * 2 / TABLE_SIZE);
            }
        }

        public static float sin(float value) {
            // scale radians to 0..TABLE_SIZE
            return SIN_TABLE[(int)(value * 10430.378F) & 0xFFFF];
        }

        public static float cos(float value) {
            return SIN_TABLE[(int)(value * 10430.378F + 16384) & 0xFFFF];
        }
    }

    // Your custom lookup tables with 4096 entries for sin and cos
    static class CustomSinCos {
        private static final int SIN_BITS = 10;
        private static final int SIN_MASK = (1 << SIN_BITS) - 1; // 0xFFF
        private static final int SIN_COUNT = SIN_MASK + 1;
        private static final float PI2 = (float) (Math.PI * 2);
        private static final float RAD_TO_INDEX = SIN_COUNT / PI2;

        private static final float[] SIN_TABLE = new float[SIN_COUNT];
        private static final float[] COS_TABLE = new float[SIN_COUNT];

        static {
            for (int i = 0; i < SIN_COUNT; i++) {
                float angle = (i * PI2) / SIN_COUNT;
                SIN_TABLE[i] = (float) Math.sin(angle);
                COS_TABLE[i] = (float) Math.cos(angle);
            }
        }

        public static float sin(float value) {
            int index = (int) (value * RAD_TO_INDEX) & SIN_MASK;
            return SIN_TABLE[index];
        }

        public static float cos(float value) {
            int index = (int) (value * RAD_TO_INDEX) & SIN_MASK;
            return COS_TABLE[index];
        }
    }
}
