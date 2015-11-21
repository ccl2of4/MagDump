package ccl2of4.magdump.items

import ccl2of4.magdump.ItemStackMagDumpAddOns
import ccl2of4.magdump.entity.EntityCartridge
import ccl2of4.magdump.items.component.Magazine
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{EnumAction, Item, ItemStack}
import net.minecraft.world.World

abstract class Firearm(reloadTicks: Int, cartridgeName: String, cartridgeClass: Class[_ <: EntityCartridge], magazineCapacity: Int) extends Item {

  setCreativeTab(CreativeTabs.tabCombat)

  override def getMaxItemUseDuration(itemStack: ItemStack): Int = 72000
  override def onEaten(p_77654_1_ : ItemStack, p_77654_2_ : World, p_77654_3_ : EntityPlayer): ItemStack = p_77654_1_

  override def getItemUseAction(itemStack: ItemStack): EnumAction = state(itemStack) match {
    case RELOADING => EnumAction.block
    case FIRING => EnumAction.bow
    case _ => EnumAction.none
  }

  override def onItemRightClick(itemStack: ItemStack, world: World, entityPlayer: EntityPlayer): ItemStack = {
    if (RELOADED == state(itemStack)) {
      entityPlayer.setItemInUse(itemStack, getMaxItemUseDuration(itemStack))
    } else if (RELOADING == state(itemStack)) {
      if (magazine.canReload(itemStack, entityPlayer)) {
        entityPlayer.setItemInUse(itemStack, getMaxItemUseDuration(itemStack))
        onReloadStarted(itemStack, entityPlayer)
      }
    } else if (FIRING == state(itemStack)) {
      entityPlayer.setItemInUse(itemStack, getMaxItemUseDuration(itemStack))
      onFiringStarted(itemStack, entityPlayer)
    }
    itemStack
  }

  override def onPlayerStoppedUsing(itemStack: ItemStack, world: World, entityPlayer: EntityPlayer, count: Int): Unit = {
    val tickNum = this.tickNum(itemStack, count)
    if (FIRING == state(itemStack)) {
      onFiringFinished(itemStack, entityPlayer, tickNum)
      if (magazine.isEmpty(itemStack)) {
        setState(itemStack, RELOADING)
      }
    } else if (RELOADED == state(itemStack)) {
      setState(itemStack, FIRING)
    }
  }

  override def onUsingTick(itemStack: ItemStack, entityPlayer: EntityPlayer, count: Int): Unit = {
    val tickNum = this.tickNum(itemStack, count)
    if (RELOADING == state(itemStack)) {
      onReloadTick(itemStack, entityPlayer, tickNum)
    } else if (FIRING == state(itemStack)) {
      onFiringTick(itemStack, entityPlayer, tickNum)
    }
  }

  protected def tickNum(itemStack: ItemStack, count: Int): Int =
    getMaxItemUseDuration(itemStack) - count

  protected def numCartridges(numTicks: Int): Int

  protected def onFiringStarted(itemStack: ItemStack, entityPlayer: EntityPlayer): Unit = {}

  protected def onFiringTick(itemStack: ItemStack, entityPlayer: EntityPlayer, tickNum: Int): Unit

  protected def onFiringFinished(itemStack: ItemStack, entityPlayer: EntityPlayer, tickNum: Int): Unit = {
    magazine.ejectCartridges(itemStack,
      Math.min(magazine.numCartridges(itemStack), numCartridges(tickNum)))
    smoke(entityPlayer)
  }

  protected def onReloadStarted(itemStack: ItemStack, entityPlayer: EntityPlayer): Unit = {}

  protected def onReloadTick(itemStack: ItemStack, entityPlayer: EntityPlayer, tickNum: Int): Unit = {
    if (reloadTicks <= tickNum) {
      onReloadFinished(itemStack, entityPlayer)
      setState(itemStack, RELOADED)
      entityPlayer.clearItemInUse()
    }
  }

  protected def onReloadFinished(itemStack: ItemStack, entityPlayer: EntityPlayer): Unit = {
    if (magazine.canReload(itemStack, entityPlayer)) {
      magazine.reload(itemStack, entityPlayer)
    }
    reloadClick(entityPlayer)
  }

  protected def fire(itemStack: ItemStack, world: World, entityPlayer: EntityPlayer, cartridgeNum: Int): Unit = {
    if (!magazine.canEjectCartridges(itemStack, cartridgeNum)) {
      click(entityPlayer)
      return
    }

    bang(entityPlayer)
    flame(entityPlayer)
    recoil(entityPlayer)

    if (!world.isRemote) {
      val cartridge = cartridgeClass.getConstructor(classOf[World], classOf[EntityLivingBase])
        .newInstance(world, entityPlayer)
      world.spawnEntityInWorld(cartridge)
    }

  }

  protected def reloadClick(entityPlayer: EntityPlayer): Unit =
    entityPlayer.worldObj.playSoundAtEntity(entityPlayer, "random.click", 1.0F, 3.0F)

  protected def click(entityPlayer: EntityPlayer): Unit =
    entityPlayer.worldObj.playSoundAtEntity(entityPlayer, "random.click", 1.0F, 1.0F)

  protected def bang(entityPlayer: EntityPlayer): Unit =
    entityPlayer.worldObj.playSoundAtEntity(entityPlayer, "random.explode", 5.0F, 0.5F)

  protected def flame(entityPlayer: EntityPlayer): Unit = {
    // entityPlayer.worldObj.spawnParticle("flame", entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, 0, 0, 0)
  }

  protected def recoil(entityPlayer: EntityPlayer): Unit = {
    // val factor = 10F
    // val x = Math.sin((entityPlayer.rotationYaw / 180F) * Math.PI) / factor
    // val y = -Math.cos((entityPlayer.rotationYaw / 180F) * Math.PI) / factor
    // entityPlayer.addVelocity(x, 0, y)
  }

  protected def smoke(entityPlayer: EntityPlayer): Unit =
    0.until(3).foreach { i: Int =>
      // entityPlayer.worldObj.spawnParticle("smoke", entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, 0, 0, 0)
    }

  protected def state(itemStack: ItemStack) =
    itemStack.firearmSubTagCompound.getInteger("state")

  protected val RELOADING = 0
  protected val RELOADED = 1
  protected val FIRING = 2
  protected val NONE = 4

  private def setState(itemStack: ItemStack, state: Int) =
    itemStack.firearmSubTagCompound.setInteger("state", state)

  private def magazine = {
    if (null == _magazine) {
      _magazine = new Magazine(cartridgeName, cartridgeClass, magazineCapacity)
    }
    _magazine
  }

  private var _magazine: Magazine = null

}
