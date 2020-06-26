package robosky.uplands.block.bossroom;

import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import robosky.uplands.block.BlockRegistry;
import robosky.uplands.block.unbreakable.Unbreakable;
import robosky.uplands.util.BlockUtil;
import robosky.uplands.util.IntBox;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;

/**
 * Controls boss room mechanics.
 */
public final class ControlBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable {

    public static final BlockEntityType<ControlBlockEntity> TYPE = BlockEntityType.Builder.create(ControlBlockEntity::new, BlockRegistry.BOSS_CONTROL).build(null);

    private static final Logger logger = LogManager.getLogger();

    /**
     * The bounds of the boss room, relative to the position of this instance.
     */
    private IntBox bounds = IntBox.EMPTY;

    /**
     * The absolute world position of the boss room bounds. Assumes that the
     * position of the block entity does not change between ticks (a reasonable
     * assumption). This field is kept in sync wuth `bounds`.
     */
    private transient Box worldBoundsBox = new Box(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

    /**
     * The boss entity to spawn on command.
     */
    private EntityType<?> bossType = EntityType.PIG;

    /**
     * The boss entity UUID.
     */
    private UUID bossUuid = null;

    /**
     * The boss entity. Late-initialized on the first tick.
     */
    private transient Entity bossEntity = null;

    private BlockState replacement = Blocks.AIR.getDefaultState();

    public ControlBlockEntity() {
        super(TYPE);
    }

    public IntBox bounds() {
        return bounds;
    }

    public EntityType<?> bossType() {
        return bossType;
    }

    public void setBossType(EntityType<?> bossType) {
        this.bossType = bossType;
    }

    private void setBounds(IntBox value) {
        bounds = value;
        worldBoundsBox = new Box(
            this.pos.getX() + value.minX(),
            this.pos.getY() + value.minY(),
            this.pos.getZ() + value.minZ(),
            this.pos.getX() + value.maxX(),
            this.pos.getY() + value.maxY(),
            this.pos.getZ() + value.maxZ());
    }

    public void adjustBounds(Direction dir, int blocks) {
        int minX = bounds.minX();
        int minY = bounds.minY();
        int minZ = bounds.minZ();
        int maxX = bounds.maxX();
        int maxY = bounds.maxY();
        int maxZ = bounds.maxZ();
        switch (dir) {
        case DOWN:
            if (bounds.minY() - blocks <= bounds.maxY()) {
                minY = bounds.minY() - blocks;
            }
            break;
        case UP:
            if (bounds.maxY() + blocks >= bounds.minY()) {
                maxY = bounds.maxY() + blocks;
            }
            break;
        case NORTH:
            if (bounds.minZ() - blocks <= bounds.maxZ()) {
                minZ = bounds.minZ() - blocks;
            }
            break;
        case SOUTH:
            if (bounds.maxZ() + blocks >= bounds.minZ()) {
                maxZ = bounds.maxZ() + blocks;
            }
            break;
        case WEST:
            if (bounds.minX() - blocks <= bounds.maxX()) {
                minX = bounds.minX() - blocks;
            }
            break;
        case EAST:
            if (bounds.maxX() + blocks >= bounds.minX()) {
                maxX = bounds.maxX() + blocks;
            }
            break;
        }
        this.setBounds(new IntBox(minX, minY, minZ, maxX, maxY, maxZ));
    }

    private Iterable<BlockPos> bossRoomPositions() {
        return BlockPos.iterate(
            this.pos.getX() + bounds.minX(),
            this.pos.getY() + bounds.minY(),
            this.pos.getZ() + bounds.minZ(),
            this.pos.getX() + bounds.maxX(),
            this.pos.getY() + bounds.maxY(),
            this.pos.getZ() + bounds.maxZ());
    }

    /**
     * Spawns the boss at the specified BlockPos.
     */
    public void activateBoss(BlockPos spawn) {
        if (!this.world.isClient && bossUuid == null) {
            for (BlockPos pos : bossRoomPositions()) {
                BlockState state = this.world.getBlockState(pos);
                if (state.getBlock() == BlockRegistry.BOSS_DOORWAY) {
                    this.world.setBlockState(pos, state.with(DoorwayBlock.STATE, DoorwayState.BLOCKED));
                }
            }
            bossEntity = bossType.spawn(this.world, null, null, null, spawn, SpawnType.SPAWNER, false, false);
            bossUuid = bossEntity.getUuid();
            logger.info("Activated boss at {}", spawn);
        }
    }

    /**
     * Despawns the boss (e.g. if the player died while fighting)
     */
    public void deactivateBoss() {
        if (!this.world.isClient) {
            if (bossEntity != null) {
                if (this.world instanceof ServerWorld) {
                    ((ServerWorld) this.world).removeEntity(bossEntity);
                }
            }
            bossUuid = null;
            for (BlockPos pos : bossRoomPositions()) {
                BlockState state = this.world.getBlockState(pos);
                if (state.getBlock() == BlockRegistry.BOSS_DOORWAY) {
                    this.world.setBlockState(pos, state.with(DoorwayBlock.STATE, DoorwayState.OPEN));
                }
            }
            logger.info("Deactivated boss");
        }
    }

    /**
     * Cleans up the boss room upon the boss's defeat. This includes replacing
     * unbreakable blocks with their breakable counterparts.
     */
    public void onBossDefeat() {
        bossUuid = null;
        if (!this.world.isClient) {
            for (BlockPos pos : bossRoomPositions()) {
                BlockState state = this.world.getBlockState(pos);
                Block block = state.getBlock();
                if (block == BlockRegistry.BOSS_DOORWAY) {
                    this.world.setBlockState(pos, Blocks.AIR.getDefaultState());
                } else if (block instanceof Unbreakable) {
                    this.world.setBlockState(pos, ((Unbreakable) block).toBreakable(state));
                }
            }
            this.world.setBlockState(this.pos, replacement);
            logger.info("Detected boss defeat");
        }
    }

    @Override
    public void tick() {
        if (!this.world.isClient) {
            // initialize bossEntity if it is not already
            // this is done in tick() to ensure that the entity
            // is loaded
            if (bossUuid != null) {
                if (bossEntity == null) {
                    if (this.world instanceof ServerWorld) {
                        bossEntity = ((ServerWorld) this.world).getEntity(bossUuid);
                    }
                }
                // in case there is no entity with the given UUID yet
                if (bossEntity != null) {
                    if (!bossEntity.isAlive()) {
                        onBossDefeat();
                    } else {
                        List<PlayerEntity> players = this.world.getEntities(PlayerEntity.class, worldBoundsBox, null);
                        if (players.isEmpty()) {
                            // everyone died (or escaped)
                            deactivateBoss();
                        }
                    }
                }
            } else {
                bossEntity = null;
            }
        }
    }

    @Override
    public void applyRotation(BlockRotation rot) {
        IntBox box;
        switch (rot) {
        case COUNTERCLOCKWISE_90:
            box = new IntBox(
                bounds.minZ(),
                bounds.minY(),
                -bounds.maxX() + 1,
                bounds.maxZ(),
                bounds.maxY(),
                -bounds.minX() + 1);
            break;
        case CLOCKWISE_180:
            box = new IntBox(
                -bounds.maxX() + 1,
                bounds.minY(),
                -bounds.maxZ() + 1,
                -bounds.minX() + 1,
                bounds.maxY(),
                -bounds.minZ() + 1);
            break;
        case CLOCKWISE_90:
            box = new IntBox(
                -bounds.maxZ() + 1,
                bounds.minY(),
                bounds.minX(),
                -bounds.minZ() + 1,
                bounds.maxY(),
                bounds.maxX());
            break;
        default:
            box = bounds;
        }
        setBounds(box);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        return toClientTag(super.toTag(tag));
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        tag.putIntArray("Bounds", new int[] {bounds.minX(), bounds.minY(), bounds.minZ(), bounds.maxX(), bounds.maxY(), bounds.maxZ()});
        tag.putString("Boss", Registry.ENTITY_TYPE.getId(bossType).toString());
        if (bossUuid != null) {
            tag.putUuid("BossUUID", bossUuid);
        }
        tag.putString("Replacement", BlockUtil.stringifyState(replacement));
        return tag;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        fromClientTag(tag);
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        int[] boxArr = tag.getIntArray("Bounds");
        if (boxArr.length == 6 && boxArr[0] <= boxArr[3] && boxArr[1] <= boxArr[4] && boxArr[2] <= boxArr[5]) {
            setBounds(new IntBox(boxArr[0], boxArr[1], boxArr[2], boxArr[3], boxArr[4], boxArr[5]));
        } else {
            setBounds(IntBox.EMPTY);
        }

        Identifier id = Identifier.tryParse(tag.getString("Boss"));
        if (id != null) {
            bossType = Registry.ENTITY_TYPE.get(id);
        } else {
            bossType = EntityType.PIG;
        }

        if (tag.containsUuid("BossUUID")) {
            bossUuid = tag.getUuid("BossUUID");
        } else {
            bossUuid = null;
        }
        replacement = BlockUtil.parseState(tag.getString("Replacement"));
    }
}
