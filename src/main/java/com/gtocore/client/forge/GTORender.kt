package com.gtocore.client.forge

import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.event.RenderLevelStageEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

@OnlyIn(Dist.CLIENT)
object GTORender {
    @SubscribeEvent
    @JvmStatic
    fun onRender(event: RenderLevelStageEvent) {
        val it = GTORenderManager.tasks.iterator()
        while (it.hasNext()) {
            val renderType = it.next()
            if (renderType.renderData.willBeDelete) it.remove()
            if (renderType.renderData.willBeCalled) renderType.render(event)
        }
    }
}
