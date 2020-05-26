package robosky.uplands.util;

/**
 * Like {@link net.minecraft.util.math.Box}, but immutable and integer-aligned.
 */
// Oh records, where art thou?
public final class IntBox {

    public static final IntBox EMPTY = new IntBox(0, 0, 0, 0, 0, 0);

    private final int minX;
    private final int minY;
    private final int minZ;
    private final int maxX;
    private final int maxY;
    private final int maxZ;

    public IntBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        if (minX > maxX || minY > maxY || minZ > maxZ) {
            throw new IllegalArgumentException(
                String.format("Invalid bounds: (%d, %d, %d, %d, %d, %d)", minX, minY, minZ, maxX, maxY, maxZ));
        }
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public int minX() {
        return minX;
    }

    public int minY() {
        return minY;
    }

    public int minZ() {
        return minZ;
    }

    public int maxX() {
        return maxX;
    }

    public int maxY() {
        return maxY;
    }

    public int maxZ() {
        return maxZ;
    }

    @Override
    public String toString() {
        return "IntBox{" +
            "minX=" + minX +
            ", minY=" + minY +
            ", minZ=" + minZ +
            ", maxX=" + maxX +
            ", maxY=" + maxY +
            ", maxZ=" + maxZ +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IntBox intBox = (IntBox) o;

        if (minX != intBox.minX) {
            return false;
        }
        if (minY != intBox.minY) {
            return false;
        }
        if (minZ != intBox.minZ) {
            return false;
        }
        if (maxX != intBox.maxX) {
            return false;
        }
        if (maxY != intBox.maxY) {
            return false;
        }
        return maxZ == intBox.maxZ;
    }

    @Override
    public int hashCode() {
        int result = minX;
        result = 31 * result + minY;
        result = 31 * result + minZ;
        result = 31 * result + maxX;
        result = 31 * result + maxY;
        result = 31 * result + maxZ;
        return result;
    }
}
