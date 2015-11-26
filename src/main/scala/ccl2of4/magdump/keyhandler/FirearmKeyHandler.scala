package ccl2of4.magdump.keyhandler

import cpw.mods.fml.client.registry.ClientRegistry
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent
import net.minecraft.client.settings.KeyBinding
import org.lwjgl.input.Keyboard

object FirearmKeyHandler {

  def isReloadKeyPressed: Boolean = _reloadKeyPressed

  def registerBindings(): Unit = {
    ClientRegistry.registerKeyBinding(_reloadKeyBinding)
  }

  @SubscribeEvent
  def onKeyInput(event: KeyInputEvent): Unit = {
    _reloadKeyPressed = _reloadKeyBinding.isPressed
  }

  private var _reloadKeyPressed = false
  private val _reloadKeyBinding = new KeyBinding("key.magdump.reload", Keyboard.KEY_R, "key.magdump.control")

}
