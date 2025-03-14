package com.gto.gtocore.common.machine.multiblock.part;

import com.gto.gtocore.api.machine.part.AmountConfigurationHatchPartMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public final class ThreadHatchPartMachine extends AmountConfigurationHatchPartMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ThreadHatchPartMachine.class, AmountConfigurationHatchPartMachine.MANAGED_FIELD_HOLDER);

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    private boolean repeatedRecipes = true;

    public ThreadHatchPartMachine(IMachineBlockEntity holder, int tier) {
        super(holder, tier, 1, 1 << (tier - GTValues.ZPM));
    }

    public int getCurrentThread() {
        return getCurrent();
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(
                GuiTextures.BUTTON_WORKING_ENABLE.getSubTexture(0, 0.5, 1, 0.5),
                GuiTextures.BUTTON_WORKING_ENABLE.getSubTexture(0, 0, 1, 0.5),
                () -> repeatedRecipes, (clickData, pressed) -> repeatedRecipes = pressed)
                .setTooltipsSupplier(pressed -> List.of(Component.translatable("gtocore.machine.repeated_recipes", Component.translatable(pressed ? "gtocore.machine.on" : "gtocore.machine.off")))));
    }
}
