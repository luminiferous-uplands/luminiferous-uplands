package robosky.uplands.world.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.primitives.Floats;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public final class Clump {

    private final List<Entry> entries;

    private Clump(List<Entry> entries) {
        this.entries = entries;
    }

    public static Clump of(float r) {
        int ir = (int)Math.ceil(r);
        List<Entry> entries = new ArrayList<>();
        for(int z = -ir; z <= ir; z++) {
            for(int x = -ir; x <= ir; x++) {
                for(int y = -ir; y <= ir; y++) {
                    float d = (float)Math.sqrt(x * x + y * y + z * z);
                    if(d <= r) {
                        entries.add(new Clump.Entry(x, y, z, d));
                    }
                }
            }
        }
        if(entries.isEmpty()) {
            entries.add(new Clump.Entry(0, 0, 0, 0));
        }
        entries.sort((a, b) -> Floats.compare(a.d, b.d));
        return new Clump(entries);
    }

    public BlockPos removeUniform(Random rand, int dx, int dy, int dz) {
        int index = rand.nextInt(entries.size());
        return entries.remove(index).blockPos(dx, dy, dz);
    }

    public BlockPos removeGaussian(Random rand, int dx, int dy, int dz) {
        int index = (int)Math.abs(rand.nextGaussian() * entries.size());
        if(index >= entries.size()) {
            index = 0; //in unlikely scenarios, gaussian numbers can go way outside the bounds.
        } else if(index < 0) {
            index = 0;
        }
        return entries.remove(index).blockPos(dx, dy, dz);
    }

    public Clump copy() {
        return new Clump(new ArrayList<>(this.entries));
    }

    public int size() {
        return entries.size();
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    private static class Entry {

        private final int x;
        private final int y;
        private final int z;
        private final float d;

        public Entry(int x, int y, int z, float d) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.d = d;
        }

        public Vec3i pos() {
            return new Vec3i(x, y, z);
        }

        public BlockPos blockPos(int x, int y, int z) {
            return new BlockPos(this.x + x, this.y + y, this.z + z);
        }
    }
}
