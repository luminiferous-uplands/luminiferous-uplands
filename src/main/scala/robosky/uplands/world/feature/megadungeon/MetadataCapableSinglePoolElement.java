package robosky.uplands.world.feature.megadungeon;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import java.util.Random;

import net.minecraft.block.enums.StructureBlockMode;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool.Projection;
import net.minecraft.structure.pool.StructurePoolElementType;
import net.minecraft.structure.processor.JigsawReplacementStructureProcessor;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;

import robosky.uplands.UplandsMod;

public class MetadataCapableSinglePoolElement extends SinglePoolElement implements UplanderPoolElement {

    public static final StructurePoolElementType TYPE =
        Registry.register(Registry.STRUCTURE_POOL_ELEMENT, UplandsMod.id("metadata_element"), MetadataCapableSinglePoolElement::new);

    private final boolean rotateable;

    public MetadataCapableSinglePoolElement(String name, boolean rotateable) {
        super(UplandsMod.id(name).toString(), ImmutableList.of(), Projection.RIGID);
        this.rotateable = rotateable;
    }

    // deserialization
    public MetadataCapableSinglePoolElement(Dynamic<?> dyn) {
        super(dyn);
        this.rotateable = dyn.get("rotateable").asBoolean(true);
    }

    // serialzation
    @Override
    public <T> Dynamic<T> method_16625(DynamicOps<T> ops) {
        T value = super.method_16625(ops).getValue();
        return new Dynamic<>(ops, ops.mergeInto(value, ops.createString("rotateable"), ops.createBoolean(rotateable)));
    }

    @Override
    public StructurePoolElementType getType() {
       return TYPE;
    }

    // Metadata shenanigans so your data structure blocks do something!
    // The if statement in this method is probably unneeded based on the implementations of the methods that call this,
    // but I included it for safety
    @Override
    public void method_16756(IWorld world, Structure.StructureBlockInfo info, BlockPos pos, BlockRotation rotation,
      Random rand, MutableIntBoundingBox bbox) {
        if (info.tag != null && StructureBlockMode.valueOf(info.tag.getString("mode")) == StructureBlockMode.DATA) {
            MegadungeonGenerator.handleMetadata(info.tag.getString("metadata"), info.pos, world, rand, bbox);
        }
    }

    @Override
    protected StructurePlacementData method_16616(BlockRotation rot, MutableIntBoundingBox bbox) {
      StructurePlacementData data = new StructurePlacementData();
      data.setBoundingBox(bbox);
      data.setRotation(rot);
      data.method_15131(true);
      data.setIgnoreEntities(false);
      //      data.addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS)
      data.addProcessor(JigsawReplacementStructureProcessor.INSTANCE);
      this.processors.forEach(data::addProcessor);
      this.getProjection().getProcessors().forEach(data::addProcessor);
      return data;
    }

    @Override
    public Identifier getName() {
        return this.location;
    }

    @Override
    public boolean disableRotation() {
        return !rotateable;
    }
}
