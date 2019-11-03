package robosky.uplands.world.feature.megadungeon

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.exceptions.CommandSyntaxException

import java.util
import java.util.Random
import com.google.common.collect.ImmutableList
import com.mojang.datafixers.util.Pair
import net.minecraft.block.Blocks
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.block.enums.StructureBlockMode
import net.minecraft.command.arguments.BlockStateArgumentType
import net.minecraft.structure.pool.StructurePool.Projection
import net.minecraft.structure.pool.StructurePoolBasedGenerator.PieceFactory
import net.minecraft.structure.pool._
import net.minecraft.structure.processor.JigsawReplacementStructureProcessor
import net.minecraft.structure.{Structure, StructureManager, StructurePiece, StructurePlacementData}
import net.minecraft.util.math.{BlockPos, MutableIntBoundingBox}
import net.minecraft.util.registry.Registry
import net.minecraft.util.{BlockRotation, Identifier}
import net.minecraft.world.IWorld
import net.minecraft.world.gen.ChunkRandom
import net.minecraft.world.gen.chunk.ChunkGenerator
import robosky.uplands.UplandsMod

object MegadungeonGenerator {
  def addPieces(generator: ChunkGenerator[_], manager: StructureManager, startPos: BlockPos, pieces: util.List[StructurePiece],
    random: ChunkRandom): Unit = {
    val pieceFactory: PieceFactory = MegadungeonPiece.create
    MegadungeonPoolGenerator.addPieces(UplandsMod :/ "megadungeon/entrance", 32, pieceFactory, generator,
      manager, startPos, pieces, random, UplandsMod :/ "megadungeon/boss_room")
  }

  private val blockStateParser: BlockStateArgumentType = BlockStateArgumentType.blockState()

  def handleMetadata(str: String, pos: BlockPos, world: IWorld, rand: Random, bbox: MutableIntBoundingBox): Unit = {
    val (a, b) = str.split(';') match {
      case Array(pt1) => (pt1, "")
      case Array(pt1, pt2, _*) => (pt1, pt2)
    }
    if (a.startsWith("loot!")) {
      world.getBlockEntity(pos.down) match {
        case chest: LootableContainerBlockEntity =>
          val id = a.substring(5)
          chest.setLootTable(new Identifier(id), rand.nextLong)
        case _ =>
      }
    }
    val tgt = if (b.startsWith("to!")) {
      val reader = new StringReader(b.substring(3))
      try {
        blockStateParser.method_9654(reader).getBlockState
      } catch {
        case _: CommandSyntaxException => Blocks.CAVE_AIR.getDefaultState
      }
    } else {
      Blocks.CAVE_AIR.getDefaultState
    }
    world.setBlockState(pos, tgt, 3)
  }

  private def registerPool(name: String, terminators: Option[String] = None)(pieces: (String, Int, Boolean)*): Unit = {
    val array: Array[Pair[StructurePoolElement, Integer]] = pieces.map { case (s, i, b) =>
      Pair.of(new MetadataCapableSinglePoolElement(s, b).asInstanceOf[StructurePoolElement], Int.box(i)) }.toArray
    val termId = terminators.map(UplandsMod :/ _).getOrElse(new Identifier("minecraft", "empty"))
    StructurePoolBasedGenerator.REGISTRY.add(new StructurePool(UplandsMod :/ name, termId,
      ImmutableList.copyOf(array), Projection.RIGID))
  }

  registerPool("megadungeon/entrance")(("megadungeon/entrance", 1, false))
  registerPool("megadungeon/shafts")(("megadungeon/shaft_vertical", 4, false), ("megadungeon/shaft_bottom", 1, false))
  registerPool("megadungeon/halls", Some("megadungeon/dead_ends"))(
    ("megadungeon/hallway", 6, true),
    ("megadungeon/corner", 3, true),
    ("megadungeon/tee", 3, true),
    // ("megadungeon/staircase", 2, true), // causes massive vertical gen if enabled
    ("megadungeon/alcove", 1, true),
    ("megadungeon/spawner_room", 1, true),
    ("megadungeon/trap", 1, true),
    ("megadungeon/boss_room", 1, true),
    ("megadungeon/dead_end", 1, true))
  registerPool("megadungeon/dead_ends")(("megadungeon/dead_end", 1, true))

  def initialize(): Unit = {}
}
