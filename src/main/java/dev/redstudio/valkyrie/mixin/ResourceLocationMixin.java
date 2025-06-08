package dev.redstudio.valkyrie.mixin;

import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ResourceLocation.class)
public abstract class ResourceLocationMixin {

    /**
     * @reason Optimized static method to split object names like "modid:item". Uses fewer allocations and avoids unnecessary checks.
     * @author Strubium
     */
    @Overwrite
    public static String[] splitObjectName(String toSplit) {
        int i = toSplit.indexOf(':');
        if (i < 0) {
            return new String[] { "minecraft", toSplit };
        }
        String namespace = (i > 0) ? toSplit.substring(0, i) : "minecraft";
        String path = toSplit.substring(i + 1);
        return new String[] { namespace, path };
    }
}
