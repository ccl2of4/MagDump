package ccl2of4.magdump.items

import ccl2of4.magdump.entity.EntityCartridge
import net.minecraft.creativetab.CreativeTabs

object M60 extends AutomaticFirearm(50, "thirtyCal", classOf[EntityCartridge], 50, 5) {

  setUnlocalizedName("m60")
  setTextureName("magdump:m60")
  setCreativeTab(CreativeTabs.tabCombat)

}
