package dev.redstudio.valkyrie;

import net.jafama.FastMath;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.Random;

public class AxisAlignedBBBenchmark {

    private static final int ITERATIONS = 10_000_000;
    private static final Random RAND = new Random(12345);

    public static void main(String[] args) {
        AxisAlignedBB[] boxesA = new AxisAlignedBB[1000];
        AxisAlignedBB[] boxesB = new AxisAlignedBB[1000];

        // Initialize random boxes
        for (int i = 0; i < 1000; i++) {
            boxesA[i] = randomBox();
            boxesB[i] = randomBox();
        }

        // Warmup JVM
        for (int i = 0; i < 10000; i++) {
            intersectMath(boxesA[i % 1000], boxesB[i % 1000]);
            intersectFastMath(boxesA[i % 1000], boxesB[i % 1000]);
            unionMath(boxesA[i % 1000], boxesB[i % 1000]);
            unionFastMath(boxesA[i % 1000], boxesB[i % 1000]);
        }

        // Benchmark Math.min/max
        long startMath = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            intersectMath(boxesA[i % 1000], boxesB[i % 1000]);
            unionMath(boxesA[i % 1000], boxesB[i % 1000]);
        }
        long endMath = System.nanoTime();

        // Benchmark FastMath.min/max
        long startFastMath = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            intersectFastMath(boxesA[i % 1000], boxesB[i % 1000]);
            unionFastMath(boxesA[i % 1000], boxesB[i % 1000]);
        }
        long endFastMath = System.nanoTime();

        System.out.printf("Math.min/max:   %.3f ms%n", (endMath - startMath) / 1_000_000.0);
        System.out.printf("FastMath.min/max: %.3f ms%n", (endFastMath - startFastMath) / 1_000_000.0);
    }

    // Random AxisAlignedBB helper
    private static AxisAlignedBB randomBox() {
        double x1 = RAND.nextDouble() * 100;
        double y1 = RAND.nextDouble() * 100;
        double z1 = RAND.nextDouble() * 100;
        double x2 = x1 + RAND.nextDouble() * 10;
        double y2 = y1 + RAND.nextDouble() * 10;
        double z2 = z1 + RAND.nextDouble() * 10;
        return new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
    }

    // Original with Math.min/max
    private static AxisAlignedBB intersectMath(AxisAlignedBB a, AxisAlignedBB b) {
        double d0 = Math.max(a.minX, b.minX);
        double d1 = Math.max(a.minY, b.minY);
        double d2 = Math.max(a.minZ, b.minZ);
        double d3 = Math.min(a.maxX, b.maxX);
        double d4 = Math.min(a.maxY, b.maxY);
        double d5 = Math.min(a.maxZ, b.maxZ);
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    // Optimized with FastMath.min/max
    private static AxisAlignedBB intersectFastMath(AxisAlignedBB a, AxisAlignedBB b) {
        double d0 = FastMath.max(a.minX, b.minX);
        double d1 = FastMath.max(a.minY, b.minY);
        double d2 = FastMath.max(a.minZ, b.minZ);
        double d3 = FastMath.min(a.maxX, b.maxX);
        double d4 = FastMath.min(a.maxY, b.maxY);
        double d5 = FastMath.min(a.maxZ, b.maxZ);
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    private static AxisAlignedBB unionMath(AxisAlignedBB a, AxisAlignedBB b) {
        double d0 = Math.min(a.minX, b.minX);
        double d1 = Math.min(a.minY, b.minY);
        double d2 = Math.min(a.minZ, b.minZ);
        double d3 = Math.max(a.maxX, b.maxX);
        double d4 = Math.max(a.maxY, b.maxY);
        double d5 = Math.max(a.maxZ, b.maxZ);
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    private static AxisAlignedBB unionFastMath(AxisAlignedBB a, AxisAlignedBB b) {
        double d0 = FastMath.min(a.minX, b.minX);
        double d1 = FastMath.min(a.minY, b.minY);
        double d2 = FastMath.min(a.minZ, b.minZ);
        double d3 = FastMath.max(a.maxX, b.maxX);
        double d4 = FastMath.max(a.maxY, b.maxY);
        double d5 = FastMath.max(a.maxZ, b.maxZ);
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }
}
