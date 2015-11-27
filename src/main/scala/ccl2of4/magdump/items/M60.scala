package ccl2of4.magdump.items

import ccl2of4.magdump.entity.cartridge.EntityCartridge762NATO
import net.minecraft.creativetab.CreativeTabs

object M60 extends AutomaticFirearm(50, "762NATO", classOf[EntityCartridge762NATO], 50, 5) {

  setUnlocalizedName("m60")
  setTextureName("magdump:m60")
  setCreativeTab(CreativeTabs.tabCombat)

}
