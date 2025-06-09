package dev.redstudio.valkyrie.mixin;

import net.minecraft.util.IntegerCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(IntegerCache.class)
public class IntegerCacheMixin {

    /**
     * @reason Optimized getInteger — Just use javas
     * @author Strubium
     */
    @Overwrite
    public static Integer getInteger(int value) {
        return Integer.valueOf(value);
    }
}
