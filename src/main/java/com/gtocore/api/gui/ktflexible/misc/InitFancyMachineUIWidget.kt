package com.gtocore.api.gui.ktflexible

import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider

class InitFancyMachineUIWidget(mainPage: IFancyUIProvider, width: Int, height: Int, init: Runnable = Runnable { }) : FancyMachineUIWidget(mainPage, width, height) {
    init {
        init.run()
    }

    override fun detectAndSendChanges() {
        try {
            super.detectAndSendChanges()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
