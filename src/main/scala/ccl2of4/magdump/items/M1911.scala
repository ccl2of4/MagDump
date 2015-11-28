package ccl2of4.magdump.items

import ccl2of4.magdump.entity.cartridge.EntityCartridge762NATO
import net.minecraft.creativetab.CreativeTabs

object M1911 extends SemiAutomaticFirearm(30, "45ACP", classOf[EntityCartridge762NATO], 8) {

  setUnlocalizedName("m1911")
  setTextureName("magdump:m1911")
  setCreativeTab(CreativeTabs.tabCombat)

}
