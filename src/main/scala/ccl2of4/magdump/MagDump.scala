package ccl2of4.magdump

import ccl2of4.magdump.entity.{EntityBullet, EntityCartridge}
import ccl2of4.magdump.items._
import ccl2of4.magdump.render.RenderCartridge
import cpw.mods.fml.client.registry.RenderingRegistry
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.registry.{EntityRegistry, GameRegistry}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import org.apache.logging.log4j.Logger

@Mod(modid = "MagDump", name = "MagDump", version = "0.0.1", modLanguage = "scala")
object MagDump {

  var log : Logger = null

  @EventHandler
  def initLog(event: FMLPreInitializationEvent): Unit = {
    log = event.getModLog
  }

  @EventHandler
  def registerItems(event: FMLInitializationEvent): Unit = {
    log.info("Registering items.")
    GameRegistry.registerItem(M60, "m60")
    GameRegistry.registerItem(CoachGun, "coachGun")
    GameRegistry.registerItem(ThirtyCal, "thirtyCal")
    GameRegistry.registerItem(BuckShot, "buckShot")
    EntityRegistry.registerModEntity(classOf[EntityBullet], "bullet", 11, this, 16, 20, true)
    EntityRegistry.registerModEntity(classOf[EntityCartridge], "cartridge", 11, this, 16, 20, true)
  }

  @EventHandler
  @SideOnly(Side.CLIENT)
  def registerRenderers(event: FMLInitializationEvent): Unit = {
    log.info("Registering renderers,")
    RenderingRegistry.registerEntityRenderingHandler(classOf[EntityBullet], RenderCartridge)
    RenderingRegistry.registerEntityRenderingHandler(classOf[EntityCartridge], RenderCartridge)
  }

}
