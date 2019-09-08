package robosky.ether.block.bossroom;

import net.minecraft.util.StringIdentifiable;

public enum ControlAdjustment implements StringIdentifiable {
    OUT("out"),
    IN("in");

    private final String name;

    private ControlAdjustment(String name) {
      this.name = name;
    }

    @Override
    public String asString() {
      return name;
    }
}
