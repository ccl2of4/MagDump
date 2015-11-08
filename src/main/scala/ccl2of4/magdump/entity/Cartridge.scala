package ccl2of4.magdump.entity

import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World

abstract class Cartridge(world: World, entityLiving: EntityLivingBase) extends Entity(world) {

  override def entityInit(): Unit = {}
  override def writeEntityToNBT(p_70014_1_ : NBTTagCompound): Unit = {}
  override def readEntityFromNBT(p_70037_1_ : NBTTagCompound): Unit = {}

}
