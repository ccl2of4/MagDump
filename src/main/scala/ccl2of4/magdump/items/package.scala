package ccl2of4.magdump

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item

package object items {

  object ItemThirtyCal extends Item {
    setUnlocalizedName("762NATO")
    setCreativeTab(CreativeTabs.tabCombat)
    setTextureName("magdump:762NATO")
  }

  object ItemBuckShot extends Item {
    setUnlocalizedName("buckShot")
    setCreativeTab(CreativeTabs.tabCombat)
    setTextureName("magdump:buckShot")
  }
  
  object Item45ACP extends Item {
    setUnlocalizedName("45ACP")
    setCreativeTab(CreativeTabs.tabCombat)
    setTextureName("magdump:45ACP")
  }

}
