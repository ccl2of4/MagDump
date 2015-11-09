package ccl2of4.magdump

import ccl2of4.magdump.entity.{BuckShot, ThirtyCal}
import ccl2of4.magdump.items.{CoachGun, M60, Firearm}
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.registry.{EntityRegistry, GameRegistry}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemBow}
import org.apache.logging.log4j.Logger

@Mod(modid = "MagDump", name = "MagDump", version = "0.0.1", modLanguage = "scala")
object MagDump {

  var log : Logger = null

  @EventHandler
  def preInit(event: FMLPreInitializationEvent): Unit = {
    log = event.getModLog
    registerItems()
  }

  def registerItems(): Unit = {
    log.info("Registering Items")
    GameRegistry.registerItem(M60, "m60")
    GameRegistry.registerItem(CoachGun, "coachGun")
    GameRegistry.registerItem(new Item().setUnlocalizedName("thirtyCal").setCreativeTab(CreativeTabs.tabCombat), "thirtyCal")
    GameRegistry.registerItem(new Item().setUnlocalizedName("buckShot").setCreativeTab(CreativeTabs.tabCombat), "buckShot")
    EntityRegistry.registerModEntity(classOf[ThirtyCal], "thirtyCal", 11, this, 16, 20, true)
    EntityRegistry.registerModEntity(classOf[BuckShot], "buckShot", 11, this, 16, 20, true)
  }

}
