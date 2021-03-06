package ccl2of4.magdump.items

import ccl2of4.magdump.ItemStackMagDumpAddOns
import ccl2of4.magdump.entity.cartridge.EntityCartridge
import ccl2of4.magdump.items.component.Magazine
import ccl2of4.magdump.keyhandler.FirearmKeyHandler
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
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
      if (shouldReload(itemStack, entityPlayer)) {
        entityPlayer.setItemInUse(itemStack, getMaxItemUseDuration(itemStack))
        onReloadStarted(itemStack, entityPlayer)
      }
    } else if (FIRING == state(itemStack)) {
      entityPlayer.setItemInUse(itemStack, getMaxItemUseDuration(itemStack))
      onFiringStarted(itemStack, entityPlayer)
    }
    itemStack
  }

  def shouldReload(itemStack: ItemStack, entityPlayer: EntityPlayer): Boolean =
    (magazine.canReload(itemStack, entityPlayer)
      && magazine.numCartridges(itemStack) != magazine.maxCapacity)

  override def onPlayerStoppedUsing(itemStack: ItemStack, world: World, entityPlayer: EntityPlayer, count: Int): Unit = {
    val tickNum = this.tickNum(itemStack, count)
    if (FIRING == state(itemStack)) {
      onFiringFinished(itemStack, entityPlayer, tickNum)
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

  override def onUpdate(itemStack : ItemStack, world : World, entity : Entity, count : Int, bool : Boolean) {
    super.onUpdate(itemStack, world, entity, count, bool)

    if (!shouldHandleReloadInputs(entity)) {
      return
    }

    if (FirearmKeyHandler.isReloadKeyPressed) {
      setState(itemStack, RELOADING)
    } else if (state(itemStack) != RELOADED) {
      setState(itemStack, FIRING)
    }
  }

  private def shouldHandleReloadInputs(entity: Entity) =
    entity.isInstanceOf[EntityPlayer] && !entity.asInstanceOf[EntityPlayer].isUsingItem

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
      val cartridge = cartridgeClass.getConstructor(classOf[World], classOf[Entity])
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
    entityPlayer.rotationPitch -= {
      if (entityPlayer.isSneaking) 1F else 2F
    }
    entityPlayer.rotationYaw += {
      if (entityPlayer.isSneaking) 0.25F else 0.5F
    }
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
      _magazine = new Magazine(magazineCapacity, cartridgeName)
    }
    _magazine
  }

  private var _magazine: Magazine = null

}
