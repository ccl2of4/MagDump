package ccl2of4.magdump.items.component

import ccl2of4.magdump.{MagDump, ItemStackMagDumpAddOns}
import ccl2of4.magdump.entity.EntityCartridge
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.entity.{EntityLivingBase, Entity}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.world.World

class Magazine(cartridgeName: String, cartridgeClass: Class[_ <: EntityCartridge], maxCapacity: Int) {

  class MagazineOutOfAmmoException extends Exception
  class MagazineCantReloadException extends Exception

  def numCartridges(itemStack: ItemStack): Int =
    itemStack.magazineSubTagCompound.getInteger("numCartridges")

  def setNumCartridges(itemStack: ItemStack, numCartridges: Int) =
    itemStack.magazineSubTagCompound.setInteger("numCartridges", numCartridges)

  def isEmpty(itemStack: ItemStack): Boolean = 0 == numCartridges(itemStack)

  def canEjectCartridges(itemStack: ItemStack, count: Int) = {
    numCartridges(itemStack) >= count
  }

  def ejectCartridges(itemStack: ItemStack, count: Int): Unit = {
    if (numCartridges(itemStack) < count) {
      throw new MagazineOutOfAmmoException
    }
    setNumCartridges(itemStack, numCartridges(itemStack) - count)
  }

  def canReload(itemStack: ItemStack, entityPlayer: EntityPlayer): Boolean = {
    val item = GameRegistry.findItem("MagDump", cartridgeName)
    entityPlayer.inventory.hasItem(item)
  }

  def reload(itemStack: ItemStack, entityPlayer: EntityPlayer): Unit = {
    if (!canReload(itemStack, entityPlayer)) {
      throw new MagazineCantReloadException
    }

    setNumCartridges(itemStack, 0)

    val item = GameRegistry.findItem("MagDump", cartridgeName)

    0.until(maxCapacity).toStream
      .takeWhile { _ =>
      entityPlayer.inventory.hasItem(item)
    }
      .foreach { _ =>
      entityPlayer.inventory.consumeInventoryItem(item)
      setNumCartridges(itemStack, numCartridges(itemStack) + 1)
    }

  }

}
