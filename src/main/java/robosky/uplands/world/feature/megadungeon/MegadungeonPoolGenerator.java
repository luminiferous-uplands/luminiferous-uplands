package robosky.uplands.world.feature.megadungeon;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import net.minecraft.block.JigsawBlock;
import net.minecraft.structure.*;
import net.minecraft.structure.pool.EmptyPoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import robosky.uplands.world.feature.megadungeon.MegadungeonPoolGenerator.Impl;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class MegadungeonPoolGenerator {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void addPieces(Identifier start, int maxRooms, StructurePoolBasedGenerator.PieceFactory pieceFactory,
                                 ChunkGenerator<?> generator, StructureManager manager, BlockPos pos, List<StructurePiece> pieces,
                                 Random rand, Identifier requiredRoom) {
        StructureFeatures.initialize();
        new Impl(maxRooms, pieceFactory, generator, manager, pieces, rand, requiredRoom).doAddPieces(start, pos);

    }

    static final class Impl {
        private final int maxRooms;
        private final StructurePoolBasedGenerator.PieceFactory pieceFactory;
        private final ChunkGenerator<?> generator;
        private final StructureManager manager;
        private final List<StructurePiece> pieces;
        private final Random random;
        private Identifier requiredRoom;
        private boolean requiredRoomAdded = false;
        private final Deque<Entry> entryQueue = Queues.newArrayDeque();

        Impl(int maxRooms, StructurePoolBasedGenerator.PieceFactory pieceFactory, ChunkGenerator<?> generator,
             StructureManager manager, List<StructurePiece> pieces, Random rand, Identifier requiredRoom) {
            this.maxRooms = maxRooms;
            this.pieceFactory = pieceFactory;
            this.generator = generator;
            this.manager = manager;
            this.pieces = pieces;
            this.random = rand;
            this.requiredRoom = requiredRoom;
        }

        private void doAddPieces(Identifier start, BlockPos pos) {
            // decide on a starting piece
            StructurePool pool = StructurePoolBasedGenerator.REGISTRY.get(start);
            StructurePoolElement element = pool.getRandomElement(random);
            BlockRotation rotation = (element instanceof UplanderPoolElement && ((UplanderPoolElement) element)
                    .disableRotation()) ? BlockRotation.NONE : BlockRotation.random(random);
            PoolStructurePiece piece = pieceFactory.create(manager, element, pos, element.getGroundLevelDelta(), rotation,
                    element.getBoundingBox(manager, pos, rotation));
            int midX = (piece.getBoundingBox().maxX + piece.getBoundingBox().minX) / 2;
            int midZ = (piece.getBoundingBox().maxZ + piece.getBoundingBox().minZ) / 2;
            int midY = generator.getHeightOnGround(midX, midZ, Heightmap.Type.WORLD_SURFACE_WG);
            piece.translate(0, midY - (piece.getBoundingBox().minY + piece.getGroundLevelDelta()), 0);
            pieces.add(piece);
            // branch out from the starting piece (recursively)
            if (maxRooms > 0) {
                final int radius = 128;
                Box box = new Box(midX - radius, midY - radius, midZ - radius, midX + radius + 1, midY + radius + 1, midZ + radius + 1);
                this.entryQueue.addLast(new Entry(piece, new AtomicReference<>(VoxelShapes
                        .combineAndSimplify(VoxelShapes.cuboid(box), VoxelShapes.cuboid(Box.from(piece.getBoundingBox())),
                                BooleanBiFunction.ONLY_FIRST)), midY + 80, 0));

                while (!this.entryQueue.isEmpty()) {
                    Entry entry = this.entryQueue.removeFirst();
                    this.propagateEntry(entry.piece, entry.shape, entry.minY, entry.roomIndex);
                }

            }
        }

        /**
         * If the given piece has any junctions, attempts to add additional
         * pieces to the piece queue.
         */
        private void propagateEntry(PoolStructurePiece piece, AtomicReference<VoxelShape> shape, int int_1, int roomIndex) {
            AtomicReference<VoxelShape> shape1 = new AtomicReference<>();
            BlockBox bbox = piece.getBoundingBox();

            for (Structure.StructureBlockInfo info : piece.getPoolElement().getStructureBlockInfos(this.manager,
                    piece.getPos(), piece.getRotation(), this.random)) {
                Direction direction_1 = info.state.get(JigsawBlock.FACING);
                StructurePool mainPool = StructurePoolBasedGenerator.REGISTRY.get(new Identifier(info.tag.getString("target_pool")));
                StructurePool terminatorPool = StructurePoolBasedGenerator.REGISTRY.get(mainPool.getTerminatorsId());
                if (mainPool.getElementCount() != 0 || mainPool == StructurePool.EMPTY) {
                    AtomicReference<VoxelShape> shape4;
                    int minY;
                    if (bbox.contains(info.pos.offset(direction_1))) {
                        shape4 = shape1;
                        minY = bbox.minY;
                        if (shape1.get() == null) {
                            shape1.set(VoxelShapes.cuboid(Box.from(bbox)));
                        }
                    } else {
                        shape4 = shape;
                        minY = int_1;
                    }

                    List<StructurePoolElement> elements = Lists.newArrayList();
                    // add pieces from the pool we want to consider for generation
                    // only add termination pieces if the boss room is fully processed
                    if (roomIndex != this.maxRooms || (requiredRoom != null && !requiredRoomAdded)) {
                        elements.addAll(mainPool.getElementIndicesInRandomOrder(this.random));
                    }
                    elements.addAll(terminatorPool.getElementIndicesInRandomOrder(this.random));
                    // whether all this looping has actually added a piece yet
                    boolean added = false;
                    for (StructurePoolElement element1 : elements) {
                        // only add one piece
                        if (added) break;
                        // don't process the empty pool
                        if (element1 == EmptyPoolElement.INSTANCE) {
                            break;
                        }
                        boolean finalRoom =
                            element1 instanceof UplanderPoolElement && ((UplanderPoolElement) element1).getName().equals(requiredRoom);
                        // don't add more than one boss room
                        if (finalRoom && requiredRoomAdded) {
                            continue;
                        }

                        // whether this piece can connect to its neighbors
                        boolean noJunction = false;
                        List<BlockRotation> rotations = (element1 instanceof UplanderPoolElement && ((UplanderPoolElement) element1)
                                .disableRotation()) ? ImmutableList.of(BlockRotation.NONE) : BlockRotation.randomRotationOrder(this.random);
                        for (BlockRotation rotation : rotations) {
                            // get a list of potential pieces of the given rotation
                            List<Structure.StructureBlockInfo> infos = element1.getStructureBlockInfos(this.manager, BlockPos.ORIGIN, rotation, this.random);
                            int maxElementHeight = element1.getBoundingBox(this.manager, BlockPos.ORIGIN, rotation).getBlockCountY() > 16 ? 0 :
                                    infos.stream().mapToInt((info1) -> {
                                        if (!element1.getBoundingBox(this.manager, BlockPos.ORIGIN, rotation).contains(info1.pos
                                                .offset(info1.state.get(JigsawBlock.FACING)))) {
                                            return 0;
                                        } else {
                                            return Math.max(StructurePoolBasedGenerator.REGISTRY.get(new Identifier(info1.tag
                                                            .getString("target_pool"))).getHighestY(this.manager),
                                                    StructurePoolBasedGenerator.REGISTRY.get(StructurePoolBasedGenerator
                                                            .REGISTRY.get(new Identifier(info1.tag.getString("target_pool")))
                                                            .getTerminatorsId()).getHighestY(this.manager));
                                        }
                                    }).max().orElse(0);

                            int y = 0;
                            int relativeY = 0;
                            int offsetY;
                            BlockBox bbox2 = null;
                            BlockPos pos = null;
                            int height;
                            // determine if a structure can generate and set the
                            // positions/dimensions to the appropriate values
                            do {
                                Optional<Structure.StructureBlockInfo> first = infos.stream().filter(i -> JigsawBlock
                                        .attachmentMatches(info, i)).findFirst();
                                if (!first.isPresent()) {
                                    noJunction = true;
                                    break;
                                }
                                Structure.StructureBlockInfo connection = first.get();
                                // remove the s tructure from future consideration
                                // in future iterations of this do-while
                                infos.remove(connection);

                                BlockPos offset = new BlockPos(info.pos.offset(direction_1).getX() - connection.pos.getX(),
                                        info.pos.offset(direction_1).getY() - connection.pos.getY(), info.pos.offset(direction_1).getZ() - connection.pos.getZ());
                                BlockBox bbox4 = element1.getBoundingBox(this.manager, offset, rotation);
                                y = connection.pos.getY();
                                relativeY = info.pos.getY() - bbox.minY - y + info.state.get(JigsawBlock.FACING).getOffsetY();
                                offsetY = bbox.minY + relativeY;
                                bbox2 = bbox4.offset(0, offsetY - bbox4.minY, 0);
                                pos = offset.add(0, offsetY - bbox4.minY, 0);
                                if (maxElementHeight > 0) {
                                    height = Math.max(maxElementHeight + 1, bbox2.maxY - bbox2.minY);
                                    bbox2.maxY = bbox2.minY + height;
                                }
                            } while (VoxelShapes.matchesAnywhere(shape4.get(), VoxelShapes.cuboid(Box.from(bbox2).contract(0.25D)),
                                    BooleanBiFunction.ONLY_SECOND));
                            // don't add the piece if there is no possible connection
                            if (noJunction) break;

                            shape4.set(VoxelShapes.combine(shape4.get(), VoxelShapes.cuboid(Box.from(bbox2)), BooleanBiFunction.ONLY_FIRST));
                            height = piece.getGroundLevelDelta();
                            // get the piece to generate
                            PoolStructurePiece piece1 = this.pieceFactory.create(this.manager, element1, pos,
                                    height - relativeY, rotation, bbox2);

                            piece.addJunction(new JigsawJunction(info.pos.offset(direction_1).getX(), bbox.minY +
                                    info.pos.getY() - bbox.minY - (info.pos.getY() - bbox.minY) + height, info.pos.offset(direction_1).getZ(),
                                    relativeY, StructurePool.Projection.RIGID));
                            piece1.addJunction(new JigsawJunction(info.pos.getX(), bbox.minY + info.pos.getY() -
                                    bbox.minY - y + height - relativeY, info.pos.getZ(), -relativeY, StructurePool.Projection.RIGID));
                            this.pieces.add(piece1);
                            // add the new piece for recursive consideration if we should generate more
                            if (roomIndex + 1 <= this.maxRooms || (requiredRoom != null && !requiredRoomAdded)) {
                                this.entryQueue.addLast(new Entry(piece1, shape4, minY, roomIndex + 1));
                            }
                            // break the outer while, since we have added a piece
                            added = true;
                            if (finalRoom) {
                                requiredRoomAdded = true;
                            }
                            break;
                        }
                    }
                } else {
                    MegadungeonPoolGenerator.LOGGER.warn("Empty or nonexistent pool: {}", info.tag.getString("target_pool"));
                }
            }
        }
    }

    /**
     * Stores a PoolStructurePiece for potential recursive child generation.
     */
    private static final class Entry {
        private final PoolStructurePiece piece;
        private final AtomicReference<VoxelShape> shape;
        private final int minY;
        private final int roomIndex;

        private Entry(PoolStructurePiece piece, AtomicReference<VoxelShape> reference, int minY, int roomIndex) {
            this.piece = piece;
            this.shape = reference;
            this.minY = minY;
            this.roomIndex = roomIndex;
        }

    }
}
