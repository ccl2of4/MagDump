package ccl2of4.magdump.items

import ccl2of4.magdump.entity.EntityCartridge
import net.minecraft.creativetab.CreativeTabs

object CoachGun extends SemiAutomaticFirearm(50, "buckShot", classOf[EntityCartridge], 2) {

  setUnlocalizedName("coachGun")
  setTextureName("magdump:coachGun")
  setCreativeTab(CreativeTabs.tabCombat)

}
