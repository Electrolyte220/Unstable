package dev.electrolyte.unstable;

import net.minecraft.util.StringRepresentable;

public class UnstableEnums {

    public enum ChestLocation implements StringRepresentable {
        NORTH,
        SOUTH,
        EAST,
        WEST,
        NONE;

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase();
        }
    }
}
