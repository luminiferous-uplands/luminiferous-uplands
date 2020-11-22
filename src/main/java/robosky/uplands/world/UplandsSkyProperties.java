package robosky.uplands.world;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.math.Vec3d;

public class UplandsSkyProperties extends SkyProperties {

    public UplandsSkyProperties() {
        super(8, false, SkyType.NORMAL, false, false);
    }

    @Override
    public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
        return color.multiply(sunHeight * 0.94F + 0.06F, sunHeight * 0.94F + 0.06F, sunHeight * 0.91F + 0.09F);
    }

    @Override
    public boolean useThickFog(int camX, int camY) {
        return false;
    }

    @Override
    public @Nullable float[] getFogColorOverride(float skyAngle, float tickDelta) {
        return null;
    }
}
