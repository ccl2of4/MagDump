package ccl2of4.magdump.items

import ccl2of4.magdump.entity.cartridge.EntityCartridgeBuckshot
import net.minecraft.creativetab.CreativeTabs

object CoachGun extends SemiAutomaticFirearm(50, "buckShot", classOf[EntityCartridgeBuckshot], 2) {

  setUnlocalizedName("coachGun")
  setTextureName("magdump:coachGun")
  setCreativeTab(CreativeTabs.tabCombat)

}
