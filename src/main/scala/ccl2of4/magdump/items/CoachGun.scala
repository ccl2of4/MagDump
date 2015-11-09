package ccl2of4.magdump.items

import ccl2of4.magdump.entity.{ThirtyCal, Cartridge, BuckShot}
import ccl2of4.magdump.items.M60._
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.world.World

object CoachGun extends SemiAutomaticFirearm(50, "buckShot", classOf[BuckShot], 2) {

  setUnlocalizedName("coachGun")
  setTextureName("magdump:coachGun")
  setCreativeTab(CreativeTabs.tabCombat)

  protected override def makeCartridge(world: World, entityLiving: EntityLivingBase, deviation: Float): Cartridge =
    new BuckShot(world, entityLiving)

}
