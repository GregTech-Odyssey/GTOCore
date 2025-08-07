package com.gtocore.client.forge

import com.gtocore.api.gui.graphic.impl.GTOProgressClientComponent
import com.gtocore.api.gui.graphic.impl.GTOProgressToolTipComponent

import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

@OnlyIn(Dist.CLIENT)
object GTOComponentRegistry {
    @SubscribeEvent
    @JvmStatic
    fun onClientSetup(event: RegisterClientTooltipComponentFactoriesEvent) {
        event.register(
            GTOProgressToolTipComponent::class.java,
            ::GTOProgressClientComponent,
        )
    }
}
