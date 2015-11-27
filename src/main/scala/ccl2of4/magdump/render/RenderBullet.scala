package ccl2of4.magdump.render

import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.Render
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

object RenderBullet extends Render {

  override def doRender(entity : Entity, d : Double, d1 : Double, d2 : Double, f : Float, f1 : Float): Unit = {
    bindEntityTexture(entity)
    GL11.glPushMatrix()
    GL11.glTranslatef(d.asInstanceOf[Float], d1.asInstanceOf[Float], d2.asInstanceOf[Float]);
    val tessellator = Tessellator.instance
    val f2 = 0.0F
    val f3 = 5F / 16F
    val f10 = 0.05625F
    GL11.glEnable(32826)
    GL11.glScalef(0.07F, 0.07F, 0.07F)
    GL11.glNormal3f(f10, 0.0F, 0.0F)
    tessellator.startDrawingQuads()
    tessellator.addVertexWithUV(0D, -1D, -1D, f2, f2)
    tessellator.addVertexWithUV(0D, -1D, 1D, f3, f2)
    tessellator.addVertexWithUV(0D, 1D, 1D, f3, f3)
    tessellator.addVertexWithUV(0D, 1D, -1D, f2, f3)
    tessellator.draw()
    GL11.glNormal3f(-f10, 0.0F, 0.0F)
    tessellator.startDrawingQuads()
    tessellator.addVertexWithUV(0D, 1D, -1D, f2, f2)
    tessellator.addVertexWithUV(0D, 1D, 1D, f3, f2)
    tessellator.addVertexWithUV(0D, -1D, 1D, f3, f3)
    tessellator.addVertexWithUV(0D, -1D, -1D, f2, f3)
    tessellator.draw()

    0.until(4).foreach { j : Int =>
      GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F)
      GL11.glNormal3f(0.0F, 0.0F, f10)
      tessellator.startDrawingQuads()
      tessellator.addVertexWithUV(-1D, -1D, 0.0D, f2, f2)
      tessellator.addVertexWithUV(1D, -1D, 0.0D, f3, f2)
      tessellator.addVertexWithUV(1D, 1D, 0.0D, f3, f3)
      tessellator.addVertexWithUV(-1D, 1D, 0.0D, f2, f3)
      tessellator.draw()
    }
    GL11.glDisable(32826)
    GL11.glPopMatrix()
  }

  override def getEntityTexture(p_110775_1_ : Entity): ResourceLocation = {
    new ResourceLocation("magdump", "textures/entity/musket_bullet.png")
  }
}
