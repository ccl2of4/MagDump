package ccl2of4.magdump.item

import ccl2of4.magdump.entity.{Cartridge, ThirtyCal}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.world.World

object M60 extends AutomaticFirearm(50, "thirtyCal", classOf[ThirtyCal], 50, 5) {

  setUnlocalizedName("m60")
  setCreativeTab(CreativeTabs.tabCombat)

  protected override def makeCartridge(world: World, entityLiving: EntityLivingBase, deviation: Float): Cartridge =
    new ThirtyCal(world, entityLiving)

}
