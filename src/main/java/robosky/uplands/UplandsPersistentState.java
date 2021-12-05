package robosky.uplands;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.PersistentState;

public class UplandsPersistentState extends PersistentState {

    public static final String KEY = "LuminiferousUplands";

    @Nullable
    private Vec3d platformLocation;

    public UplandsPersistentState() {
        super(KEY);
        this.platformLocation = null;
    }

    @Nullable
    public Vec3d getPlatformLocation() {
        return platformLocation;
    }

    public void setPlatformLocation(@Nullable Vec3d platformLocation) {
        this.platformLocation = platformLocation;
        this.setDirty(true);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        int[] platform = tag.getIntArray("SpawnPlatform");
        if(platform.length < 3) {
            platformLocation = null;
        } else {
            platformLocation = new Vec3d(platform[0], platform[1], platform[2]);
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        if(platformLocation != null) {
            tag.putIntArray("SpawnPlatform", new int[]{ (int)platformLocation.x, (int)platformLocation.y, (int)platformLocation.z });
        }
        return tag;
    }
}
