package robosky.ether.world.feature.megadungeon

import java.util
import java.util.Random

import com.google.common.collect.ImmutableList
import com.mojang.datafixers.util.Pair
import net.minecraft.block.Blocks
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.block.enums.StructureBlockMode
import net.minecraft.structure.pool.StructurePool.Projection
import net.minecraft.structure.pool.StructurePoolBasedGenerator.PieceFactory
import net.minecraft.structure.pool._
import net.minecraft.structure.{Structure, StructureManager, StructurePiece}
import net.minecraft.util.math.{BlockPos, MutableIntBoundingBox}
import net.minecraft.util.{BlockRotation, Identifier}
import net.minecraft.world.IWorld
import net.minecraft.world.gen.ChunkRandom
import net.minecraft.world.gen.chunk.ChunkGenerator
import robosky.ether.UplandsMod

object MegadungeonGenerator {
  def addPieces(generator: ChunkGenerator[_], manager: StructureManager, startPos: BlockPos, pieces: util.List[StructurePiece],
    random: ChunkRandom): Unit = {
    val pieceFactory: PieceFactory = MegadungeonPiece.create
    StructurePoolBasedGenerator.addPieces(UplandsMod :/ "megadungeon/entrance", 32, pieceFactory, generator,
      manager, startPos, pieces, random)
  }

  private def createSingleElement(name: String): StructurePoolElement = new SinglePoolElement((UplandsMod :/ name).toString,
    ImmutableList.of(), Projection.RIGID) {

    // Metadata shenanigans so your data structure blocks do something!
    // The if statement in this method is probably unneeded based on the implementations of the methods that call this,
    // but I included it for safety
    override def method_16756(world: IWorld, info: Structure.StructureBlockInfo, pos: BlockPos, rotation: BlockRotation,
      rand: Random, bbox: MutableIntBoundingBox): Unit =
      if (info.tag != null && StructureBlockMode.valueOf(info.tag.getString("mode")) == StructureBlockMode.DATA)
        handleMetadata(info.tag.getString("metadata"), info.pos, world, rand, bbox)
  }


  def handleMetadata(str: String, pos: BlockPos, world: IWorld, rand: Random, bbox: MutableIntBoundingBox): Unit = {
    if (str.startsWith("loot!")) {
      world.setBlockState(pos, Blocks.AIR.getDefaultState, 3)
      world.getBlockEntity(pos.down) match {
        case chest: ChestBlockEntity => chest.setLootTable(new Identifier(str.substring(5)), rand.nextLong)
        case _ =>
      }
    }
  }

  private def registerPool(name: String, pieces: (String, Int)*): Unit = {
    val array: Array[Pair[StructurePoolElement, Integer]] = pieces.map { case (s, i) => Pair.of(createSingleElement(s), Int.box(i)) }.toArray
    StructurePoolBasedGenerator.REGISTRY.add(new StructurePool(UplandsMod :/ name, new Identifier("minecraft", "empty"),
      ImmutableList.copyOf(array), Projection.RIGID))
  }

  registerPool("megadungeon/entrance", "megadungeon/entrance" -> 1)
  registerPool("megadungeon/shafts", "megadungeon/shaft_vertical" -> 1)

  def initialize(): Unit = {}
}