package ccl2of4.magdump.keyhandler

import cpw.mods.fml.client.registry.ClientRegistry
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent
import net.minecraft.client.settings.KeyBinding
import org.lwjgl.input.Keyboard

object ReloadKeyHandler {

  def isKeyPressed: Boolean = _keyPressed

  def registerBindings(): Unit = {
    ClientRegistry.registerKeyBinding(_keyBinding)
  }

  @SubscribeEvent
  def onKeyInput(event: KeyInputEvent): Unit = {
    _keyPressed = _keyBinding.isPressed
  }

  private var _keyPressed = false
  private val _keyBinding = new KeyBinding("key.magdump.desc", Keyboard.KEY_R, "key.magdump.control")

}
