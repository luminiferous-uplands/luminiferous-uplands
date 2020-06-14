package robosky.uplands.block.bossroom;

import robosky.uplands.block.BlockRegistry;
import robosky.uplands.block.package$;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.BlockRotation;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;

public class DoorwayBlockEntity extends BlockEntity implements BlockEntityClientSerializable {

    public static final BlockEntityType<DoorwayBlockEntity> TYPE = BlockEntityType.Builder.create(DoorwayBlockEntity::new, BlockRegistry.BOSS_DOORWAY).build(null);

    /**
     * When blocked, the block state this block entity appears as.
     */
    private BlockState mimicState = Blocks.AIR.getDefaultState();

    /**
     * The last time the mimic state was updated.
     */
    private long lastMimicUpdate = 0L;

    public DoorwayBlockEntity() {
        super(TYPE);
    }

    public BlockState mimicState() {
        return mimicState;
    }

    public void setMimicState(BlockState value) {
        this.mimicState = value;
        if (this.hasWorld()) {
            this.lastMimicUpdate = this.world.getTime();
        }
    }

    public long lastMimicUpdate() {
        return this.lastMimicUpdate;
    }

    @Override
    public void applyRotation(BlockRotation rot) {
        // silent update
        this.mimicState = this.mimicState.rotate(rot);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        tag.putString("Mimic", package$.MODULE$.stringifyState(this.mimicState));
        tag.putLong("LastMimicUpdate", this.lastMimicUpdate);
        return tag;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        return toClientTag(super.toTag(tag));
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        this.mimicState = package$.MODULE$.parseState(tag.getString("Mimic"));
        this.lastMimicUpdate = tag.getLong("LastMimicUpdate");
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        fromClientTag(tag);
    }
}
