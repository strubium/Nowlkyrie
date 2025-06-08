package dev.redstudio.valkyrie;

import net.minecraft.util.ResourceLocation;

public class ResourceLocationSplitBenchmark {

    private static final int ITERATIONS = 100_000_000;
    private static final String[] TEST_STRINGS = {
            "minecraft:stone",
            "mod:item_name",
            "item_only",
            ":no_namespace",
            "example:item",
            "abc:def:ghi"
    };

    public static void main(String[] args) {
        System.out.println("Warming up...");
        for (int i = 0; i < 10_000; i++) {
            splitOriginal(TEST_STRINGS[i % TEST_STRINGS.length]);
            ResourceLocation.splitObjectName(TEST_STRINGS[i % TEST_STRINGS.length]);
        }

        System.out.println("Benchmarking original method...");
        long startOriginal = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            splitOriginal(TEST_STRINGS[i % TEST_STRINGS.length]);
        }
        long endOriginal = System.nanoTime();

        System.out.println("Benchmarking optimized mixin method...");
        long startOptimized = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            ResourceLocation.splitObjectName(TEST_STRINGS[i % TEST_STRINGS.length]);
        }
        long endOptimized = System.nanoTime();

        double originalTotalMs = (endOriginal - startOriginal) / 1_000_000.0;
        double optimizedTotalMs = (endOptimized - startOptimized) / 1_000_000.0;

        double originalAvgNs = (endOriginal - startOriginal) / (double) ITERATIONS;
        double optimizedAvgNs = (endOptimized - startOptimized) / (double) ITERATIONS;

        System.out.println("\n=== Results ===");
        System.out.printf("Original Total Time: %.2f ms%n", originalTotalMs);
        System.out.printf("Original Avg Time: %.2f ns%n", originalAvgNs);
        System.out.printf("Optimized Total Time: %.2f ms%n", optimizedTotalMs);
        System.out.printf("Optimized Avg Time: %.2f ns%n", optimizedAvgNs);
        System.out.printf("Speedup: %.2fx faster%n", originalAvgNs / optimizedAvgNs);
    }

    /**
     * Copy of the original ResourceLocation.splitObjectName before Mixin patch.
     */
    public static String[] splitOriginal(String toSplit) {
        String[] astring = new String[] { "minecraft", toSplit };
        int i = toSplit.indexOf(58);

        if (i >= 0) {
            astring[1] = toSplit.substring(i + 1);
            if (i > 1) {
                astring[0] = toSplit.substring(0, i);
            }
        }

        return astring;
    }
}
