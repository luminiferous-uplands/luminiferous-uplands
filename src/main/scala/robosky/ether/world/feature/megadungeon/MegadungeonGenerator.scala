package robosky.ether.world.feature.megadungeon

import java.util
import java.util.Random

import com.google.common.collect.ImmutableList
import com.mojang.datafixers.util.Pair
import net.minecraft.block.Blocks
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.block.enums.StructureBlockMode
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
import robosky.ether.UplandsMod

object MegadungeonGenerator {
  def addPieces(generator: ChunkGenerator[_], manager: StructureManager, startPos: BlockPos, pieces: util.List[StructurePiece],
    random: ChunkRandom): Unit = {
    val pieceFactory: PieceFactory = MegadungeonPiece.create
    MegadungeonPoolGenerator.addPieces(UplandsMod :/ "megadungeon/entrance", 32, pieceFactory, generator,
      manager, startPos, pieces, random, UplandsMod :/ "megadungeon/boss_room")
  }

  case class MetadataCapableSinglePoolElement(name: String, rotateable: Boolean) extends SinglePoolElement((UplandsMod :/ name).toString, ImmutableList.of(), Projection.RIGID)
    with UplanderPoolElement {

    // Metadata shenanigans so your data structure blocks do something!
    // The if statement in this method is probably unneeded based on the implementations of the methods that call this,
    // but I included it for safety
    override def method_16756(world: IWorld, info: Structure.StructureBlockInfo, pos: BlockPos, rotation: BlockRotation,
      rand: Random, bbox: MutableIntBoundingBox): Unit =
      if (info.tag != null && StructureBlockMode.valueOf(info.tag.getString("mode")) == StructureBlockMode.DATA)
        handleMetadata(info.tag.getString("metadata"), info.pos, world, rand, bbox)

    override protected def method_16616(rot: BlockRotation, bbox: MutableIntBoundingBox): StructurePlacementData = {
      val data = new StructurePlacementData
      data.setBoundingBox(bbox)
      data.setRotation(rot)
      data.method_15131(true)
      data.setIgnoreEntities(false)
      //      data.addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS)
      data.addProcessor(JigsawReplacementStructureProcessor.INSTANCE)
      this.processors.forEach(p => data.addProcessor(p))
      this.getProjection.getProcessors.forEach(p => data.addProcessor(p))
      data
    }

    override def getName: Identifier = location

    override def disableRotation: Boolean = !rotateable
  }


  def handleMetadata(str: String, pos: BlockPos, world: IWorld, rand: Random, bbox: MutableIntBoundingBox): Unit = {
    val (a, b) = str.split(';') match {
      case Array(pt1, pt2) => (pt1, pt2)
      case Array(pt1) => (pt1, "")
    }
    if (a.startsWith("loot!")) {
      world.getBlockEntity(pos.down) match {
        case chest: LootableContainerBlockEntity =>
          val id = str.substring(5)
          chest.setLootTable(new Identifier(id), rand.nextLong)
        case _ =>
      }
    }
    val tgt = if(b.startsWith("to!")) Registry.BLOCK.get(new Identifier(b.substring(3))) else Blocks.CAVE_AIR
    world.setBlockState(pos, tgt.getDefaultState, 3)
  }

  private def registerPool(name: String, pieces: (String, Int, Boolean)*): Unit = {
    val array: Array[Pair[StructurePoolElement, Integer]] = pieces.map { case (s, i, b) =>
      Pair.of(MetadataCapableSinglePoolElement(s, b).asInstanceOf[StructurePoolElement], Int.box(i)) }.toArray
    StructurePoolBasedGenerator.REGISTRY.add(new StructurePool(UplandsMod :/ name, new Identifier("minecraft", "empty"),
      ImmutableList.copyOf(array), Projection.RIGID))
  }

  registerPool("megadungeon/entrance", ("megadungeon/entrance", 1, false))
  registerPool("megadungeon/shafts", ("megadungeon/shaft_vertical", 4, false), ("megadungeon/shaft_bottom", 1, false))
  registerPool("megadungeon/halls",
    ("megadungeon/hallway", 6, true),
    ("megadungeon/corner", 3, true),
    ("megadungeon/tee", 3, true),
    // ("megadungeon/staircase", 2, true), // causes massive vertical gen if enabled
    ("megadungeon/alcove", 1, true),
    ("megadungeon/spawner_room", 1, true),
    ("megadungeon/trap", 1, true),
    ("megadungeon/dead_end", 1, true))

  def initialize(): Unit = {}
}
