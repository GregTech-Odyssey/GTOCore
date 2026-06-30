package com.gtocore.client.forge

import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.minecraftforge.client.event.RenderLevelStageEvent

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexFormat
import it.unimi.dsi.fastutil.objects.ObjectArrayList

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object GTORenderManager {
    val tasks: ObjectArrayList<GTORenderType<*>> = ObjectArrayList<GTORenderType<*>>()

    // 形状预设：正n边形与圆（使用 PolylineThick）
    fun presetRegularPolygon(center: Vec3, level: ResourceKey<Level>, sides: Int, radius: Double, width: Float = 0.03f, color: Int = 0xFFFF0000.toInt(), durationTick: Int = 40, flickerCycle: Int = Int.MAX_VALUE, closed: Boolean = true): GTORenderType.PolylineThick {
        val pts = ArrayList<Vec3>(sides)
        val angStep = 2.0 * PI / sides
        for (i in 0 until sides) {
            val a = i * angStep
            val x = center.x + radius * cos(a)
            val z = center.z + radius * sin(a)
            pts.add(Vec3(x, center.y, z))
        }
        return GTORenderType.PolylineThick(
            GTORenderData.PolylineThickData(
                level = level,
                points = pts,
                width = width,
                color = color,
                closed = closed,
                durationTick = durationTick,
                flickerCycle = flickerCycle,
            ),
        )
    }
}

// 公用：3D加粗线段渲染器（基于相机视向的四边形条带，内部实心，QUADS）
private object ThickPolylineRenderer {
    fun drawSegments(poseStack: PoseStack, camera: Camera, colorRGBA: Int, width: Float, segments: List<Pair<Vec3, Vec3>>) {
        if (segments.isEmpty()) return
        val half = width / 2f

        poseStack.pushPose()
        // 在世界坐标渲染，移除相机位移
        poseStack.translate(-camera.position.x, -camera.position.y, -camera.position.z)

        RenderSystem.disableDepthTest()
        RenderSystem.depthMask(false)
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.setShader(GameRenderer::getPositionColorShader)
        RenderSystem.disableCull()

        val tesselator = Tesselator.getInstance()
        val buffer = tesselator.builder
        val matrix = poseStack.last().pose()
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR)

        fun emitSeg(aIn: Vec3, bIn: Vec3) {
            val ax = aIn.x.toFloat()
            val ay = aIn.y.toFloat()
            val az = aIn.z.toFloat()
            val bx = bIn.x.toFloat()
            val by = bIn.y.toFloat()
            val bz = bIn.z.toFloat()
            val dx = bx - ax
            val dy = by - ay
            val dz = bz - az
            val len = kotlin.math.sqrt(dx * dx + dy * dy + dz * dz)
            if (len < 1e-6f) return
            val ux = dx / len
            val uy = dy / len
            val uz = dz / len
            // 视向向量：从线段中点指向相机
            val mx = (ax + bx) * 0.5f
            val my = (ay + by) * 0.5f
            val mz = (az + bz) * 0.5f
            val vx = (camera.position.x - mx).toFloat()
            val vy = (camera.position.y - my).toFloat()
            val vz = (camera.position.z - mz).toFloat()
            val vlen = kotlin.math.sqrt(vx * vx + vy * vy + vz * vz)
            val nvx = if (vlen > 1e-6f) vx / vlen else 0f
            val nvy = if (vlen > 1e-6f) vy / vlen else 0f
            val nvz = if (vlen > 1e-6f) vz / vlen else 0f
            // 法线 = dir x view（或其反向），得到屏幕面近似恒宽
            var nx = uy * nvz - uz * nvy
            var ny = uz * nvx - ux * nvz
            var nz = ux * nvy - uy * nvx
            var nlen = kotlin.math.sqrt(nx * nx + ny * ny + nz * nz)
            if (nlen < 1e-6f) {
                // 退化：用全局上向量(0,1,0)替代
                nx = -uz
                ny = 0f
                nz = ux
                nlen = kotlin.math.sqrt(nx * nx + ny * ny + nz * nz)
                if (nlen < 1e-6f) return
            }
            nx /= nlen
            ny /= nlen
            nz /= nlen
            val offx = nx * half
            val offy = ny * half
            val offz = nz * half
            // 端帽延伸，减少段间缝隙
            val ex = ux * half
            val ey = uy * half
            val ez = uz * half
            val axEx = ax - ex
            val ayEy = ay - ey
            val azEz = az - ez
            val bxEx = bx + ex
            val byEy = by + ey
            val bzEz = bz + ez

            // 四边形：aL -> bL -> bR -> aR
            buffer.vertex(matrix, axEx + offx, ayEy + offy, azEz + offz).color(colorRGBA).endVertex()
            buffer.vertex(matrix, bxEx + offx, byEy + offy, bzEz + offz).color(colorRGBA).endVertex()
            buffer.vertex(matrix, bxEx - offx, byEy - offy, bzEz - offz).color(colorRGBA).endVertex()
            buffer.vertex(matrix, axEx - offx, ayEy - offy, azEz - offz).color(colorRGBA).endVertex()
        }

        for (p in segments) emitSeg(p.first, p.second)

        tesselator.end()
        RenderSystem.enableDepthTest()
        RenderSystem.depthMask(true)
        RenderSystem.disableBlend()
        RenderSystem.enableCull()
        poseStack.popPose()
    }
}

sealed class GTORenderType<T : GTORenderData>(val renderData: T) {
    open fun render(event: RenderLevelStageEvent) {
        self.renderData.startTick ?: run { self.renderData.startTick = event.renderTick }
    }
    var self: GTORenderType<T> = this

    // 新增：在XZ平面的有宽度折线（以四边形条带实现，内部实心）
    class PolylineThick(data: GTORenderData.PolylineThickData) : GTORenderType<GTORenderData.PolylineThickData>(data) {
        override fun render(event: RenderLevelStageEvent) {
            super.render(event)
            if (event.renderTick - self.renderData.startTick!! > self.renderData.durationTick) {
                self.renderData.willBeCalled = false
                self.renderData.willBeDelete = true
                return
            }
            if ((event.renderTick % self.renderData.flickerCycle) < self.renderData.flickerCycle / 2) return
            if (event.stage != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return
            with(PlayerRenderContext.create(event) ?: return) {
                if (self.renderData.level != player.level().dimension()) return
                val pts = self.renderData.points
                val n = pts.size
                if (n < 2) return
                val segs = ArrayList<Pair<Vec3, Vec3>>(if (self.renderData.closed) n else n - 1)
                for (i in 0 until n - 1) segs.add(pts[i] to pts[i + 1])
                if (self.renderData.closed) segs.add(pts[n - 1] to pts[0])
                ThickPolylineRenderer.drawSegments(
                    poseStack,
                    camera,
                    self.renderData.color,
                    self.renderData.width,
                    segs,
                )
            }
        }
    }
}
sealed class GTORenderData(val description: String) {
    var willBeDelete = false
    var willBeCalled = true
    var startTick: Int? = null

    /*
     * pos: 渲染位置
     * level: 渲染所在的维度
     * durationTick: 渲染Tick数，超过这个时间后将会被删除
     * flickerCycle: 闪烁周期，每这个周期(亮-暗)一次
     */
    data class BlockLineData(val pos: BlockPos, val level: ResourceKey<Level>, val durationTick: Int, val flickerCycle: Int = 2000000000, val lineWidth: Float = 0.03f, val color: Int = 0xFFFF0000.toInt()) : GTORenderData("渲染方块线框用于提示")

    // XZ平面有宽度折线/环
    data class PolylineThickData(val level: ResourceKey<Level>, val points: List<Vec3>, val width: Float, val color: Int = 0xFFFF0000.toInt(), val closed: Boolean = true, val durationTick: Int, val flickerCycle: Int = Int.MAX_VALUE) : GTORenderData("在XZ平面渲染加粗折线/环")
}

class PlayerRenderContext(val player: Player, val poseStack: PoseStack, val camera: Camera, val instance: Minecraft = Minecraft.getInstance()) {
    companion object {
        fun create(event: RenderLevelStageEvent): PlayerRenderContext? {
            val instance = Minecraft.getInstance()
            val player = instance.player ?: return null
            return PlayerRenderContext(player, event.poseStack, event.camera)
        }
    }
}
