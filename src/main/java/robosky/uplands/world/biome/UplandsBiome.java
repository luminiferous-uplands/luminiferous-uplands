package robosky.uplands.world.biome;

public interface UplandsBiome {
    default double getIslandSize() {
        return 50;
    }

    default double getTopInterpolationStart() {
        return 0;
    }
}
