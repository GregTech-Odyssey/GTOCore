package com.gtocore.mixin.ae2.screen;

import com.gtocore.config.GTOConfig;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.me.crafting.CraftConfirmScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.core.localization.GuiText;
import appeng.menu.me.crafting.CraftConfirmMenu;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CraftConfirmScreen.class)
public class CraftConfirmScreenMixin extends AEBaseScreen<CraftConfirmMenu> {

    @Shadow(remap = false)
    @Final
    private Button start;

    public CraftConfirmScreenMixin(CraftConfirmMenu menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Redirect(method = "updateBeforeRender", at = @At(value = "INVOKE", target = "Lappeng/menu/me/crafting/CraftingPlanSummary;isSimulation()Z", ordinal = 0), remap = false)
    private boolean gto$ignoreUnstartable(appeng.menu.me.crafting.CraftingPlanSummary instance) {
        start.setMessage((GTOConfig.INSTANCE.gamePlay.allowMissingCraftingJobs && instance.isSimulation()) ? Component.translatable("gtocore.ae.appeng.craft.missing_start") : GuiText.Start.text());
        start.setTooltip(Tooltip.create((GTOConfig.INSTANCE.gamePlay.allowMissingCraftingJobs && instance.isSimulation()) ? Component.translatable("gtocore.ae.appeng.craft.missing_start.desc") : GuiText.Start.text()));
        return !GTOConfig.INSTANCE.gamePlay.allowMissingCraftingJobs;
    }
}
