package robosky.uplands.world.feature;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

public class CloudFeature extends Feature<CloudFeatureConfig> {

    public CloudFeature() {
        super(CloudFeatureConfig.CODEC);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, CloudFeatureConfig config) {
        // generate a line?
        int halfLength = config.halfLength;
        int radius = config.radius;
        float angle = MathHelper.nextFloat(random, 0.0f, (float)Math.PI * 2.0f);
        // rotate
        int offsetX = (int)(halfLength * MathHelper.cos(angle));
        int offsetZ = (int)(halfLength * MathHelper.sin(angle));
        int outerOffsetX = (int)((halfLength + radius) * MathHelper.cos(angle));
        int outerOffsetZ = (int)((halfLength + radius) * MathHelper.sin(angle));
        int posX1 = pos.getX() - offsetX;
        int posZ1 = pos.getZ() - offsetZ;
        // euclidean distance of (X0, Z0) from a line given two points:
        //   (X2 - X1)(Z1 - Z0) - (X1 - X0)(Z2 - Z1)
        //   ---------------------------------------  =  distance
        //      sqrt( (X2 - X1)^2 + (Z2 - Z1)^2 )
        int scaleSq = 4 * offsetX * offsetX + 4 * offsetZ * offsetZ;
        int targetSq = scaleSq * radius * radius;
        int height = config.height;
        BlockPos.Mutable posMut = new BlockPos.Mutable();
        boolean generated = false;
        int incX = outerOffsetX < 0 ? -1 : 1;
        int incZ = outerOffsetZ < 0 ? -1 : 1;
        for(int x = pos.getX() - outerOffsetX, maxX = pos.getX() + outerOffsetX; x != maxX; x += incX) {
            for(int z = pos.getZ() - outerOffsetZ, maxZ = pos.getZ() + outerOffsetZ; z != maxZ; z += incZ) {
                int scaledDistance = 2 * offsetX * (posZ1 - z) - 2 * offsetZ * (posX1 - x);
                if(scaledDistance * scaledDistance < targetSq) {
                    for(int y = pos.getY() - height, maxY = pos.getY(); y < maxY; y++) {
                        posMut.set(x, y, z);
                        if(world.getBlockState(posMut).isAir()) {
                            world.setBlockState(posMut, config.state, 2);
                            generated = true;
                        }
                    }
                }
            }
        }
        return generated;
    }
}
