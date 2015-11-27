package ccl2of4.magdump

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item

package object items {

  object ThirtyCal extends Item {
    setUnlocalizedName("762NATO")
    setCreativeTab(CreativeTabs.tabCombat)
    setTextureName("magdump:762NATO")
  }

  object BuckShot extends Item {
    setUnlocalizedName("buckShot")
    setCreativeTab(CreativeTabs.tabCombat)
    setTextureName("magdump:buckShot")
  }

}
