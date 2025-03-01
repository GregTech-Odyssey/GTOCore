package com.gto.gtocore.integration.emi.multipage;

import com.lowdragmc.lowdraglib.emi.ModularWrapperWidget;
import com.lowdragmc.lowdraglib.jei.ModularWrapper;
import dev.emi.emi.api.widget.Widget;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

/**
 * @author EasterFG on 2025/3/1
 */
@OnlyIn(Dist.CLIENT)
public class CustomModularEmiRecipe extends ModularWrapperWidget {

    public CustomModularEmiRecipe(ModularWrapper<?> modular, List<Widget> slots) {
        super(modular, slots);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return modular.getWidget().keyPressed(keyCode, scanCode, modifiers);
    }
}
