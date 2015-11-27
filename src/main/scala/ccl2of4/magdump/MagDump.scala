package ccl2of4.magdump

import ccl2of4.magdump.entity.bullet.{EntityBullet762NATO, EntityBullet, EntityBulletBuckshot}
import ccl2of4.magdump.entity.cartridge.{EntityCartridge762NATO, EntityCartridgeBuckshot, EntityCartridge}
import ccl2of4.magdump.items._
import ccl2of4.magdump.keyhandler.FirearmKeyHandler
import ccl2of4.magdump.render.RenderBullet
import cpw.mods.fml.client.registry.RenderingRegistry
import cpw.mods.fml.common.{FMLCommonHandler, Mod}
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLPostInitializationEvent, FMLInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.registry.{EntityRegistry, GameRegistry}
import cpw.mods.fml.relauncher.{SideOnly, Side}
import org.apache.logging.log4j.Logger

@Mod(modid = "MagDump", name = "MagDump", version = "0.0.1", modLanguage = "scala")
object MagDump {

  var log : Logger = null

  @EventHandler
  def initLog(event: FMLPreInitializationEvent): Unit = {
    log = event.getModLog
  }

  @EventHandler
  @SideOnly(Side.CLIENT)
  def registerKeyBindings(event: FMLInitializationEvent): Unit = {
    FMLCommonHandler.instance().bus().register(FirearmKeyHandler)
    FirearmKeyHandler.registerBindings()
  }

  @EventHandler
  def registerItems(event: FMLInitializationEvent): Unit = {
    log.info("Registering items.")
    GameRegistry.registerItem(M60, "m60")
    GameRegistry.registerItem(CoachGun, "coachGun")
    GameRegistry.registerItem(ThirtyCal, "762NATO")
    GameRegistry.registerItem(BuckShot, "buckShot")

    EntityRegistry.registerModEntity(classOf[EntityCartridgeBuckshot], "cartridgeBuckshot", 1, this, 80, 1, false)
    EntityRegistry.registerModEntity(classOf[EntityBulletBuckshot], "bulletBuckshot", 2, this, 80, 1, true)

    EntityRegistry.registerModEntity(classOf[EntityCartridge762NATO], "cartridge762Nato", 3, this, 80, 1, false)
    EntityRegistry.registerModEntity(classOf[EntityBullet762NATO], "bullet762Nato", 4, this, 80, 1, true)
  }

  @EventHandler
  @SideOnly(Side.CLIENT)
  def registerRenderers(event: FMLPostInitializationEvent): Unit = {
    log.info("Registering renderers,")
    RenderingRegistry.registerEntityRenderingHandler(classOf[EntityBulletBuckshot], RenderBullet)
    RenderingRegistry.registerEntityRenderingHandler(classOf[EntityBullet762NATO], RenderBullet)
  }

}
