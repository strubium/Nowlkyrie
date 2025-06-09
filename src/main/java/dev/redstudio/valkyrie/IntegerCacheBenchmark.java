package dev.redstudio.valkyrie;

import dev.redstudio.valkyrie.mixin.IntegerCacheMixin;

import java.util.Random;

public class IntegerCacheBenchmark {

    private static final int ITERATIONS = 100_000_000;
    private static final Random RAND = new Random(42);
    private static final int[] testValues = new int[ITERATIONS];

    static {
        for (int i = 0; i < ITERATIONS; i++) {
            testValues[i] = RAND.nextInt(70000); // cover all ranges
        }
    }

    // Version 1: Original JVM method
    public static Integer originalGetInteger(int value) {
        return Integer.valueOf(value);
    }

    // Version 3: Minecraft's IntegerCache (65535 cache)
    public static class MinecraftIntegerCache {
        private static final Integer[] CACHE = new Integer[65535];
        static {
            for (int i = 0; i < CACHE.length; ++i) {
                CACHE[i] = i;
            }
        }
        public static Integer getInteger(int value) {
            return value > 0 && value < CACHE.length ? CACHE[value] : value;
        }
    }

    public static void main(String[] args) {
        long start, end;

        // Benchmark 1: Integer.valueOf()
        start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            Integer val = originalGetInteger(testValues[i]);
        }
        end = System.nanoTime();
        double msOriginal = (end - start) / 1_000_000.0;
        System.out.printf("1. Java Integer.valueOf():   %.4f ms%n", msOriginal);

        // Benchmark 2: OptimizedMixin
        start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            Integer val = IntegerCacheMixin.getInteger(testValues[i]);
        }
        end = System.nanoTime();
        double msMixin = (end - start) / 1_000_000.0;
        System.out.printf("2. Optimized Mixin:   %.4f ms (%.2fx)%n", msMixin, msOriginal / msMixin);

        // Benchmark 3: Minecraft IntegerCache
        start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            Integer val = MinecraftIntegerCache.getInteger(testValues[i]);
        }
        end = System.nanoTime();
        double msMC = (end - start) / 1_000_000.0;
        System.out.printf("3. MC IntegerCache (65535):  %.4f ms (%.2fx)%n", msMC, msOriginal / msMC);
    }
}

