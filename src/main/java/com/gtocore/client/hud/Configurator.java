package com.gtocore.client.hud;

import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;

import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.BiConsumer;

import static com.tterrag.registrate.util.nullness.NonNullBiConsumer.noop;

public class Configurator extends IFancyConfiguratorButton.Toggle {

    @Getter
    @OnlyIn(Dist.CLIENT)
    private IMoveableHUD hudInstance;
    private final IGuiTexture on;
    private final IGuiTexture off;
    BiConsumer<ClickData, Boolean> onClick;
    @Getter
    @Setter
    private boolean isConfigurationMode = false;

    public Configurator(IGuiTexture on, IGuiTexture off) {
        super(on, off, () -> false, noop());

        this.on = on;
        this.off = off;
        setTooltipsSupplier(b -> List.of(
                Component.translatable(hudInstance.isEnabled() ? IMoveableHUD.HUD_TOGGLE_ON : IMoveableHUD.HUD_TOGGLE_OFF),
                Component.translatable(IMoveableHUD.HUD_DRAG)));
    }

    @Override
    public IGuiTexture getIcon() {
        return hudInstance.isEnabled() ? on : off;
    }

    @OnlyIn(Dist.CLIENT)
    public void setHudInstance(String hudID) {
        this.hudInstance = IMoveableHUD.REGISTERED_HUDS.get(hudID);
        setTooltipsSupplier(b -> List.of(
                Component.translatable(hudInstance.isEnabled() ? IMoveableHUD.HUD_TOGGLE_ON : IMoveableHUD.HUD_TOGGLE_OFF),
                Component.translatable(IMoveableHUD.HUD_DRAG)));

        onClick = (clickData, p) -> {
            if (!LDLib.isRemote()) return;
            if (clickData.button == 1) {
                if (!IMoveableHUD.addActiveHud(hudInstance)) {
                    IMoveableHUD.removeActiveHud(hudInstance);
                }
                return;
            }
            hudInstance.toggleEnabled();
        };
    }

    @Override
    public void onClick(ClickData clickData) {
        onClick.accept(clickData, hudInstance.isEnabled());
    }
}
