package ccl2of4.magdump.item

import ccl2of4.magdump.entity.Cartridge
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

abstract class SemiAutomaticFirearm(reloadTicks: Int, cartridgeName: String, cartridgeClass: Class[_ <: Cartridge], magazineCapacity: Int) extends Firearm(reloadTicks, cartridgeName, cartridgeClass, magazineCapacity) {

  protected override def numCartridges(numTicks: Int): Int = 1

  protected override def onFiringTick(itemStack: ItemStack, entityPlayer: EntityPlayer, tickNum: Int): Unit = {
    if (0 == tickNum) {
      fire(itemStack, entityPlayer.getEntityWorld, entityPlayer, 1)
    }
  }

}
