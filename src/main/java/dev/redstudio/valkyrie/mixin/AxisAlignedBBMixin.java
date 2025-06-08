package dev.redstudio.valkyrie.mixin;

import net.jafama.FastMath;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AxisAlignedBB.class)
public class AxisAlignedBBMixin {

    /**
     * @reason Optimize with FastMath min/max instead of Math.min/Math.max
     * @author Strubium
     */
    @Overwrite
    public AxisAlignedBB intersect(AxisAlignedBB other) {
        AxisAlignedBB self = (AxisAlignedBB)(Object)this;
        double d0 = FastMath.max(self.minX, other.minX);
        double d1 = FastMath.max(self.minY, other.minY);
        double d2 = FastMath.max(self.minZ, other.minZ);
        double d3 = FastMath.min(self.maxX, other.maxX);
        double d4 = FastMath.min(self.maxY, other.maxY);
        double d5 = FastMath.min(self.maxZ, other.maxZ);
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    /**
     * @reason Optimize with FastMath min/max instead of Math.min/Math.max
     * @author Strubium
     */
    @Overwrite
    public AxisAlignedBB union(AxisAlignedBB other) {
        AxisAlignedBB self = (AxisAlignedBB)(Object)this;
        double d0 = FastMath.min(self.minX, other.minX);
        double d1 = FastMath.min(self.minY, other.minY);
        double d2 = FastMath.min(self.minZ, other.minZ);
        double d3 = FastMath.max(self.maxX, other.maxX);
        double d4 = FastMath.max(self.maxY, other.maxY);
        double d5 = FastMath.max(self.maxZ, other.maxZ);
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

}
