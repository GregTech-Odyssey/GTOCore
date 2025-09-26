package com.gtocore.client.renderer

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.GameRenderer
import org.joml.Matrix4f
import java.awt.Color

object RenderUtil {

    /**
     * Draws a border with a dynamically changing rainbow gradient.
     * The inside of the border is transparent.
     *
     * @param guiGraphics The GuiGraphics instance for rendering.
     * @param x           The x-coordinate of the top-left corner.
     * @param y           The y-coordinate of the top-left corner.
     * @param width       The width of the area to border.
     * @param height      The height of the area to border.
     * @param z           The z-level for rendering.
     * @param borderWidth The width of the border.
     */
    @JvmStatic // Expose this as a static Java method for easier Java interop
    fun drawRainbowBorder(guiGraphics: GuiGraphics, x: Int, y: Int, width: Int, height: Int, z: Int, borderWidth: Float) {
        val tesselator = Tesselator.getInstance()
        val bufferBuilder = tesselator.builder
        val matrix = guiGraphics.pose().last().pose()

        // Define the colors for the four corners
        val c1 = getDynamicRainbowColor(0.0f)       // Top-Left
        val c2 = getDynamicRainbowColor(0.25f)      // Top-Right
        val c3 = getDynamicRainbowColor(0.5f)       // Bottom-Right
        val c4 = getDynamicRainbowColor(0.75f)      // Bottom-Left

        // Enable blending and set the appropriate shader for colored vertices
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.setShader(GameRenderer::getPositionColorShader)

        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR)

        // Top border
        addVertex(bufferBuilder, matrix, x.toFloat(), y + borderWidth, z.toFloat(), c1)
        addVertex(bufferBuilder, matrix, (x + width).toFloat(), y + borderWidth, z.toFloat(), c2)
        addVertex(bufferBuilder, matrix, (x + width).toFloat(), y.toFloat(), z.toFloat(), c2)
        addVertex(bufferBuilder, matrix, x.toFloat(), y.toFloat(), z.toFloat(), c1)

        // Right border
        addVertex(bufferBuilder, matrix, x + width - borderWidth, (y + height).toFloat(), z.toFloat(), c3)
        addVertex(bufferBuilder, matrix, (x + width).toFloat(), (y + height).toFloat(), z.toFloat(), c3)
        addVertex(bufferBuilder, matrix, (x + width).toFloat(), y.toFloat(), z.toFloat(), c2)
        addVertex(bufferBuilder, matrix, x + width - borderWidth, y.toFloat(), z.toFloat(), c2)

        // Bottom border
        addVertex(bufferBuilder, matrix, x.toFloat(), (y + height).toFloat(), z.toFloat(), c4)
        addVertex(bufferBuilder, matrix, (x + width).toFloat(), (y + height).toFloat(), z.toFloat(), c3)
        addVertex(bufferBuilder, matrix, (x + width).toFloat(), y + height - borderWidth, z.toFloat(), c3)
        addVertex(bufferBuilder, matrix, x.toFloat(), y + height - borderWidth, z.toFloat(), c4)

        // Left border
        addVertex(bufferBuilder, matrix, x.toFloat(), (y + height).toFloat(), z.toFloat(), c4)
        addVertex(bufferBuilder, matrix, x + borderWidth, (y + height).toFloat(), z.toFloat(), c4)
        addVertex(bufferBuilder, matrix, x + borderWidth, y.toFloat(), z.toFloat(), c1)
        addVertex(bufferBuilder, matrix, x.toFloat(), y.toFloat(), z.toFloat(), c1)

        tesselator.end() // This draws the vertices
        RenderSystem.disableBlend()
    }

    /**
     * Adds a vertex with a specific color to the BufferBuilder.
     */
    private fun addVertex(buffer: BufferBuilder, matrix: Matrix4f, x: Float, y: Float, z: Float, color: Int) {
        val r = (color shr 16 and 0xFF) / 255.0f
        val g = (color shr 8 and 0xFF) / 255.0f
        val b = (color and 0xFF) / 255.0f
        val a = (color shr 24 and 0xFF) / 255.0f
        buffer.vertex(matrix, x, y, z).color(r, g, b, a).endVertex()
    }

    /**
     * Gets a color from the rainbow spectrum based on a hue value.
     */
    private fun getRainbowColor(hue: Float): Int {
        return Color.HSBtoRGB(hue, 0.8f, 1.0f)
    }

    /**
     * Gets a rainbow color that changes over time.
     */
    private fun getDynamicRainbowColor(offset: Float): Int {
        val cycleMillis = 4000L
        val hue = (System.currentTimeMillis() % cycleMillis).toFloat() / cycleMillis
        return getRainbowColor((hue + offset) % 1.0f)
    }
}