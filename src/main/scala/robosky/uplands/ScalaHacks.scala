package robosky.uplands

import scala.collection.JavaConverters._
import robosky.uplands.block.{BlockRegistry, UplandsOreBlock}
import robosky.uplands.block.UplandsOreBlock.UplandsOreType

/**
 * Deprecated old scala code which refers to java types due to being incompletely converted.
 */
@Deprecated
object ScalaHacks {
  def createOreTypes(): java.util.Map[UplandsOreType, UplandsOreBlock] = UplandsOreBlock.oreTypes.map(t => t -> BlockRegistry.registerWithItem(s"${t.name}", new UplandsOreBlock(t))).toMap.asJava
}
