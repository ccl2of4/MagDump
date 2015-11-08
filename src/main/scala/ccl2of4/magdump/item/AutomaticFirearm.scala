package ccl2of4.magdump.item

import ccl2of4.magdump.entity.{ThirtyCal, Cartridge}
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World

abstract class AutomaticFirearm(reloadTicks: Int, cartridgeName: String, cartridgeClass: Class[_ <: Cartridge], magazineCapacity: Int, fireRate: Int) extends Firearm(reloadTicks, cartridgeName, cartridgeClass, magazineCapacity) {

  protected override def numCartridges(numTicks: Int): Int =
    (numTicks / fireRate) + 1

  protected override def onFiringTick(itemStack: ItemStack, entityPlayer: EntityPlayer, tickNum: Int): Unit = {
    if (tickNum % fireRate == 0) {
      fire(itemStack, entityPlayer.getEntityWorld, entityPlayer, numCartridges(tickNum))
    }
  }

}
