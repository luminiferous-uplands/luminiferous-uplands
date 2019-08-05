package robosky.ether.world.feature.megadungeon

import java.util

import com.google.common.collect.ImmutableList
import com.mojang.datafixers.util.Pair
import net.minecraft.structure.pool.StructurePool.Projection
import net.minecraft.structure.pool.StructurePoolBasedGenerator.PieceFactory
import net.minecraft.structure.pool._
import net.minecraft.structure.{StructureManager, StructurePiece}
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
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
    ImmutableList.of(), Projection.RIGID)

  private def registerPool(name: String, pieces: (String, Int)*): Unit = {
    val array: Array[Pair[StructurePoolElement, Integer]] = pieces.map { case (s, i) => Pair.of(createSingleElement(s), Int.box(i)) }.toArray
    StructurePoolBasedGenerator.REGISTRY.add(new StructurePool(UplandsMod :/ name, new Identifier("minecraft", "empty"),
      ImmutableList.copyOf(array), Projection.RIGID))
  }

  registerPool("megadungeon/entrance", "megadungeon/entrance" -> 1)
  registerPool("megadungeon/shafts", "megadungeon/shaft_vertical" -> 1)

  def initialize(): Unit = {}
}