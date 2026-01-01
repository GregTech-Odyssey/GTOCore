package com.gtocore.common.item

import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items

import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget
import com.gregtechceu.gtceu.api.item.component.IItemUIFactory
import com.gtolib.api.gui.ktflexible.root
import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory
import com.lowdragmc.lowdraglib.gui.modular.ModularUI
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture
import com.lowdragmc.lowdraglib.gui.widget.Widget

class TesterBehaviour :
    IItemUIFactory,
    IFancyUIProvider {
    lateinit var player: Player
    override fun createUI(p0: HeldItemUIFactory.HeldItemHolder, p1: Player): ModularUI? {
        player = p1
        return ModularUI(176, 166, p0, p1)
            .widget(FancyMachineUIWidget(this, 176, 166))
    }
    override fun createMainPage(p0: FancyMachineUIWidget?): Widget = root(176, 166) {
    }

    override fun attachSideTabs(configuratorPanel: TabsWidget?) {
        configuratorPanel?.mainTab = this@TesterBehaviour
    }

    override fun getTabIcon(): IGuiTexture = ItemStackTexture(Items.IRON_AXE)

    override fun getTitle(): Component? = Component.literal("UI测试器")
}
