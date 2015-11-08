package ccl2of4.magdump.entity

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.world.World

abstract class Cartridge(world: World, entityLiving: EntityLivingBase, deviation: Float) extends EntityArrow(world, entityLiving, deviation) {

}
