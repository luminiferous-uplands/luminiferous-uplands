package robosky.uplands.world.feature.megadungeon;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import robosky.uplands.UplandsMod;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.command.arguments.BlockStateArgumentType;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePool.Projection;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.pool.StructurePoolBasedGenerator.PieceFactory;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public final class MegadungeonGenerator {

    public MegadungeonGenerator() {
    }

    public static void addPieces(ChunkGenerator<?> generator, StructureManager manager, BlockPos startPos, List<StructurePiece> pieces, ChunkRandom random) {
        PieceFactory pieceFactory = MegadungeonPiece::create;
        MegadungeonPoolGenerator.addPieces(UplandsMod.id("megadungeon/entrance"),
            32,
            pieceFactory,
            generator,
            manager,
            startPos,
            pieces,
            random,
            UplandsMod.id("megadungeon/boss_room"));
    }

    private static final BlockStateArgumentType blockStateParser = BlockStateArgumentType.blockState();

    public static void handleMetadata(String str, BlockPos pos, IWorld world, Random rand, BlockBox bbox) {
        String a, b;
        String[] parts = str.split(";");
        if (parts.length == 1) {
            a = parts[0];
            b = "";
        } else if (parts.length >= 2) {
            a = parts[0];
            b = parts[1];
        } else {
            a = b = "";
        }
        if (a.startsWith("loot!")) {
            BlockEntity be = world.getBlockEntity(pos.down());
            if (be instanceof LootableContainerBlockEntity) {
                String id = a.substring(5);
                ((LootableContainerBlockEntity) be).setLootTable(new Identifier(id), rand.nextLong());
            }
        }
        BlockState tgt;
        if (b.startsWith("to!")) {
            StringReader reader = new StringReader(b.substring(3));
            try {
                tgt = blockStateParser.parse(reader).getBlockState();
            } catch (CommandSyntaxException e) {
                tgt = Blocks.CAVE_AIR.getDefaultState();
            }
        } else {
            tgt = Blocks.CAVE_AIR.getDefaultState();
        }
        world.setBlockState(pos, tgt, 3);
    }

    private static void registerPool(String name, String terminators, PoolTriple... pieces) {
        ImmutableList.Builder<Pair<StructurePoolElement, Integer>> builder = ImmutableList.builder();
        for (PoolTriple triple : pieces) {
            builder.add(Pair.of(new MetadataCapableSinglePoolElement(triple.name, triple.rotate), triple.weight));
        }
        Identifier termId;
        if (terminators != null) {
            termId = UplandsMod.id(terminators);
        } else {
            termId = new Identifier("minecraft", "empty");
        }
        StructurePoolBasedGenerator.REGISTRY.add(new StructurePool(UplandsMod.id(name), termId, builder.build(), Projection.RIGID));
    }

    private static final class PoolTriple {
        final String name;
        final int weight;
        final boolean rotate;

        PoolTriple(String name, int weight, boolean rotate) {
            this.name = name;
            this.weight = weight;
            this.rotate = rotate;
        }
    }

    public static void initialize() {
        registerPool("megadungeon/entrance", null,
            new PoolTriple("megadungeon/entrance", 1, false));
        registerPool("megadungeon/shafts", null,
            new PoolTriple("megadungeon/shaft_vertical", 4, false),
            new PoolTriple("megadungeon/shaft_bottom", 1, false));
        registerPool("megadungeon/halls", "megadungeon/dead_ends",
            new PoolTriple("megadungeon/hallway", 6, true),
            new PoolTriple("megadungeon/corner", 3, true),
            new PoolTriple("megadungeon/tee", 3, true),
            // new PoolTriple("megadungeon/staircase", 2, true), // causes massive vertical gen if enabled
            new PoolTriple("megadungeon/alcove", 1, true),
            new PoolTriple("megadungeon/spawner_room", 1, true),
            new PoolTriple("megadungeon/trap", 1, true),
            new PoolTriple("megadungeon/boss_room", 1, true),
            new PoolTriple("megadungeon/dead_end", 1, true));
        registerPool("megadungeon/dead_ends", null,
            new PoolTriple("megadungeon/dead_end", 1, true));
    }
}
