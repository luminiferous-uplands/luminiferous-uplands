package robosky.ether.block.bossroom;

import net.minecraft.util.StringIdentifiable;

public enum DoorwayState implements StringIdentifiable {
    OPEN("open"),
    BLOCKED("blocked");

    private final String name;

    private DoorwayState(String name) {
      this.name = name;
    }

    @Override
    public String asString() {
      return name;
    }
}
