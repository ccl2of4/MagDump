package ccl2of4.magdump.items

import ccl2of4.magdump.entity.{EntityBuckShot, EntityCartridge}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.world.World

object CoachGun extends SemiAutomaticFirearm(50, "buckShot", classOf[EntityBuckShot], 2) {

  setUnlocalizedName("coachGun")
  setTextureName("magdump:coachGun")
  setCreativeTab(CreativeTabs.tabCombat)

}
