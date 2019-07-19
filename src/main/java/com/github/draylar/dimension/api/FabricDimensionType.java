package com.github.draylar.dimension.api;

import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;

import java.util.function.BiFunction;

/**
 * Fabric version of DimensionType which makes the constructor slightly easier to use.
 * DimensionType is a registry wrapper for Dimension[s].
 */
public class FabricDimensionType extends DimensionType {
    private int numericalID;
    private Identifier identifier;

    public FabricDimensionType(Identifier name, int dimensionID, BiFunction<World, DimensionType, ? extends Dimension> factory) {
        super(dimensionID, name.getNamespace() + "_" + name.getPath(), "DIM_" + name.getPath(), factory, true);
        this.numericalID = dimensionID;
        this.identifier = name;
    }

    public int getNumericalID() {
        return numericalID;
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}