package ccl2of4.magdump.keyhandler

import cpw.mods.fml.client.registry.ClientRegistry
import net.minecraft.client.settings.KeyBinding
import org.lwjgl.input.Keyboard

object FirearmKeyHandler {

  def isReloadKeyPressed: Boolean = _reloadKeyBinding.getIsKeyPressed

  def registerBindings(): Unit = {
    ClientRegistry.registerKeyBinding(_reloadKeyBinding)
  }

  private val _reloadKeyBinding = new KeyBinding("key.magdump.reload", Keyboard.KEY_R, "key.magdump.control")

}
